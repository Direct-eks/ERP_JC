package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.entity.StoragePlaceO;
import org.jc.backend.service.StoragePlaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "StoragePlace Related")
@RestController
@RequestMapping("/storagePlace")
public class StoragePlaceController {

    private static final Logger logger = LoggerFactory.getLogger(StoragePlaceController.class);

    private final StoragePlaceService storagePlaceService;

    public StoragePlaceController(StoragePlaceService storagePlaceService) {
        this.storagePlaceService = storagePlaceService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = StoragePlaceO.class)
    @GetMapping("/getAllPlaces")
    public List<StoragePlaceO> getAllPlaces() {
        logger.info("GET Request to /storagePlace/getAllPlaces");

        return storagePlaceService.getAllPlaces();
    }

    @ApiOperation(value = "", response = void.class)
    @PutMapping("/addNewPlace")
    public void addNewPlace(@Validated @RequestBody StoragePlaceO placeO) {
        logger.info("GET Request to /storagePlace/getAllPlaces, data: {}", placeO.toString());

        storagePlaceService.addNewPlace(placeO);
    }

    @ApiOperation(value = "", response = void.class)
    @PatchMapping("/changePlace")
    public void changePlace(@Validated @RequestBody StoragePlaceO placeO) {
        logger.info("GET Request to /storagePlace/getAllPlaces, data: {}", placeO.toString());

        storagePlaceService.changePlace(placeO);
    }
}
