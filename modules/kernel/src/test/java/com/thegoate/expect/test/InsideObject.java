package com.thegoate.expect.test;

import com.thegoate.utils.fill.serialize.Kid;

import java.util.ArrayList;
import java.util.List;

public class InsideObject extends Kid {

    private List<String> theList = new ArrayList<>();

    public List<String> getTheList() {
        return theList;
    }

    public InsideObject setTheList(List<String> theList) {
        this.theList = theList;
        return this;
    }
}
