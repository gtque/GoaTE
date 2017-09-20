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
package com.thegoate.mock;

import com.thegoate.Goate;
import com.thegoate.annotations.AnnotationFactory;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.mock.annotations.Mocker;
import com.thegoate.mock.staff.Mock;

import java.lang.reflect.InvocationTargetException;

/**
 * Builds and returns a web driver of the given type.
 * Created by Eric Angeli on 6/28/2017.
 */
public class MockBuilder {

    BleatBox LOG = BleatFactory.getLogger(getClass());
    Goate definition = new Goate();
    String currentMethod = "";
    int constructorIndex = 0;
    int methodIndex = 0;
    int methodParameterIndex = 0;

    public Mock buildMocker(String mock) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        AnnotationFactory af = new AnnotationFactory();
        return (Mock) af.annotatedWith(Mocker.class).find(mock).using("type").build();
    }

    public Object build(){
        return build(definition);
    }

    public Object build(Goate data){
        Mock mock = null;
        String type = "object";
        try {
            type = ""+data.get("type",type);
            mock = buildMocker(type);
        }catch(Exception e){
            LOG.error("Failed to load the mock builder for: " + type + "\n" + e.getMessage(), e);
        }
        return mock!=null?mock.build(data):null;
    }

    public MockBuilder define(String property, Object value){
        definition.put(property, value);
        return this;
    }

    public MockBuilder object(){
        return type("object");
    }

    public MockBuilder spy(){
        return type("spy");
    }

    public MockBuilder type(String type){
        return define("type",type);
    }

    public MockBuilder setClass(Class theClass){
        return setClass(theClass.getName());
    }

    public MockBuilder setClass(String theClass){
        return define("class", theClass);
    }

    public MockBuilder field(String field, Object value){
        return define("fields."+field,value);
    }

    public MockBuilder constructorParameter(Object value){
        return constructorParameter(value, null, constructorIndex++);
    }

    public MockBuilder constructorParameter(Object value, Class parameterType){
        return constructorParameter(value, parameterType.getName(), constructorIndex++);
    }

    public MockBuilder constructorParameter(Object value, String parameterType){
        return constructorParameter(value, parameterType, constructorIndex++);
    }

    public MockBuilder constructorParameter(Object value, int index){
        return constructorParameter(value, null, index);
    }

    public MockBuilder constructorParameter(Object value, String parameterType, int index){
        String base = "constructor."+index+".";
        define(base + "value", value);
        define(base + "index", index);
        if(parameterType!=null){
            define(base + "type", parameterType);
        }
        return this;
    }

    public MockBuilder method(String method){
        this.currentMethod = "methods."+methodIndex+"."+method;
        methodIndex++;
        methodParameterIndex = 0;
        return define(currentMethod,method);
    }

    public MockBuilder methodParameter(Object value){
        return methodParameter(value, null, methodParameterIndex++);
    }

    public MockBuilder methodParameter(Object value, int methodParameterIndex){
        return methodParameter(value, null, methodParameterIndex);
    }

    public MockBuilder methodParameter(Object value, Class parameterType){
        return methodParameter(value, parameterType.getName(), methodParameterIndex++);
    }

    public MockBuilder methodParameter(Object value, String parameterType){
        return methodParameter(value, parameterType, methodParameterIndex++);
    }

    public MockBuilder methodParameter(Object value, String parameterType, int index){
        String base = currentMethod+".parameters."+index+".";
        define(base + "value", value);
        define(base + "index", index);
        if(parameterType!=null){
            define(base + "type", parameterType);
        }
        return this;
    }

    public MockBuilder returnReal(){
        return define(currentMethod+".return.returnReal", "boolean::true");
    }

    public MockBuilder returnValue(Object value){
        return define(currentMethod+".return.value", value);
    }

    public MockBuilder returnValues(Object... values){
        for(int i=0; i<values.length; i++) {
            define(currentMethod + ".return.value." + i, values[i]);
        }
        return this;
    }

    public MockBuilder methodThrows(Class theClass){
        return methodThrows(theClass.getName());
    }

    public MockBuilder methodThrows(String theClassPath){
        return define(currentMethod+".throws", theClassPath);
    }

    public MockBuilder methodThrows(Throwable theThrowable){
        return define(currentMethod+".throws", theThrowable);
    }
}
