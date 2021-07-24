package org.jc.backend.service.Impl;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.jc.backend.dao.WarehouseStockMapper;
import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.OutboundProductO;
import org.jc.backend.entity.StatO.EntryProductVO;
import org.jc.backend.entity.StatO.ProductStatO;
import org.jc.backend.entity.StatO.StockStatO;
import org.jc.backend.entity.WarehouseProductO;
import org.jc.backend.entity.WarehouseStockO;
import org.jc.backend.service.*;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static org.jc.backend.utils.MyUtils.myRoundingMode;
import static org.jc.backend.utils.MyUtils.myScale;

@Service
public class WarehouseStockServiceImpl implements WarehouseStockService {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseStockServiceImpl.class);

    private final WarehouseStockMapper warehouseStockMapper;
    private final InboundEntryService inboundEntryService;
    private final OutboundEntryService outboundEntryService;
    private final WarehouseEntryService warehouseEntryService;
    private final ModelService modelService;
    private final SkuService skuService;

    public WarehouseStockServiceImpl(WarehouseStockMapper warehouseStockMapper,
                                     @Lazy InboundEntryService inboundEntryService,
                                     @Lazy OutboundEntryService outboundEntryService,
                                     @Lazy WarehouseEntryService warehouseEntryService,
                                     ModelService modelService,
                                     @Lazy SkuService skuService) {
        this.warehouseStockMapper = warehouseStockMapper;
        this.inboundEntryService = inboundEntryService;
        this.outboundEntryService = outboundEntryService;
        this.warehouseEntryService = warehouseEntryService;
        this.modelService = modelService;
        this.skuService = skuService;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional(readOnly = true)
    @Override
    public List<WarehouseStockO> getWarehouseStocksBySku(int id) {
        return warehouseStockMapper.queryWarehouseStocksBySku(id);
    }

    @Transactional
    @Override
    public int insertNewWarehouseStock(WarehouseStockO warehouseStockO) {
        try {
            warehouseStockMapper.insertNewWarehouseStock(warehouseStockO);
            return warehouseStockO.getWarehouseStockID();
        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("insert failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public WarehouseStockO getWarehouseStockByWarehouseAndSku(int warehouse, int sku) {
        try {
            return warehouseStockMapper.queryWarehouseStockByWarehouseAndSku(warehouse, sku);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    /**
     * @return -1 if products (involve no presales)
     * or 0 if products (involve only presales)
     * or 1 if products (involve mixed presales and non-presales)
     */
    @Transactional(readOnly = true)
    @Override
    public int passPresaleCheck(List<InboundProductO> inboundProducts) {
        try {
            // check if all products are non-presales
            if (inboundProducts.stream().noneMatch(p -> {
                int id = p.getWarehouseStockID();
                if (id == -1) return false;
                return warehouseStockMapper.queryWarehouseStockByID(id).getStockQuantity() < 0;
            })) {
                return -1;
            }
            // check if all products are presales
            if (inboundProducts.stream().allMatch(p -> {
                int id = p.getWarehouseStockID();
                if (id == -1) return false;
                return warehouseStockMapper.queryWarehouseStockByID(id).getStockQuantity() < 0;
            })) {
                return 0;
            }

            return 1;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    /**
     * For usage of increase stock quantity for inbound product entry, modify replenishment if presale exists
     */
    @Transactional
    @Override
    public void increaseStock(InboundProductO product, String date) {
        try {
            int stockID = product.getWarehouseStockID();
            WarehouseStockO stock =  warehouseStockMapper.queryWarehouseStockByID(stockID);

            var triple = this.generateProductMaps(stockID);
            var inboundProductMap = triple.getLeft();
            var outboundProductMap = triple.getMiddle();
            var productMap = triple.getRight();

            ProductStatO lastEntry;
            List<ProductStatO> affectedProducts = new ArrayList<>();

            // find nearest inbound record
            var entry = inboundProductMap.floorEntry(date);
            if (entry == null) { // first inbound record, extract initial value from stock record
                product.setStockQuantity(stock.getInitialStockQuantity());
                product.setStockUnitPrice(stock.getStockUnitPriceWithoutTax());
                // only need to add today's outbound record to affectedProducts, due to
                // the restrict that inbound product entry date must be before presale date
                if (outboundProductMap.get(date) != null) {
                    affectedProducts.addAll(outboundProductMap.get(date));
                }
            }
            else { // exist inbound record from same day or previous days
                if (entry.getKey().compareTo(date) == 0) { // same day, find last inbound record
                    lastEntry = entry.getValue().get(entry.getValue().size() - 1);
                    if (outboundProductMap.get(date) != null) { // add today's outbound record to affectedProducts
                        affectedProducts.addAll(outboundProductMap.get(date));
                    }
                }
                else { // previous days
                    var sameDayProducts = outboundProductMap.get(date);
                    if (sameDayProducts == null) { // today has neither inbound/outbound records, get nearest record
                        var previousDaysProducts = productMap.lowerEntry(date);
                        lastEntry = previousDaysProducts.getValue().get(previousDaysProducts.getValue().size() - 1);
                    }
                    else { // get today's last inbound record
                        lastEntry = sameDayProducts.get(sameDayProducts.size() - 1);
                        affectedProducts.addAll(sameDayProducts); // add today's outbound record to affectedProducts
                    }
                }
                var pair = this.doCalculation(lastEntry, lastEntry.isInbound());
                product.setStockQuantity(pair.getLeft());
                product.setStockUnitPrice(pair.getRight());
            }
            lastEntry = new ProductStatO();
            BeanUtils.copyProperties(product, lastEntry);
            lastEntry.setInbound(true);

            var pair = this.addRestProductsAndCalculate(productMap, affectedProducts, date, lastEntry);
            stock.setStockQuantity(pair.getLeft());
            stock.setStockUnitPriceWithoutTax(pair.getRight());
            warehouseStockMapper.updateStockInfo(stock);

            // update sku total
            skuService.updateStockInfo(stock.getSkuID());

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    private Triple<TreeMap<String, List<ProductStatO>>,
            TreeMap<String, List<ProductStatO>>,
            TreeMap<String, List<ProductStatO>>> generateProductMaps(int stockID) {
        List<ProductStatO> inboundProducts = inboundEntryService.getAllInboundProducts(stockID);
        List<ProductStatO> outboundProducts = outboundEntryService.getAllOutboundProducts(stockID);

        var inboundProductMap = inboundProducts.parallelStream()
                .peek(p -> p.setInbound(true))
                .collect(Collectors.groupingBy(ProductStatO::getEntryDate, TreeMap::new, Collectors.toList()));
        inboundProductMap.forEach((k, v) -> v.sort(Comparator.comparingInt(ProductStatO::getInboundProductID)));

        var outboundProductMap = outboundProducts.parallelStream()
                .peek(p -> p.setInbound(false))
                .collect(Collectors.groupingBy(ProductStatO::getShipmentDate, TreeMap::new, Collectors.toList()));

        List<ProductStatO> warehouseInProducts = warehouseEntryService
                .getAllProductsByWarehouseStockID(stockID, true);
        List<ProductStatO> warehouseOutProducts = warehouseEntryService
                .getAllProductsByWarehouseStockID(stockID, false);

        var warehouseInProductMap = warehouseInProducts.parallelStream()
                .peek(p -> {p.setInbound(true); p.setWarehouseProduct(true);})
                .collect(Collectors.groupingBy(ProductStatO::getEntryDate, TreeMap::new, Collectors.toList()));

        var warehouseOutProductMap = warehouseOutProducts.parallelStream()
                .peek(p -> {p.setInbound(false); p.setWarehouseProduct(true);})
                .collect(Collectors.groupingBy(ProductStatO::getShipmentDate, TreeMap::new, Collectors.toList()));

        // merge inbound products map
        for (var entry : warehouseInProductMap.entrySet()) {
            entry.getValue().sort(Comparator.comparingInt(ProductStatO::getInboundProductID));
            String key = entry.getKey();
            var value = entry.getValue();
            if (inboundProductMap.containsKey(key)) {
                inboundProductMap.get(key).addAll(value);
            }
            else {
                inboundProductMap.put(key, value);
            }
        }

        // merge outbound products map
        for (var entry : warehouseOutProductMap.entrySet()) {
            entry.getValue().sort(Comparator.comparingInt(ProductStatO::getOutboundProductID));
            String key = entry.getKey();
            var value = entry.getValue();
            if (outboundProductMap.containsKey(key)) {
                outboundProductMap.get(key).addAll(value);
            }
            else {
                outboundProductMap.put(key, value);
            }
        }

        var productMap = new TreeMap<>(inboundProductMap);
        for (var entry : outboundProductMap.entrySet()) { // merge map
            entry.getValue().sort(Comparator.comparingInt(ProductStatO::getOutboundProductID));
            String key = entry.getKey();
            var value = entry.getValue();
            if (productMap.containsKey(key)) {
                productMap.get(key).addAll(value);
            }
            else {
                productMap.put(key, value);
            }
        }

        return ImmutableTriple.of(inboundProductMap, outboundProductMap, productMap);
    }

    private Pair<Integer, String> doCalculation(ProductStatO lastEntry, boolean isInbound) {
        int prevStockQuantity = lastEntry.getStockQuantity();
        int currStockQuantity;
        BigDecimal currStockPrice;
        if (isInbound) {
            currStockQuantity = prevStockQuantity + lastEntry.getQuantity();
            BigDecimal prevStockPrice = new BigDecimal(lastEntry.getStockUnitPrice());
            BigDecimal productUnitPrice = new BigDecimal(lastEntry.getUnitPriceWithoutTax());
            if (currStockQuantity == 0) {
                currStockPrice = new BigDecimal(0);
            }
            else {
                currStockPrice = prevStockPrice.multiply(BigDecimal.valueOf(prevStockQuantity))
                        .add(productUnitPrice.multiply(BigDecimal.valueOf(lastEntry.getQuantity())))
                        .divide(BigDecimal.valueOf(currStockQuantity), myScale, myRoundingMode);
            }
        }
        else {
            currStockQuantity = prevStockQuantity - lastEntry.getQuantity();
            currStockPrice = new BigDecimal(lastEntry.getStockUnitPrice());
        }

        return new ImmutablePair<>(currStockQuantity, currStockPrice.toPlainString());
    }

    private Pair<Integer, String> addRestProductsAndCalculate(
            TreeMap<String, List<ProductStatO>> productMap,
            List<ProductStatO> affectedProducts,
            String date,
            ProductStatO lastEntry) {
        // add all products (from the next day of the specified date to today) to affected list
        for (var e : productMap.subMap(date, false,
                MyUtils.todayDateString(), true).entrySet()) {
            affectedProducts.addAll(e.getValue());
        }
        // change all following records if curr record is not the last one
        for (var p : affectedProducts) {
            // todo re-set unit price for warehouse product

            var pair = this.doCalculation(lastEntry, lastEntry.isInbound());
            p.setStockQuantity(pair.getLeft());
            p.setStockUnitPrice(pair.getRight());
            if (p.isWarehouseProduct()) {
                warehouseEntryService.updateProductForStock(p, p.isInbound());
            }
            else {
                if (p.isInbound()) {
                    inboundEntryService.updateInboundProduct(p);
                }
                else {
                    outboundEntryService.updateOutboundProduct(p);
                }
            }

            lastEntry = p;
        }

        return this.doCalculation(lastEntry, lastEntry.isInbound());
    }

    /**
     * For usage of modify stock quantity for inbound product modification
     */
    @Transactional
    @Override
    public void modifyStock(InboundProductO product, String date) {
        try {
            int stockID = product.getWarehouseStockID();
            WarehouseStockO stock = warehouseStockMapper.queryWarehouseStockByID(stockID);

            var productMap = this.generateProductMaps(stockID).getRight();

            ProductStatO lastEntry = new ProductStatO();
            BeanUtils.copyProperties(product, lastEntry);
            lastEntry.setInbound(true);
            List<ProductStatO> affectedProducts = new ArrayList<>();

            int index = -1;
            var sameDayProducts = productMap.get(date);
            for (var p : sameDayProducts) {
                if (p.getInboundProductID() == product.getInboundProductID()) {
                    index = sameDayProducts.indexOf(p);
                    break;
                }
            }
            for (int i = index; i < sameDayProducts.size() - 1; i++) {
                affectedProducts.add(sameDayProducts.get(i));
            }

            var pair = this.addRestProductsAndCalculate(productMap, affectedProducts, date, lastEntry);
            stock.setStockQuantity(pair.getLeft());
            stock.setStockUnitPriceWithoutTax(pair.getRight());
            warehouseStockMapper.updateStockInfo(stock);

            // update sku total
            skuService.updateStockInfo(stock.getSkuID());

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    /**
     * for usage of decrease stock quantity for outbound product entry
     */
    @Transactional
    @Override
    public void decreaseStock(OutboundProductO product, String date) {
        try {
            int stockID = product.getWarehouseStockID();
            WarehouseStockO stock =  warehouseStockMapper.queryWarehouseStockByID(stockID);

            var triple = this.generateProductMaps(stockID);
            var productMap = triple.getRight();

            ProductStatO lastEntry;
            List<ProductStatO> affectedProducts = new ArrayList<>();

            // get nearest record
            var entry = productMap.floorEntry(date);
            if (entry == null) { // no other records, possibly presale
                product.setStockQuantity(stock.getInitialStockQuantity());
                product.setStockUnitPrice(stock.getInitialStockUnitPrice());
            }
            else { // extract last record
                lastEntry = entry.getValue().get(entry.getValue().size() - 1);
                var pair = this.doCalculation(lastEntry, lastEntry.isInbound());
                product.setStockQuantity(pair.getLeft());
                product.setStockUnitPrice(pair.getRight());
            }
            lastEntry = new ProductStatO();
            BeanUtils.copyProperties(product, lastEntry);
            lastEntry.setInbound(false);

            var pair = this.addRestProductsAndCalculate(productMap, affectedProducts, date, lastEntry);
            stock.setStockQuantity(pair.getLeft());
            stock.setStockUnitPriceWithoutTax(pair.getRight());
            warehouseStockMapper.updateStockInfo(stock);

            // update sku total
            skuService.updateStockInfo(stock.getSkuID());

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    /**
     * For usage of modify stock quantity for inbound product modification
     */
    @Transactional
    @Override
    public void modifyStock(OutboundProductO product, String date) {
        try {
            int stockID = product.getWarehouseStockID();
            WarehouseStockO stock = warehouseStockMapper.queryWarehouseStockByID(stockID);

            var productMap = this.generateProductMaps(stockID).getRight();

            ProductStatO lastEntry = new ProductStatO();
            BeanUtils.copyProperties(product, lastEntry);
            lastEntry.setInbound(false);
            List<ProductStatO> affectedProducts = new ArrayList<>();

            int index = -1;
            var sameDayProducts = productMap.get(date);
            for (var p : sameDayProducts) {
                if (p.getOutboundProductID() == product.getOutboundProductID()) {
                    index = sameDayProducts.indexOf(p);
                    break;
                }
            }
            for (int i = index; i < sameDayProducts.size() - 1; i++) {
                affectedProducts.add(sameDayProducts.get(i));
            }

            var pair = this.addRestProductsAndCalculate(productMap, affectedProducts, date, lastEntry);
            stock.setStockQuantity(pair.getLeft());
            stock.setStockUnitPriceWithoutTax(pair.getRight());
            warehouseStockMapper.updateStockInfo(stock);

            // update sku total
            skuService.updateStockInfo(stock.getSkuID());

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void increaseStock(WarehouseProductO product, String date, String type) {
        InboundProductO inboundProductO = new InboundProductO();
        inboundProductO.setWarehouseStockID(product.getWarehouseStockID());
        this.increaseStock(inboundProductO, date);
        product.setStockQuantity(inboundProductO.getStockQuantity());
        product.setStockUnitPrice(inboundProductO.getStockUnitPrice());
    }

    @Transactional
    @Override
    public void decreaseStock(WarehouseProductO product, String date, String type) {
        OutboundProductO outboundProductO = new OutboundProductO();
        BeanUtils.copyProperties(product, outboundProductO);
        outboundProductO.setUnitPriceWithoutTax(product.getUnitPrice());
        this.decreaseStock(outboundProductO, date);
        product.setStockQuantity(outboundProductO.getStockQuantity());
        product.setStockUnitPrice(outboundProductO.getStockUnitPrice());
    }

    @Transactional
    @Override
    public void modifyStock(WarehouseProductO product, String date, boolean isInbound) {
        if (isInbound) {
            InboundProductO inboundProductO = new InboundProductO();
            inboundProductO.setWarehouseStockID(product.getWarehouseStockID());
            inboundProductO.setInboundProductID(product.getWarehouseProductID());
            this.modifyStock(inboundProductO, date);
            // todo modify unit price
        }
        else {
            OutboundProductO outboundProductO = new OutboundProductO();
            outboundProductO.setWarehouseStockID(product.getWarehouseStockID());
            outboundProductO.setOutboundProductID(product.getWarehouseProductID());
            this.modifyStock(outboundProductO, date);
            // todo modify unit price
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<EntryProductVO> getProductsByWarehouseStockID(int id) {
        try {
            List<InboundProductO> inboundProducts = inboundEntryService.getProductsByWarehouseID(id);
            List<OutboundProductO> outboundProducts = outboundEntryService.getProductsByWarehouseID(id);

            List<EntryProductVO> products = new ArrayList<>();
            inboundProducts.forEach(product -> {
                EntryProductVO newProduct = new EntryProductVO();
                BeanUtils.copyProperties(product, newProduct);
                newProduct.setEntryID(product.getInboundEntryID());
                products.add(newProduct);
            });
            outboundProducts.forEach(product -> {
                EntryProductVO newProduct = new EntryProductVO();
                BeanUtils.copyProperties(product, newProduct);
                newProduct.setEntryID(product.getOutboundEntryID());
                products.add(newProduct);
            });

            products.sort(Comparator.comparing(p -> p.getEntryID().substring(2)));
            return products;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Override
    public void updateWarehouseStockInitialInfo(List<WarehouseStockO> stocks) {
        try {
            for (var stock : stocks) {
                warehouseStockMapper.updateInitialInfo(stock);
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<StockStatO> getWarehouseStockReport(int categoryID, int warehouseID, String factoryBrand, String code) {
        try {
            var categories = modelService.getModelCategories();
            String treeLevel = null;
            for (var c : categories) {
                if (c.getModelCategoryID() == categoryID) {
                    treeLevel = c.getTreeLevel();
                    break;
                }
            }
            treeLevel = treeLevel == null ? "" : treeLevel;

            var list = warehouseStockMapper.queryWarehouseStocks(treeLevel);
            list.removeIf(item -> {
                if (warehouseID != -1 && item.getWarehouseID() != warehouseID) {
                    return true;
                }
                if (!factoryBrand.isBlank() && !item.getFactoryCode().equals(factoryBrand)) {
                    return true;
                }
                if (!code.isBlank() && !item.getCode().equals(code)) {
                    return true;
                }
                return false;
            });
            return list;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public void exportStockReport(HttpServletResponse response) {
        try {
            var list = warehouseStockMapper.queryWarehouseStocks("");

            SXSSFWorkbook workbook = new SXSSFWorkbook();
            Sheet sheet = workbook.createSheet("库存报表");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("所在分类");
            header.createCell(1).setCellValue("代号");
            header.createCell(2).setCellValue("厂牌");
            header.createCell(3).setCellValue("单位");
            header.createCell(4).setCellValue("库存数量");
            header.createCell(5).setCellValue("库存单价");
            header.createCell(6).setCellValue("库房名称");

            int rowIndex = 1;
            for (var item : list) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(item.getCategoryCode());
                row.createCell(1).setCellValue(item.getCode());
                row.createCell(2).setCellValue(item.getFactoryCode());
                row.createCell(3).setCellValue(item.getUnitName());
                row.createCell(4).setCellValue(item.getStockQuantity());
                row.createCell(5).setCellValue(item.getStockUnitPrice());
                row.createCell(6).setCellValue(item.getWarehouseName());
            }

            response.reset();
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    URLEncoder.encode("库存报表.xlsx", StandardCharsets.UTF_8));

            workbook.write(response.getOutputStream());
            workbook.dispose();
            workbook.close();
            response.flushBuffer();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        } catch (IOException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("io failed");
        }
    }
}
