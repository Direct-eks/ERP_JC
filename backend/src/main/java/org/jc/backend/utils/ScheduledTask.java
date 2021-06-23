package org.jc.backend.utils;

import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.OutboundProductO;
import org.jc.backend.entity.StatO.EntryProductVO;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        List<WarehouseStockO> warehouseStocks = warehouseStockService.getAllWarehouseStocks();
        List<InboundProductO> inboundProducts = inboundEntryService.getAllInboundProducts();
        List<OutboundProductO> outboundProducts = outboundEntryService.getAllOutboundProducts();

        var warehouseStockMap = warehouseStocks.parallelStream()
                .collect(Collectors.toMap(WarehouseStockO::getWarehouseStockID, w -> w));

//        inboundProducts.parallelStream().map(p -> {
//            EntryProductVO vo = new EntryProductVO();
//            BeanUtils.copyProperties(p, vo);
//            return vo;
//        }).collect(Collectors.groupingByConcurrent(EntryProductVO::getWarehouseStockID))
//        .forEach((k, listOfProducts) -> {
//            listOfProducts.sort(Comparator.comparing(p -> p.get));
//        });
//
//        outboundProducts.parallelStream().map(p -> {
//            EntryProductVO vo = new EntryProductVO();
//            BeanUtils.copyProperties(p, vo);
//            return vo;
//        }).collect(Collectors.groupingByConcurrent(EntryProductVO::getWarehouseStockID));

        miscellaneousDataService.updateLastWarehouseStockUpdateTime();
        logger.info("calculateStockPrices task end");
    }
}
