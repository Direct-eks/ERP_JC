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
    List<SupplierResourceO> queryResourcesBySupplier(int id);

    void createSupplier();
    void deleteSupplierByID(int id);

    List<SupplierResourceO> queryResourceBySku(int id);

    void insertResource();
    void deleteResourceBySupplierID(int id);
}
