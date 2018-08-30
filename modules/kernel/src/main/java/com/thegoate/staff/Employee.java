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

package com.thegoate.staff;

import com.thegoate.Goate;
import com.thegoate.annotations.AnnotationEvaluator;
import com.thegoate.annotations.AnnotationFactory;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.metrics.Stopwatch;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Defines an employee object. An employee does some type of work by calling the work method.<br>
 * In order to make full use of an employee with other GoaTE frameworks, they should be annotated
 * with {@literal @}GoateJob to provide a list of jobs the employee fills.
 * Created by gtque on 4/21/2017.
 */
public abstract class Employee {
    protected final BleatBox LOG = BleatFactory.getLogger(getClass());
    protected final static BleatBox slog = BleatFactory.getLogger(Employee.class);
    protected HealthRecord hr = new HealthRecord();
    protected Goate data;
    protected String name = "";

    public HealthRecord getHrReport() {
        return hr;
    }

    protected Employee setName(String name){
        this.name = name;
        return this;
    }

    protected String parameterName(String parameter){
        return new StringBuilder(name).append(name.isEmpty()?"":".").append(parameter).toString();
    }

    protected String getName(){
        return name;
    }

    protected String getNameDef(){
        return name + ".definition";
    }

    public Employee initData(){
        if(data==null) {
            data = new Goate();
        }
        return this;
    }

    public Employee setData(Goate data) {
        this.data = data;
        //no point in processing the annotations until the data is set.
        new AnnotationEvaluator().process(this, getClass());//stubbed for now, but intended for processing of annotations for auto wire like functionality.
        return this;
    }

    public Object work() {
        Object result = null;
        try {
            if(data!=null&&data.get("lap",null)!=null) {
                Stopwatch.global.start(data.get("lap", Thread.currentThread().getName(), String.class));
            }
            result = doWork();
        } catch (Throwable t) {
            try {
                //Creates a report in HR. Most reports are going to be exceptions.
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(bs, true, "utf-8");
                t.printStackTrace(ps);
                String stack = new String(bs.toByteArray(), UTF_8);
                hr.report(t.getMessage(), stack);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            throw t;
        }finally {
            if(data!=null&&data.get("lap",null)!=null) {
                Stopwatch.global.split(data.get("lap", Thread.currentThread().getName(), String.class));
            }
        }
        return result;
    }

    public Goate scrub(Goate data){
        String[] baseScrub = {"Scenario", "job", "abstract", "extends", "groups", "expect", "override", getName()+"\\.definition"};
        Goate scrubbed = clean(new Goate().merge(data,false),baseScrub);
        return clean(scrubbed, detailedScrub());
    }

    public Goate clean(Goate data, String[] scrub){
        if(scrub!=null) {
            for (String key : scrub) {
                data.scrub(key);
            }
        }
        return data;
    }

    public abstract String[] detailedScrub();

    public Employee init(Goate data) {
        setData(data);
        return init();
    }

    public abstract Employee init();

    protected abstract Object doWork();

    public static Employee recruit(Class job, Goate data) {
        GoateJob theJob = (GoateJob) job.getAnnotation(GoateJob.class);
        Employee employee = null;
        if (theJob != null) {
            employee = recruit(theJob.jobs()[0], data);
        }
        return employee;
    }

    public static Employee recruit(String job, Goate data) {
        Employee employee = null;
        String id = "";
        if(job.contains("#")){
            id = job.substring(job.indexOf("#")+1);
            job = job.substring(0, job.indexOf("#"));
        }
        Class recruit = findEmployee(job);
        if (recruit != null) {
            try {
                employee = (Employee) recruit.newInstance();
                employee.setName(job + (id.isEmpty()?"":("#"+id)));
            } catch (InstantiationException | IllegalAccessException e) {
                slog.error("Problem recruiting the employee: " + e.getMessage(), e);
            }
        }
        if (employee != null) {
            employee.init(data);
        }
        return employee;
    }

    protected static Class findEmployee(String job) {
        Class klass = null;
        try {
            klass = new AnnotationFactory().find(job).using(GoateJob.class.getMethod("jobs")).annotatedWith(GoateJob.class).lookUp();
        } catch (NoSuchMethodException e) {
            slog.error("problem building directory of jobs: " + e.getMessage(), e);
        }
        return klass;
    }
}
