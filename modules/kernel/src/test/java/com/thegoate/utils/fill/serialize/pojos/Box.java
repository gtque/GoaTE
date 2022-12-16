package com.thegoate.utils.fill.serialize.pojos;

import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.Kid;

public class Box extends Kid {

    @GoateSource(source = SimpleSource.class, key = "weight")
    private int size;

    public int getSize() {
        return size;
    }

    public Box setSize(int size) {
        this.size = size;
        return this;
    }
}
