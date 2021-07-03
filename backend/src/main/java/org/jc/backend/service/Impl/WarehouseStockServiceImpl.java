package org.jc.backend.service.Impl;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.WarehouseStockMapper;
import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.OutboundProductO;
import org.jc.backend.entity.StatO.EntryProductVO;
import org.jc.backend.entity.StatO.ProductStatO;
import org.jc.backend.entity.WarehouseStockO;
import org.jc.backend.service.InboundEntryService;
import org.jc.backend.service.OutboundEntryService;
import org.jc.backend.service.WarehouseStockService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    public WarehouseStockServiceImpl(WarehouseStockMapper warehouseStockMapper,
                                     @Lazy InboundEntryService inboundEntryService,
                                     @Lazy OutboundEntryService outboundEntryService) {
        this.warehouseStockMapper = warehouseStockMapper;
        this.inboundEntryService = inboundEntryService;
        this.outboundEntryService = outboundEntryService;
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
                var pair = this.doCalculation(lastEntry, true);
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
        outboundProductMap.forEach((k, v) -> v.sort(Comparator.comparingInt(ProductStatO::getOutboundProductID)));

        var productMap = new TreeMap<>(inboundProductMap);
        for (var entry : outboundProductMap.entrySet()) { // merge map
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
        // add all products (from the next day to the specified inbound date to today) to affected list
        for (var e : productMap.subMap(date, false,
                MyUtils.todayDateString(), true).entrySet()) {
            affectedProducts.addAll(e.getValue());
        }
        // change all following records if curr record is not the last one
        for (var p : affectedProducts) {
            var pair = this.doCalculation(lastEntry, lastEntry.isInbound());
            p.setStockQuantity(pair.getLeft());
            p.setStockUnitPrice(pair.getRight());
            if (p.isInbound()) {
                inboundEntryService.updateInboundProduct(p);
            }
            else {
                outboundEntryService.updateOutboundProduct(p);
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
    public void modifyStock(InboundProductO product, String date, int quantityChange) {
        try {
            int stockID = product.getWarehouseStockID();
            WarehouseStockO stock = warehouseStockMapper.queryWarehouseStockByID(stockID);

            var triple = this.generateProductMaps(stockID);
            var productMap = triple.getRight();

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
    public void decreaseStock(OutboundProductO product) {
        try {
            int warehouseID = product.getWarehouseID();
            int skuID = product.getSkuID();
            WarehouseStockO stock =  warehouseStockMapper.queryWarehouseStockByWarehouseAndSku(warehouseID, skuID);

            int stockQuantity = stock.getStockQuantity();
            int productQuantity = product.getQuantity();
            stock.setStockQuantity(stockQuantity - productQuantity);

            warehouseStockMapper.updateStockInfo(stock);

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
    public void modifyStock(OutboundProductO product, int quantityChange) {
        try {
            WarehouseStockO stock = warehouseStockMapper.queryWarehouseStockByID(product.getWarehouseStockID());

            // deduct changes
            stock.setStockQuantity(stock.getStockQuantity() - quantityChange);

            warehouseStockMapper.updateStockInfo(stock);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
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

}
