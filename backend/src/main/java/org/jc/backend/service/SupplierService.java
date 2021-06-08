package org.jc.backend.service;

import org.jc.backend.entity.SupplierO;
import org.jc.backend.entity.SupplierResourceO;
import org.jc.backend.entity.VO.SupplierWithResourcesVO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface SupplierService {
    List<SupplierO> getAllSuppliers();
    List<SupplierResourceO> getResourcesBySupplier(int id);
    void updateSupplierWithResources(SupplierWithResourcesVO vo);
    List<SupplierResourceO> getSupplierResourcesBySku(int id);

    void deleteResourcesBySupplierID(int id);
}
