package com.thegoate.dsl.words.employee;

import com.thegoate.staff.Employee;
import com.thegoate.staff.GoateJob;

@GoateJob(jobs = {"return 42"})
public class SimpleEmployee extends Employee<Integer> {
    @Override
    public String[] detailedScrub() {
        return new String[0];
    }

    @Override
    protected Employee<Integer> init() {
        return this;
    }

    @Override
    protected Integer doWork() {
        return 42;
    }
}
