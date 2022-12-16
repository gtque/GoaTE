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

package com.thegoate.dsl.words;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.thegoate.Goate;
import com.thegoate.annotations.AnnotationFactory;
import com.thegoate.annotations.GoateDescription;
import com.thegoate.dsl.DSL;
import com.thegoate.dsl.GoateDSL;
import com.thegoate.staff.GoateTask;
import com.thegoate.staff.GoateTaskContainer;

/**
 * Executes and runs the specified task.
 * Returns the return from the task or null if the task is void.
 * Created by gtque on 4/21/2017.
 */
@GoateDSL(word = "do")
@GoateDescription(description = "Executes and runs the specified task.", parameters = {"Object: value"})
public class RunTask extends DSL {
    Goate parameters = new Goate();

    public RunTask(Object value) {
        super(value);
    }

    AnnotationFactory af = new AnnotationFactory();

    public static RunTask task(String task){
        return new RunTask("do::"+task);
    }

    public RunTask parameter(String key, Object value){
        parameters.put(key, value);
        return this;
    }

    public Object run(){
        return run(new Goate());
    }

    public Object run(Goate data){
        return evaluate(data);
    }

    @Override
    public Object evaluate(Goate data){
        parameters.merge(data, false);
        String task = "" + get(1, parameters);
        Object result = null;
        Object[] constructorParams = null;
        if(definition.size()>2){
            constructorParams = new Object[definition.size()-2];
            for(int i = 0; i<constructorParams.length;i++){
                constructorParams[i] = get(i+2,parameters);
            }
        }
        try {
            af.annotatedWith(GoateTaskContainer.class)
                    .findByMethod(makeGeneric(task))
                    .methodAnnotatedWith(GoateTask.class)
                    .constructorArgs(constructorParams);
            Object owner = findOwner();
            Method m = findTask();
            Object[] args = buildArgs(m, task, parameters);
            try {
                boolean accessible = m.canAccess(owner);//.isAccessible();
                LOG.debug("class: " + m.getDeclaringClass());
                LOG.debug("method: " + m.getName());
                LOG.debug("parameters: " + printArgs(args));
                m.setAccessible(true);
                result = m.invoke(owner, args);
                m.setAccessible(accessible);
            } catch (IllegalAccessException e) {
                LOG.error("Problem executing task: task\n" + e.getMessage(), e);
            } catch(InvocationTargetException ite){
                result = ite.getCause();
            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            LOG.error("There was a problem processing the word: " + value + "\n" + e.getMessage(), e);
        }
        return result;
    }

    protected String printArgs(Object[] args){
        String arg = "";
        for(Object o:args){
            arg += "" + o +", ";
        }
        int last = arg.lastIndexOf(", ");
        if(last<0){
            last = arg.length();
        }
        return arg.substring(0,last);
    }

    public Object findOwner() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object owner = af.build();
        return owner;
    }

    public Method findTask(){
        Method m = af.getMethod();
        return m;
    }

    public String makeGeneric(String task){
        String gen = task;
        while(gen.contains("${")){
            String val = gen.substring(gen.indexOf("${")+2,gen.indexOf("}"));
            gen = gen.replace("${"+val+"}","&&var&&");
        }
        return gen.replaceAll("&&var&&","\\${var}");
    }

    public Object[] buildArgs(Method m, String task, Goate data){
        Object[] args = null;
        if(m!=null){
            args = new Object[m.getParameterCount()];
            Map<Long, String> values = extractValues(task);
            int index = 0;
            Object[] valKeys = filteredKeys(values);
            for(Parameter p:m.getParameters()){
                Long ix = Long.parseLong(""+valKeys[index]);
                Object v = data.get(""+values.get(ix),null);
                args[index] = v;
                index++;
            }
        }
        return args;
    }

    protected Map<Long, String> extractValues(String task){
        Map<Long, String> vals = new ConcurrentHashMap<>();
        String params = task;
        while(params.contains("${")){
            Long stamp = System.nanoTime();
            String val = params.substring(params.indexOf("${")+2,params.indexOf("}"));
            params = params.replace("${"+val+"}",""+stamp);
            vals.put(stamp, val);
        }
        return vals;
    }

    protected Object[] filteredKeys(Map<Long, String> map){
        List<Long> list = new ArrayList<>();
        Set<Long> keys = map.keySet();
        if(map!=null){
            for(Long k:keys){
                list.add(k);
            }
            Collections.sort(list);
        }
        return list.toArray();
    }
}
