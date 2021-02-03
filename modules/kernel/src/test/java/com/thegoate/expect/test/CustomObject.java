package com.thegoate.expect.test;

import com.thegoate.utils.fill.serialize.Kid;

public class CustomObject extends Kid {
    private String name;
    private boolean configured = false;

    public String getName() {
        return name;
    }

    public CustomObject setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isConfigured() {
        return configured;
    }

    public CustomObject setConfigured(boolean configured) {
        this.configured = configured;
        return this;
    }
}
