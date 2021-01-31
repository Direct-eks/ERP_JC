package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.SupplierO;
import org.jc.backend.entity.SupplierResourceO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SupplierMapper {
    List<SupplierO> queryAllSuppliers();
    List<SupplierResourceO> queryResourcesBySupplier(int id);

    void createSupplier();
    void insertResource();
    void deleteResource();
}
