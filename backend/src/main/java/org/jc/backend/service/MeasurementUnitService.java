package org.jc.backend.service;

import org.jc.backend.entity.MeasurementUnitO;
import org.jc.backend.entity.VO.ListUpdateVO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface MeasurementUnitService {
    List<MeasurementUnitO> getAllUnits();

    void updateUnits(ListUpdateVO<MeasurementUnitO> updateVO);
}
