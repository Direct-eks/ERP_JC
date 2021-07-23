package org.jc.backend.utils;

import org.jc.backend.service.MiscellaneousDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Indexed;

import java.io.IOException;

@Indexed
@Component
public class ScheduledTask {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    private final MiscellaneousDataService miscellaneousDataService;

    public ScheduledTask(MiscellaneousDataService miscellaneousDataService) {
        this.miscellaneousDataService = miscellaneousDataService;
    }

    // delay 1 hour, run every 1 hour
    @Scheduled(initialDelay = 1000 * 10 * 60, fixedDelay = 1000 * 60 * 60)
    public void calculateStockPrices() {
        logger.info("database backup task begin");

        try {
            MyUtils.databaseBackup();
        } catch (IOException e) {
            return;
        }

        miscellaneousDataService.updateLastBackupTime();
        logger.info("database backup task end");
    }
}
