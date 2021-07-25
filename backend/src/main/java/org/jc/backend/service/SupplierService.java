package org.jc.backend.service;

import org.jc.backend.entity.SupplierO;
import org.jc.backend.entity.SupplierResourceO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface SupplierService {
    List<SupplierO> getAllSuppliers();
    SupplierO getSupplier(int id);

    List<SupplierResourceO> getResourcesBySupplier(int id);
    void updateSupplierWithResources(SupplierO supplierO, List<SupplierResourceO> updateVO);

    List<SupplierResourceO> getSupplierResourcesBySku(int id);
    List<SupplierResourceO> resourcesByCategoryAndFactoryBrand(int categoryID, int brandID, int supplierID);

    void deleteResourcesBySupplierID(int id);
}
