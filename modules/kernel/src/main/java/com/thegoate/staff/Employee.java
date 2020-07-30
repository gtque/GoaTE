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
import com.thegoate.utils.togoate.ToGoate;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Defines an employee object. An employee does some type of work by calling the work method.<br>
 * In order to make full use of an employee with other GoaTE frameworks, they should be annotated
 * with {@literal @}GoateJob to provide a list of jobs the employee fills.
 * Use param(key) or param(key,default) to get the parameters for the job or directly access definition.
 * When manipulating or setting the test run data use the class variable data.
 * Created by gtque on 4/21/2017.
 */
public abstract class Employee<T> implements Worker<Employee, T> {
    protected final BleatBox LOG = BleatFactory.getLogger(getClass());
    protected final static BleatBox slog = BleatFactory.getLogger(Employee.class);
    protected HealthRecord hr = new HealthRecord();
    protected Goate data;
    protected Goate definition = new Goate();
    protected String name = "";
    protected volatile long startTime = 0L;
    protected volatile T result = null;
    protected long period = 50L;
    protected boolean periodHasBeenSet = false;

    public HealthRecord getHrReport() {
        return hr;
    }

    public Employee<T> setName(String name){
        this.name = name;
        return this;
    }

    public String parameterName(String parameter){
        return new StringBuilder(name).append(name.isEmpty()?"":".").append(parameter).toString();
    }

    public String getName(){
        return name;
    }

    public String getNameDef(){
        return name + ".definition";
    }

    public Employee<T> initData(){
        if(data==null) {
            data = new Goate();
        }
        return this;
    }

    protected Object param(String paramName) {
        return param(paramName, null);
    }

    protected Object param(String paramName, Object def){
        return definition!=null?definition.get(paramName,def):def;
    }

    public Employee<T> setData(Goate data) {
        this.data = data;
        //no point in processing the annotations until the data is set.
        new AnnotationEvaluator().process(this, getClass());//stubbed for now, but intended for processing of annotations for auto wire like functionality.
        return this;
    }

    public Employee<T> defaultPeriod(long period){
        if(!periodHasBeenSet){
            this.period = period;
        }
        return this;
    }

    public Employee<T> period(long period){
        this.period = period;
        periodHasBeenSet = true;
        return this;
    }

    public synchronized final T syncWork(){
        return syncWork(period);
    }

    public synchronized final T syncWork(long waitMs){
        if(System.currentTimeMillis()-startTime>waitMs){
            result = work();
            startTime = System.currentTimeMillis();
        }
        return result;
    }

    /**
     * Always called immediately before doWork().<br>
     * Override this method to define any common setup/configuration/initialization that needs
     * to be done just prior to working after the definition and other parameters have been set.
     * @return syntactic sugar, returns itself.
     */
    public Employee<T> clockIn(){
        return this;
    }

    public final T work() {
        result = null;
        String lap = ""+System.nanoTime();
        try {
            if(data!=null&&data.get("lap",null)!=null) {
                String lt = data.get("lap", lap, String.class);
                if(!lt.isEmpty()){
                    lap = lt;
                }
                Stopwatch.global.start(lap);
            }
            result = (T)clockIn().doWork();
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
                Stopwatch.global.split(lap);
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

    public final Employee<T> build(){
        return init();
    }

    public Employee<T> init(Goate data) {
        setData(data);
        if(data!=null) {
            Object def = data.get(getName() + ".definition");
            def = def != null ? def : data;
            definition = new ToGoate(def).convert();
            definition.merge(scrub(data), false);
        }
        return init();
    }

    public Employee<T> mergeData(Goate data){
        definition.merge(scrub(data),false);
        return this;
    }

    protected abstract Employee<T> init();

    protected abstract T doWork();

    public static Employee recruit(Class job, Goate data) {
        GoateJob theJob = (GoateJob) job.getAnnotation(GoateJob.class);
        Employee employee = null;
        if (theJob != null) {
            employee = recruit(theJob.jobs()[0], data);
        }
        return employee;
    }

    public static Employee<Object> recruit(String job, Goate data) {
        return recruit(job, data, null, Object.class);
    }

    public static <T> Employee<T> recruit(String job, Goate data, Class<T> type) {
        return recruit(job, data, null, type);
    }

    public static Employee<Object> recruit(String job, Goate definition, Goate parentData) {
        return recruit (job, definition, parentData, Object.class);
    }
    public static <T> Employee<T> recruit(String job, Goate definition, Goate parentData, Class<T> type){
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
            if(definition!=null) {
                definition.merge(employee.scrub(parentData), false);
            }
            employee.init(definition);
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
