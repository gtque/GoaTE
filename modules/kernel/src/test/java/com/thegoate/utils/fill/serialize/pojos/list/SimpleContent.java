package com.thegoate.utils.fill.serialize.pojos.list;

import com.thegoate.utils.fill.serialize.Kid;

public class SimpleContent extends Kid {
    private String name;
    private int count;

    public String getName() {
        return name;
    }

    public SimpleContent setName(String name) {
        this.name = name;
        return this;
    }

    public int getCount() {
        return count;
    }

    public SimpleContent setCount(int count) {
        this.count = count;
        return this;
    }
}
