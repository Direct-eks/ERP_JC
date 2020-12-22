package org.jc.backend.service.Impl;

import org.jc.backend.dao.DepartmentMapper;
import org.jc.backend.entity.DepartmentO;
import org.jc.backend.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private final DepartmentMapper departmentMapper;

    public DepartmentServiceImpl(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    public List<DepartmentO> getDepartmentOptions() {
        return departmentMapper.queryDepartments();
    }
}
