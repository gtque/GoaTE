package com.thegoate.expect.test;

import com.thegoate.utils.fill.serialize.GoatePojo;
import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.Kid;

public class TypePojo extends Kid {
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @   GoateSource(source = UDFSource.class, key="udfFieldType.code")
    private String type = null;
}
