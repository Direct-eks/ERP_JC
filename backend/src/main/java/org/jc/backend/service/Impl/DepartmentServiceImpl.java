package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jc.backend.dao.DepartmentMapper;
import org.jc.backend.entity.DepartmentO;
import org.jc.backend.service.DepartmentService;
import org.jc.backend.service.ModificationRecordService;
import org.jc.backend.service.UsageCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.jc.backend.service.ModificationRecordService.DELETION_MSG;
import static org.jc.backend.service.ModificationRecordService.DEPARTMENT;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private final DepartmentMapper departmentMapper;
    private final ModificationRecordService modificationRecordService;
    private final UsageCheckService usageCheckService;

    public DepartmentServiceImpl(
            DepartmentMapper departmentMapper,
            ModificationRecordService modificationRecordService,
            UsageCheckService usageCheckService
    ) {
        this.departmentMapper = departmentMapper;
        this.modificationRecordService = modificationRecordService;
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
        Subject subject = SecurityUtils.getSubject();
        String usernameString = "修改者：" + subject.getPrincipals().getPrimaryPrincipal() + "; ";

        try {
            List<DepartmentO> tempDepartments = new ArrayList<>(updateVO);
            List<DepartmentO> oldDepartments = departmentMapper.queryDepartments();

            tempDepartments.removeIf(d -> d.getDepartmentID() >= 0);
            for (var department : tempDepartments) {
                departmentMapper.insertDepartment(department);
                logger.info("Inserted new department, id: {}", department.getDepartmentID());
            }

            tempDepartments = new ArrayList<>(updateVO);
            tempDepartments.removeIf(d -> d.getDepartmentID() < 0);
            for (var department : tempDepartments) {
                StringBuilder record = new StringBuilder(usernameString);
                if (department.formModificationRecord(department.getOldObject(oldDepartments), record)) {
                    departmentMapper.updateDepartment(department);
                    logger.info("Updated department, id {}", department.getDepartmentID());
                    modificationRecordService.insertRecord(DEPARTMENT, department.getDepartmentID(), record);
                }
            }

            // check for removed
            oldDepartments.removeIf(oldD -> updateVO.stream()
                    .anyMatch(d -> d.getDepartmentID().equals(oldD.getDepartmentID())));
            for (var d : oldDepartments) {
                if (!usageCheckService.isDepartmentIDInUse(d.getDepartmentID())) {
                    departmentMapper.deleteDepartment(d.getDepartmentID());
                    logger.info("Deleted department, id: {}", d.getDepartmentID());
                    modificationRecordService.insertRecord(DEPARTMENT, d.getDepartmentID(),
                            usernameString + DELETION_MSG + d);
                }
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }
}
