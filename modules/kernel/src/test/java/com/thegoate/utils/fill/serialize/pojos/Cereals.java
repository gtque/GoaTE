package com.thegoate.utils.fill.serialize.pojos;

import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.Kid;

public class Cereals extends Kid {

    @GoateSource(source = SimpleSource.class, key = "name")
    @GoateSource(source = RootSource.class, key = "type")
    private String id;

    private Box box;

    public String getId() {
        return id;
    }

    public Cereals setId(String id) {
        this.id = id;
        return this;
    }

    public Box getBox() {
        return box;
    }

    public Cereals setBox(Box box) {
        this.box = box;
        return this;
    }
}
