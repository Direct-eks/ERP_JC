package org.jc.backend.service;

import org.jc.backend.entity.MeasurementUnitO;
import org.jc.backend.entity.VO.ListUpdateVO;

import java.util.List;

public interface MeasurementUnitService {
    List<MeasurementUnitO> getAllUnits();

    void updateUnits(ListUpdateVO<MeasurementUnitO> updateVO);
}
