/*
 * Copyright (c) 2017. Eric Angeli
 *
 *  Permission is hereby granted, free of charge,
 *  to any person obtaining a copy of this software
 *  and associated documentation files (the "Software"),
 *  to deal in the Software without restriction,
 *  including without limitation the rights to use, copy,
 *  modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit
 *  persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission
 *  notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 *  AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 */
package com.thegoate.barn.staff;

import com.thegoate.annotations.AnnotationFactory;
import com.thegoate.rest.staff.ApiEmployee;
import com.thegoate.staff.Employee;
import com.thegoate.staff.GoateJob;

import java.lang.reflect.InvocationTargetException;

/**
 * Does an api call.
 * Created by Eric Angeli on 5/22/2017.
 */
@GoateJob(jobs = {"step"})
public class StepEmployee extends Employee {

    ApiEmployee worker;

    @Override
    public Employee init() {
        String method = "" + data.get("method", "get");//default to get if not specified.
        AnnotationFactory af = new AnnotationFactory();
        try {
            worker = (ApiEmployee) af.annotatedWith(GoateJob.class).find(method).using("jobs").build();
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            LOG.error("Step Init", "problem finding something to execute a " + method +"\nmake sure you have an implementation library included.", e);
        }
        return this;
    }

    @Override
    protected Object doWork() {
        Object result = null;
        if(worker!=null){
            result = worker.work();
        }
        return result;
    }

    @Override
    public String[] detailedScrub(){
        String[] scrub = {};
        return scrub;
    }
}
