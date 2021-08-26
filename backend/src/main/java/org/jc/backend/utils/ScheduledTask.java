package org.jc.backend.utils;

import org.jc.backend.config.exception.GlobalParamException;
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
    @Scheduled(initialDelay = 1000 * 60 * 60, fixedDelay = 1000 * 60 * 60)
    public void autoDatabaseBackup() {
        logger.info("database auto backup task start");

        if (!miscellaneousDataService.isAutoBackupEnabled()) {
            logger.info("database auto backup disabled, task ended");
            return;
        }

        try {
            miscellaneousDataService.backupDatabase();
        } catch (GlobalParamException e) {
            return;
        }
        logger.info("database auto backup task finished");
    }
}
