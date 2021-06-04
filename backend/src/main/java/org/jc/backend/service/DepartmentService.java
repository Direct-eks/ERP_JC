package org.jc.backend.service;

import org.jc.backend.entity.DepartmentO;
import org.jc.backend.entity.VO.ListUpdateVO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface DepartmentService {
    List<DepartmentO> getDepartmentOptions();

    void updateDepartments(ListUpdateVO<DepartmentO> updateVO);
}
