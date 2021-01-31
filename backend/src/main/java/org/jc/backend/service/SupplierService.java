package org.jc.backend.service;

import org.jc.backend.entity.SupplierO;
import org.jc.backend.entity.SupplierResourceO;
import org.jc.backend.entity.VO.SupplierWithResourcesVO;

import java.util.List;

public interface SupplierService {
    List<SupplierO> getAllSuppliers();
    List<SupplierResourceO> getResourcesBySupplier(int id);
    void updateSupplierWithResources(SupplierWithResourcesVO vo);
}
