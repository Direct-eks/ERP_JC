package org.jc.backend.service;

import org.jc.backend.entity.SupplierO;
import org.jc.backend.entity.SupplierResourceO;
import org.jc.backend.entity.VO.ListUpdateVO;
import org.jc.backend.entity.VO.SupplierWithResourcesVO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface SupplierService {
    List<SupplierO> getAllSuppliers();

    List<SupplierResourceO> getResourcesBySupplier(int id);
    void updateSupplierWithResources(SupplierO supplierO, ListUpdateVO<SupplierResourceO> updateVO);

    List<SupplierResourceO> getSupplierResourcesBySku(int id);
    List<SupplierResourceO> resourcesByCategoryAndFactoryBrand(int categoryID, int brandID);

    void deleteResourcesBySupplierID(int id);
}
