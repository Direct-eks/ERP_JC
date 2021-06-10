package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.SupplierO;
import org.jc.backend.entity.SupplierResourceO;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface SupplierMapper {
    List<SupplierO> queryAllSuppliers();
    void createSupplier(SupplierO supplierO);
    void deleteSupplierByID(int id);

    List<SupplierResourceO> queryResourcesBySupplier(int id);
    List<SupplierResourceO> queryResourcesByCategoryAndBrand(int categoryID, int brandID);
    List<SupplierResourceO> queryResourceBySku(int id);

    void insertResource(SupplierResourceO resourceO);
    void updateResource(SupplierResourceO resourceO);
    void deleteResourceBySupplierID(int id);
}
