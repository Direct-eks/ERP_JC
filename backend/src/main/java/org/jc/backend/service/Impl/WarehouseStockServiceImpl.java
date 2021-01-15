package org.jc.backend.service.Impl;

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

    public List<WarehouseStockO> getWarehouseStocksBySku(int id) {
        return warehouseStockMapper.queryWarehouseStocksBySku(id);
    }

    public WarehouseStockO getWarehouseStockByWarehouseAndSku(int warehouse, int sku) {
        return warehouseStockMapper.queryWarehouseStockByWarehouseAndSku(warehouse, sku);
    }

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
