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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                                     InboundEntryService inboundEntryService,
                                     OutboundEntryService outboundEntryService) {
        this.warehouseStockMapper = warehouseStockMapper;
        this.inboundEntryService = inboundEntryService;
        this.outboundEntryService = outboundEntryService;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional(readOnly = true)
    public List<WarehouseStockO> getWarehouseStocksBySku(int id) {
        return warehouseStockMapper.queryWarehouseStocksBySku(id);
    }

    @Transactional
    public int insertNewWarehouseStock(WarehouseStockO warehouseStockO) {
        try {
            return warehouseStockMapper.insertNewWarehouseStock(warehouseStockO);
        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production
            logger.error("insert failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public WarehouseStockO getWarehouseStockByWarehouseAndSku(int warehouse, int sku) {
        return warehouseStockMapper.queryWarehouseStockByWarehouseAndSku(warehouse, sku);
    }

    @Transactional
    public void updateWarehouseStockUnitPriceAndQuantity(InboundProductO product) {
        try {
            int warehouseID = product.getWarehouseID();
            int skuID = product.getSkuID();
            WarehouseStockO stock =  warehouseStockMapper.queryWarehouseStockByWarehouseAndSku(warehouseID, skuID);

            int stockQuantity = stock.getStockQuantity();
            double stockUnitPriceWithoutTax = stock.getStockUnitPriceWithoutTax();
            int productQuantity = product.getQuantity();
            double productUnitPriceWithoutTax = product.getUnitPriceWithoutTax();
            warehouseStockMapper.updateWarehouseStockUnitPriceAndQuantityByID(stock.getWarehouseStockID(),
                    (productUnitPriceWithoutTax * productQuantity + stockUnitPriceWithoutTax * stockQuantity)
                            / (productQuantity + stockQuantity), stockQuantity + productQuantity);

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production
            logger.error("insert failed");
            throw e;
        }
    }

    @Transactional
    public void updateWarehouseStockUnitPriceAndQuantity(InboundProductO modifiedProduct,
                                                         InboundProductO originProduct) {
        try {
            int warehouseID = originProduct.getWarehouseID();
            int skuID = originProduct.getSkuID();
            WarehouseStockO stock =  warehouseStockMapper.queryWarehouseStockByWarehouseAndSku(warehouseID, skuID);

            // update stock quantity based on modified and origin product and warehouseStock quantity
            int stockQuantity = stock.getStockQuantity();
            double stockUnitPriceWithoutTax = stock.getStockUnitPriceWithoutTax();
            int oldProductQuantity = originProduct.getQuantity();
            int newProductQuantity = modifiedProduct.getQuantity();
            int productQuantityChange = newProductQuantity - oldProductQuantity;
            double newProductUnitPriceWithoutTax = modifiedProduct.getUnitPriceWithoutTax();
            warehouseStockMapper.updateWarehouseStockUnitPriceAndQuantityByID(stock.getWarehouseStockID(),
                    (newProductUnitPriceWithoutTax * newProductQuantity +
                            stockUnitPriceWithoutTax * (stockQuantity - oldProductQuantity))
                            / (stockQuantity + productQuantityChange), stockQuantity + productQuantityChange);

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production
            logger.error("insert failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<EntryProductVO> getProductsByWarehouseStockID(int id) {

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
    }
}
