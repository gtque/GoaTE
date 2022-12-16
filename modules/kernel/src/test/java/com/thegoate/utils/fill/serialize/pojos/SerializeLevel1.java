package com.thegoate.utils.fill.serialize.pojos;

import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.Kid;

public class SerializeLevel1 extends Kid {

    @GoateSource(source = SimpleSource.class, key = "guardian")
    private String firstLevel;

    public String getFirstLevel() {
        return firstLevel;
    }

    public SerializeLevel1 setFirstLevel(String firstLevel) {
        this.firstLevel = firstLevel;
        return this;
    }
}
