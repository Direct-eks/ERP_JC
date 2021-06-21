package org.jc.backend.utils;

import org.jc.backend.service.MiscellaneousDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Indexed;

@Indexed
@Component
public class ScheduledTask {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    private final MiscellaneousDataService miscellaneousDataService;

    public ScheduledTask(MiscellaneousDataService miscellaneousDataService) {
        this.miscellaneousDataService = miscellaneousDataService;
    }

    // delay 10 min, run every 1 hour
    @Scheduled(initialDelay = 1000 * 60 * 10, fixedDelay = 1000 * 60 * 60)
    public void calculateStockPrices() {
        logger.info("calculateStockPrices task begin");

        // todo calculate prices

        miscellaneousDataService.updateLastWarehouseStockUpdateTime();
        logger.info("calculateStockPrices task end");
    }
}
