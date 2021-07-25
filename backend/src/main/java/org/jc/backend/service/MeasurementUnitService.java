package org.jc.backend.service;

import org.jc.backend.entity.MeasurementUnitO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface MeasurementUnitService {
    List<MeasurementUnitO> getAllUnits();

    void updateUnits(List<MeasurementUnitO> updateVO);
}
