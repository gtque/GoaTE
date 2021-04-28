package com.thegoate.utils.fill.serialize;

import java.util.List;

public interface TypeT {

    public void setGoateType(List<Class> type);

    /**
     * Returnes the generic T class type.
     * @return
     * @param index
     */
    public Class goateType(int index);
}
