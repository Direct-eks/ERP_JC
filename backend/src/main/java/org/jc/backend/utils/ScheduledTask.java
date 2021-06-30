package org.jc.backend.utils;

import org.jc.backend.entity.StatO.EntryProductVO;
import org.jc.backend.entity.StatO.ProductStatO;
import org.jc.backend.entity.WarehouseStockO;
import org.jc.backend.service.InboundEntryService;
import org.jc.backend.service.MiscellaneousDataService;
import org.jc.backend.service.OutboundEntryService;
import org.jc.backend.service.WarehouseStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Indexed;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Indexed
@Component
public class ScheduledTask {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    private final WarehouseStockService warehouseStockService;
    private final InboundEntryService inboundEntryService;
    private final OutboundEntryService outboundEntryService;
    private final MiscellaneousDataService miscellaneousDataService;

    public ScheduledTask(WarehouseStockService warehouseStockService,
                         InboundEntryService inboundEntryService,
                         OutboundEntryService outboundEntryService,
                         MiscellaneousDataService miscellaneousDataService) {
        this.warehouseStockService = warehouseStockService;
        this.inboundEntryService = inboundEntryService;
        this.outboundEntryService = outboundEntryService;
        this.miscellaneousDataService = miscellaneousDataService;
    }

    // delay 10 min, run every 1 hour
    @Scheduled(initialDelay = 1000 * 60 * 10, fixedDelay = 1000 * 60 * 60)
    public void calculateStockPrices() {
        logger.info("calculateStockPrices task begin");

        // todo calculate prices
        WarehouseStockO warehouseStock = warehouseStockService.getWarehouseStockByID(-1);
        List<ProductStatO> inboundProducts = inboundEntryService.getAllInboundProducts(-1);
        List<ProductStatO> outboundProducts = outboundEntryService.getAllOutboundProducts(-1);

        var inboundProductMap = inboundProducts.parallelStream()
                .collect(Collectors.groupingByConcurrent(ProductStatO::getEntryDate));

        var outboundProductMap = outboundProducts.parallelStream()
                .collect(Collectors.groupingByConcurrent(ProductStatO::getShipmentDate));

        miscellaneousDataService.updateLastWarehouseStockUpdateTime();
        logger.info("calculateStockPrices task end");
    }
}
