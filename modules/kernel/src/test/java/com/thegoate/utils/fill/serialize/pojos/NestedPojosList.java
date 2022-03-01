package com.thegoate.utils.fill.serialize.pojos;

import com.thegoate.utils.fill.serialize.Kid;
import com.thegoate.utils.fill.serialize.TypeT;
import com.thegoate.utils.fill.serialize.collections.ListType;

import java.util.List;

public class NestedPojosList<T> extends Kid {

    @ListType(type = TypeT.class)
    private
    List<T> nestedList;

    public List<T> getNestedList() {
        return nestedList;
    }

    public NestedPojosList setNestedList(List<T> nestedList) {
        this.nestedList = nestedList;
        return this;
    }
}
