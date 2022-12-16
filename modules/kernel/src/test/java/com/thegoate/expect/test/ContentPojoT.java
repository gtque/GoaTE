package com.thegoate.expect.test;

import com.thegoate.utils.fill.serialize.Kid;
import com.thegoate.utils.fill.serialize.TypeT;
import com.thegoate.utils.fill.serialize.collections.ListType;

import java.util.List;

public class ContentPojoT<T> extends Kid {

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    @ListType(type = TypeT.class)
    private List<T> content;
}
