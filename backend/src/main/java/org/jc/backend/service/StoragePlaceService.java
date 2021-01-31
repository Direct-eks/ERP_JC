package org.jc.backend.service;

import org.jc.backend.entity.StoragePlaceO;

import java.util.List;

public interface StoragePlaceService {
    List<StoragePlaceO> getAllPlaces();
    void addNewPlace(StoragePlaceO storagePlaceO);
    void changePlace(StoragePlaceO storagePlaceO);
}
