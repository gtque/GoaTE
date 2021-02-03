package com.thegoate.expect.test;

import com.thegoate.utils.fill.serialize.Kid;

import java.util.Map;

public class NestedObject extends Kid {
    private String no = "";
    private Map<String, Object> map = null;

    public String getNo() {
        return no;
    }

    public NestedObject setNo(String no) {
        this.no = no;
        return this;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public NestedObject setMap(Map<String, Object> map) {
        this.map = map;
        return this;
    }
}
