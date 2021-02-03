package com.thegoate.expect.test;

import com.thegoate.utils.fill.serialize.Kid;
import com.thegoate.utils.fill.serialize.collections.ListType;

import java.util.List;

public class ContentPojo extends Kid {

    public List<TypePojo> getContent() {
        return content;
    }

    public void setContent(List<TypePojo> content) {
        this.content = content;
    }

    @ListType(type = TypePojo.class)
    private List<TypePojo> content;
}
