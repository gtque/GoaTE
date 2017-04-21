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
import org.atteo.classindex.ClassIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Defines an employee object. An employee does some type of work by calling the work method.
 * Created by gtque on 4/21/2017.
 */
public abstract class Employee {
    protected final Logger LOG = LoggerFactory.getLogger(getClass());
    protected HealthRecord hr = new HealthRecord();
    protected Goate data;

    static Map<String, Class> employeeDirectory = null;

    public Employee() {
        new AnnotationEvaluator().process(this, getClass());
    }

    public HealthRecord getHrReport(){
        return hr;
    }

    public Employee setData(Goate data){
        this.data = data;
        return this;
    }

    public Object work() {
        Object result = null;
        try {
            result = doWork();
        } catch (Throwable t) {
            try {
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(bs, true, "utf-8");
                t.printStackTrace(ps);
                String stack = new String(bs.toByteArray(), UTF_8);
                hr.report(t.getMessage(), stack);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            throw t;
        }
        return result;
    }

    public abstract Employee init();

    public abstract Object doWork();

    public Employee recruit(Class job, Goate data){
        GoateJob theJob = (GoateJob)job.getAnnotation(GoateJob.class);
        Employee employee = null;
        if(theJob!=null){
            employee = recruit(theJob.jobs()[0], data);
        }
        return employee;
    }
    public Employee recruit(String job, Goate data){
        Employee employee = null;
        Class recruit = findEmployee(job);
        if(recruit!=null){
            try {
                employee = (Employee)recruit.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                LOG.error("Problem recruiting the employee: " + e.getMessage(), e);
            }
        }
        if(employee!=null){
            employee.setData(data).init();
        }
        return employee;
    }

    protected Class findEmployee(String job){
        if(employeeDirectory==null||!employeeDirectory.containsKey(job)){
            buildDirectory();
        }
        return employeeDirectory.get(job);
    }

    protected void buildDirectory(){
        if(employeeDirectory==null){
            employeeDirectory = new ConcurrentHashMap<>();
        }
        for (Class<?> word : ClassIndex.getAnnotated(GoateJob.class)) {
            try {
                LOG.debug("indexing word: " + word.getCanonicalName());
                Class klass = Class.forName(word.getCanonicalName());
                GoateJob dsl = (GoateJob) klass.getAnnotation(GoateJob.class);
                if (dsl != null) {
                    for(String job:dsl.jobs()) {
                        if (employeeDirectory.containsKey(job)) {
                            LOG.warn("The job: " + job + " already exists, the previous employee will be replaced with: " + klass.getName());
                        }
                        employeeDirectory.put(job, klass);
                    }
                }
            } catch (ClassNotFoundException e) {
                LOG.error("There was a problem finding an employee for the job: " + e.getMessage(), e);
            }
        }
    }
}
