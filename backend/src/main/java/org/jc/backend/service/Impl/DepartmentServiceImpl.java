package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.DepartmentMapper;
import org.jc.backend.entity.DepartmentO;
import org.jc.backend.service.DepartmentService;
import org.jc.backend.service.UsageCheckService;
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
    private final UsageCheckService usageCheckService;

    public DepartmentServiceImpl(
            DepartmentMapper departmentMapper,
            UsageCheckService usageCheckService
    ) {
        this.departmentMapper = departmentMapper;
        this.usageCheckService = usageCheckService;
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
    public void updateDepartments(List<DepartmentO> updateVO) {
        try {
            List<DepartmentO> tempDepartments = new ArrayList<>(updateVO);

            tempDepartments.removeIf(d -> d.getDepartmentID() >= 0);
            for (var department : tempDepartments) {
                departmentMapper.insertDepartment(department);
            }

            tempDepartments = new ArrayList<>(updateVO);
            tempDepartments.removeIf(d -> d.getDepartmentID() < 0);
            for (var department : tempDepartments) {
                departmentMapper.updateDepartment(department);
            }

            // check for removed
            List<DepartmentO> oldDepartments = departmentMapper.queryDepartments();
            oldDepartments.removeIf(oldD -> updateVO.stream()
                    .anyMatch(d -> d.getDepartmentID() == oldD.getDepartmentID()));
            for (var d : oldDepartments) {
                if (!usageCheckService.isDepartmentIDInUse(d.getDepartmentID())) {
                    departmentMapper.deleteDepartment(d.getDepartmentID());
                }
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }
}
