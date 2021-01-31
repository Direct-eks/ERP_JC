package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.SupplierMapper;
import org.jc.backend.entity.SupplierO;
import org.jc.backend.entity.SupplierResourceO;
import org.jc.backend.entity.VO.SupplierWithResourcesVO;
import org.jc.backend.service.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SupplierServiceImpl implements SupplierService {

    private static final Logger logger = LoggerFactory.getLogger(SupplierServiceImpl.class);

    private final SupplierMapper supplierMapper;

    public SupplierServiceImpl(SupplierMapper supplierMapper) {
        this.supplierMapper = supplierMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional(readOnly = true)
    @Override
    public List<SupplierO> getAllSuppliers() {
        try {
            return supplierMapper.queryAllSuppliers();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<SupplierResourceO> getResourcesBySupplier(int id) {
        try {
            return supplierMapper.queryResourcesBySupplier(id);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateSupplierWithResources(SupplierWithResourcesVO vo) {

        SupplierO supplier = new SupplierO();
        BeanUtils.copyProperties(vo, supplier);
        List<SupplierResourceO> newResources = vo.getResources();

        try {
            List<SupplierResourceO> oldResources = supplierMapper.queryResourcesBySupplier(supplier.getSupplierID());
            if (oldResources.isEmpty()) { // new supplier
                //todo create new supplier
            }

            Map<Integer, SupplierResourceO> oldResourcesMap = new HashMap<>();
            oldResources.forEach(r -> oldResourcesMap.put(r.getSkuID(), r));

            // compare resources for new added resources
            for (var r : newResources) {
                SupplierResourceO tempResource = oldResourcesMap.get(r.getSkuID());
                if (tempResource != null) { // compare & check changes
                    //todo compare
                }
                else { // new resource, insert
                    //todo insert new
                }
            }

            // compare resources for deleted resources
            oldResources.removeAll(newResources); // remove overlapping resource, left with deleted ones
            oldResources.forEach(r -> supplierMapper.deleteResource()); //todo complete param and mapper

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<SupplierResourceO> getSupplierResourcesBySku(int id) {
        try {
            return supplierMapper.queryResourceBySku(id);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }
}
