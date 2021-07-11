package org.jc.backend.utils;

import org.jc.backend.service.InboundEntryService;
import org.jc.backend.service.MiscellaneousDataService;
import org.jc.backend.service.OutboundEntryService;
import org.jc.backend.service.WarehouseStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Indexed;

import java.io.*;
import java.nio.charset.StandardCharsets;

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

    // delay 1 hour, run every 1 hour
    @Scheduled(initialDelay = 1000 * 10 * 60, fixedDelay = 1000 * 60 * 60)
    public void calculateStockPrices() {
        logger.info("calculateStockPrices task begin");

        Runtime rt = Runtime.getRuntime();
        String cmd = "sqlite3 C:\\ERP_JC\\db\\test.db \".dump\"";

        Process process;
        try {
            process = rt.exec( cmd);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try (InputStream inputStream = process.getInputStream();
             InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(reader);
             FileOutputStream outputStream = new FileOutputStream("c:\\ERP_JC\\db\\backup.sql");
             OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {

            String line;
            while ((line = br.readLine()) != null) {
                writer.write(line + "\\r\\n");
            }
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        miscellaneousDataService.updateLastBackupTime();
        logger.info("calculateStockPrices task end");
    }
}
