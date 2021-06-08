package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.entity.SupplierO;
import org.jc.backend.entity.SupplierResourceO;
import org.jc.backend.entity.VO.SupplierWithResourcesVO;
import org.jc.backend.service.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Indexed
@Api(tags = "Supplier Related")
@RestController
@RequestMapping("/supplier")
public class SupplierController {

    private static final Logger logger = LoggerFactory.getLogger(SupplierController.class);

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = SupplierO.class)
    @GetMapping("/getAllSuppliers")
    public List<SupplierO> getAllSuppliers() {
        logger.info("GET Request to /supplier/getAllSuppliers");

        return supplierService.getAllSuppliers();
    }

    @ApiOperation(value = "", response = SupplierResourceO.class)
    @GetMapping("/getResourcesBySupplier/{id}")
    public List<SupplierResourceO> getResourcesBySupplier(@PathVariable("id") int id) {
        logger.info("GET Request to /supplier/getResourcesBySupplier, id: {}", id);

        return supplierService.getResourcesBySupplier(id);
    }

    @ApiOperation(value = "", response = void.class)
    @PostMapping("/createSupplierWithResources")
    public void updateSupplierWithResources(@Validated @RequestBody SupplierWithResourcesVO vo) {
        logger.info("POST Request to /supplier/createSupplierWithResources, data: {}", vo.toString());

        supplierService.updateSupplierWithResources(vo);
    }

    @ApiOperation(value = "", response = SupplierResourceO.class)
    @GetMapping("/getSupplierResourcesBySku/{id}")
    public List<SupplierResourceO> getSupplierResourcesBySku(@PathVariable("id") int id) {
        logger.info("GET Request to /supplier/getSupplierResourcesBySku, id: {}", id);

        return supplierService.getSupplierResourcesBySku(id);
    }

    @ApiOperation(value = "", response = void.class)
    @DeleteMapping("/deleteResourcesBySupplierID/{id}")
    public void deleteResourcesBySupplierID(@PathVariable("id") int id) {
        logger.info("DELETE Request to /supplier/deleteResourcesBySupplierID, id: {}", id);

        supplierService.deleteResourcesBySupplierID(id);
    }
}
