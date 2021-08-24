package org.jc.backend.entity;

import java.util.List;

public interface ObjectComparison<T> {
    T getOldObject(List<T> oldObjectList);
    boolean formModificationRecord(final T oldObj, final StringBuilder record);
}
