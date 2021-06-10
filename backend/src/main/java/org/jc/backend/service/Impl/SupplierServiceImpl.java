package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.SupplierMapper;
import org.jc.backend.entity.SupplierO;
import org.jc.backend.entity.SupplierResourceO;
import org.jc.backend.entity.VO.ListUpdateVO;
import org.jc.backend.service.SupplierService;
import org.jc.backend.utils.IOModificationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Transactional(readOnly = true)
    @Override
    public List<SupplierResourceO> resourcesByCategoryAndFactoryBrand(int categoryID, int brandID) {
        try {
            List<SupplierResourceO> rawResources = supplierMapper.queryResourcesByCategoryAndBrand(categoryID, brandID);
            List<SupplierResourceO> resources = new ArrayList<>();
            rawResources.forEach(r -> {
                var resource = new SupplierResourceO();
                if (r.getSupplierID() != null) {
                    BeanUtils.copyProperties(r, resource);
                }
                else {
                    resource.setSkuID(r.getSkuID());
                    resource.setCode(r.getCode());
                    resource.setFactoryCode(r.getFactoryCode());
                }
                resources.add(resource);
            });
            return resources;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateSupplierWithResources(SupplierO supplierO, ListUpdateVO<SupplierResourceO> updateVO) {
        try {
            List<SupplierResourceO> newResources = new ArrayList<>(updateVO.getElements());
            List<SupplierResourceO> oldResources = supplierMapper.queryResourcesBySupplier(supplierO.getSupplierID());
            if (oldResources.isEmpty()) { // new supplier
                supplierMapper.createSupplier(supplierO);
            }

            // check for new added resources
            newResources.removeIf(r -> oldResources.stream()
                    .anyMatch(or -> or.getSkuID().equals(r.getSkuID())));
            for (var resource : newResources) {
                resource.setSupplierID(supplierO.getSupplierID());
                resource.setHistory("无历史报价记录");
                supplierMapper.insertResource(resource);
            }

            List<SupplierResourceO> updatedResources = new ArrayList<>(updateVO.getElements());
            updatedResources.removeAll(newResources);
            for (var resource : updatedResources) {
                String history = null;
                for (var oldResource : oldResources) {
                    if (oldResource.getSkuID().equals(resource.getSkuID())) {
                        history = IOModificationUtils.formResourceHistory(oldResource);
                        break;
                    }
                }
                resource.setHistory(history);
                supplierMapper.updateResource(resource);
            }

            // todo remove old resources

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

    @Transactional
    @Override
    public void deleteResourcesBySupplierID(int id) {
        try {
            supplierMapper.deleteResourceBySupplierID(id);
            supplierMapper.deleteSupplierByID(id);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("delete failed");
            throw e;
        }
    }
}
