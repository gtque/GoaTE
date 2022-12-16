package com.thegoate.expect.test;

import com.thegoate.utils.fill.serialize.Kid;

import java.util.ArrayList;
import java.util.List;

public class ListPojo extends Kid {

    private List<String> theList = new ArrayList<>();

    private List<NestedObject> theInsideList = new ArrayList<>();

    public List<String> getTheList() {
        return theList;
    }

    public ListPojo setTheList(List<String> theList) {
        this.theList = theList;
        return this;
    }

    public List<NestedObject> getTheInsideList() {
        return theInsideList;
    }

    public void setTheInsideList(List<NestedObject> theInsideList) {
        this.theInsideList = theInsideList;
    }
}
