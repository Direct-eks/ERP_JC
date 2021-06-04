package org.jc.backend.service;

import org.jc.backend.entity.VO.ListUpdateVO;
import org.jc.backend.entity.WarehouseO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface WarehouseService {
    List<WarehouseO> getWarehouseOptions();

    void updateWarehouses(ListUpdateVO<WarehouseO> updateVO);
}
