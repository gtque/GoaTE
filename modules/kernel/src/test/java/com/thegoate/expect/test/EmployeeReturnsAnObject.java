package com.thegoate.expect.test;

import com.thegoate.staff.Employee;

public class EmployeeReturnsAnObject extends Employee<CustomObject> {

    String name;
    boolean configured = false;
    public EmployeeReturnsAnObject name(String name){
        this.name = name;
        return this;
    }

    public EmployeeReturnsAnObject configured(boolean configured){
        this.configured = configured;
        return this;
    }

    @Override
    public String[] detailedScrub() {
        return new String[0];
    }

    @Override
    protected Employee init() {
        return null;
    }

    @Override
    protected CustomObject doWork() {
        return new CustomObject().setName(name).setConfigured(configured);
    }
}
