package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.DepartmentMapper;
import org.jc.backend.entity.DepartmentO;
import org.jc.backend.entity.VO.ListUpdateVO;
import org.jc.backend.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private final DepartmentMapper departmentMapper;

    public DepartmentServiceImpl(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional(readOnly = true)
    public List<DepartmentO> getDepartmentOptions() {
        try {
            return departmentMapper.queryDepartments();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void updateDepartments(ListUpdateVO<DepartmentO> updateVO) {
        try {
            List<DepartmentO> tempDepartments = new ArrayList<>(updateVO.getElements());

            tempDepartments.removeIf(d -> d.getDepartmentID() >= 0);
            for (var department : tempDepartments) {
                departmentMapper.insertDepartment(department);
            }

            tempDepartments = new ArrayList<>(updateVO.getElements());
            tempDepartments.removeIf(d -> d.getDepartmentID() < 0);
            for (var department : tempDepartments) {
                departmentMapper.updateDepartment(department);
            }

            // todo remove
            List<DepartmentO> oldDepartments = departmentMapper.queryDepartments();

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }
}
