package com.thegoate.barn.test;

import com.thegoate.staff.Employee;
import com.thegoate.staff.GoateJob;

/**
 * Used for testing barn, integration, step executor and overrides.<br>
 * Simply adds two values together. The values should be called val1 and val2
 * Created by Eric Angeli on 6/29/2017.
 */
@GoateJob(jobs = {"simple add"})
public class SimpleAdderEmployee extends Employee {

    int val1 = 0;
    int val2 = 0;

    @Override
    public String[] detailedScrub() {
        String[] scrub = {"val2"};
        return scrub;
    }

    @Override
    public Employee init() {
        val1 = Integer.parseInt(""+definition.get("val1", "-1"));
        val2 = Integer.parseInt(""+definition.get("val2", "-41"));
        return this;
    }

    @Override
    protected Object doWork() {
        return val1+val2;
    }
}
