package org.jc.backend.service.Impl;

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

import static org.jc.backend.utils.MyUtils.myScale;
import static org.jc.backend.utils.MyUtils.myRoundingMode;

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
            stock.setStockQuantity(stock.getStockQuantity() + product.getQuantity());
            warehouseStockMapper.updateStockQuantity(stock);


            List<ProductStatO> inboundProducts = inboundEntryService.getAllInboundProducts(stockID);
            List<ProductStatO> outboundProducts = outboundEntryService.getAllOutboundProducts(stockID);

            var inboundProductMap = inboundProducts.parallelStream()
                    .collect(Collectors.groupingBy(ProductStatO::getEntryDate, TreeMap::new, Collectors.toList()));

            var entry = inboundProductMap.floorEntry(date);
            if (entry == null) {
                return;
            }

            // get last inbound record
            ProductStatO lastEntry = entry.getValue().get(entry.getValue().size() - 1);
            int prevStockQuantity = lastEntry.getStockQuantity();
            int currStockQuantity = prevStockQuantity + lastEntry.getQuantity();
            BigDecimal prevStockPrice = new BigDecimal(lastEntry.getStockUnitPrice());
            BigDecimal productUnitPrice = new BigDecimal(lastEntry.getUnitPriceWithoutTax());
            BigDecimal currStockPrice = prevStockPrice.multiply(BigDecimal.valueOf(prevStockQuantity))
                    .add(productUnitPrice.multiply(BigDecimal.valueOf(lastEntry.getQuantity())))
                    .divide(BigDecimal.valueOf(currStockQuantity), myScale, myRoundingMode);
            product.setStockQuantity(currStockQuantity);
            product.setStockUnitPrice(currStockPrice.toPlainString());

            lastEntry = new ProductStatO();
            BeanUtils.copyProperties(product, lastEntry);
            // change all following records if curr record is not the last one
            for (var e : inboundProductMap.subMap(date, false,
                    MyUtils.todayDateString(), true).entrySet()) {
                for (var p : e.getValue()) {

                }
            }

            var outboundProductMap = outboundProducts.parallelStream()
                    .collect(Collectors.groupingBy(ProductStatO::getShipmentDate, TreeMap::new, Collectors.toList()));

            for (var e : outboundProductMap.subMap(date, true,
                    MyUtils.todayDateString(), true).entrySet()) {
                for (var p : e.getValue()) {

                }
            }


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
    public void modifyStock(InboundProductO product, int quantityChange) {
        try {
            WarehouseStockO stock = warehouseStockMapper.queryWarehouseStockByID(product.getWarehouseStockID());

            // add changes
            stock.setStockQuantity(stock.getStockQuantity() + quantityChange);

            warehouseStockMapper.updateStockQuantity(stock);

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

            warehouseStockMapper.updateStockQuantity(stock);

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

            warehouseStockMapper.updateStockQuantity(stock);

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
