package org.jc.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jc.backend.entity.DepartmentO;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Indexed
@Mapper
@Repository
public interface DepartmentMapper {
    List<DepartmentO> queryDepartments();

    void insertDepartment(DepartmentO departmentO);
    void updateDepartment(DepartmentO departmentO);
    void deleteDepartment(int id);
}
