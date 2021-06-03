package org.jc.backend.service;

import org.jc.backend.entity.StoragePlaceO;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Indexed
public interface StoragePlaceService {
    List<StoragePlaceO> getAllPlaces();
    void addNewPlace(StoragePlaceO storagePlaceO);
    void changePlace(StoragePlaceO storagePlaceO);
}
