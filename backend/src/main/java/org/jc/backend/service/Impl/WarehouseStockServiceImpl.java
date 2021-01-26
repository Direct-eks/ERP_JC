package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.WarehouseStockMapper;
import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.OutboundProductO;
import org.jc.backend.entity.StatO.EntryProductVO;
import org.jc.backend.entity.WarehouseStockO;
import org.jc.backend.service.InboundEntryService;
import org.jc.backend.service.OutboundEntryService;
import org.jc.backend.service.WarehouseStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
            return warehouseStockMapper.insertNewWarehouseStock(warehouseStockO);
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

    @Transactional
    @Override
    public void increaseStockAndUpdateStockUnitPrice(InboundProductO product) {
        try {
            int warehouseID = product.getWarehouseID();
            int skuID = product.getSkuID();
            WarehouseStockO stock =  warehouseStockMapper.queryWarehouseStockByWarehouseAndSku(warehouseID, skuID);

            int stockQuantity = stock.getStockQuantity();
            BigDecimal stockUnitPriceWithoutTax = new BigDecimal(stock.getStockUnitPriceWithoutTax());
            int productQuantity = product.getQuantity();
            BigDecimal productUnitPriceWithoutTax = new BigDecimal(product.getUnitPriceWithoutTax());

            String calculatedPrice = productUnitPriceWithoutTax.multiply(BigDecimal.valueOf(productQuantity))
                    .add(stockUnitPriceWithoutTax.multiply(BigDecimal.valueOf(stockQuantity)))
                    .divide(BigDecimal.valueOf(productQuantity + stockQuantity), RoundingMode.HALF_EVEN)
                    .toPlainString();

            warehouseStockMapper.increaseStockAndChangeStockUnitPrice(stock.getWarehouseStockID(),
                    calculatedPrice, stockQuantity + productQuantity);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void increaseStockAndUpdateStockUnitPrice(InboundProductO modifiedProduct,
                                                     InboundProductO originProduct) {
        try {
            int warehouseID = originProduct.getWarehouseID();
            int skuID = originProduct.getSkuID();
            WarehouseStockO stock =  warehouseStockMapper.queryWarehouseStockByWarehouseAndSku(warehouseID, skuID);

            // update stock quantity based on modified and origin product and warehouseStock quantity
            int stockQuantity = stock.getStockQuantity();
            BigDecimal stockUnitPriceWithoutTax = new BigDecimal(stock.getStockUnitPriceWithoutTax());
            int oldProductQuantity = originProduct.getQuantity();
            int newProductQuantity = modifiedProduct.getQuantity();
            int productQuantityChange = newProductQuantity - oldProductQuantity;
            BigDecimal newProductUnitPriceWithoutTax = new BigDecimal(modifiedProduct.getUnitPriceWithoutTax());

            String calculatedPrice = newProductUnitPriceWithoutTax.multiply(BigDecimal.valueOf(newProductQuantity))
                    .add(stockUnitPriceWithoutTax.multiply(BigDecimal.valueOf(stockQuantity - oldProductQuantity)))
                    .divide(BigDecimal.valueOf(stockQuantity + productQuantityChange), RoundingMode.HALF_EVEN)
                    .toPlainString();

            warehouseStockMapper.increaseStockAndChangeStockUnitPrice(stock.getWarehouseStockID(),
                    calculatedPrice, stockQuantity + productQuantityChange);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void decreaseStock(OutboundProductO product) {
        try {
            int warehouseID = product.getWarehouseID();
            int skuID = product.getSkuID();
            WarehouseStockO stock =  warehouseStockMapper.queryWarehouseStockByWarehouseAndSku(warehouseID, skuID);

            int stockQuantity = stock.getStockQuantity();
            int productQuantity = product.getQuantity();

            warehouseStockMapper.decreaseStock(stock.getWarehouseStockID(), stockQuantity - productQuantity);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void decreaseStock(OutboundProductO modifiedProduct, OutboundProductO originProduct) {
        try {
            int warehouseID = originProduct.getWarehouseID();
            int skuID = originProduct.getSkuID();
            WarehouseStockO stock =  warehouseStockMapper.queryWarehouseStockByWarehouseAndSku(warehouseID, skuID);

            // update stock quantity based on modified and origin product and warehouseStock quantity
            int stockQuantity = stock.getStockQuantity();
            int oldProductQuantity = originProduct.getQuantity();
            int newProductQuantity = modifiedProduct.getQuantity();
            int productQuantityChange = newProductQuantity - oldProductQuantity;

            warehouseStockMapper.decreaseStock(stock.getWarehouseStockID(),stockQuantity - productQuantityChange);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
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
