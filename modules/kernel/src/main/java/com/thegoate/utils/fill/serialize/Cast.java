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
package com.thegoate.utils.fill.serialize;

import com.thegoate.Goate;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.reflection.GoateReflection;
import com.thegoate.utils.UnknownUtilType;
import com.thegoate.utils.fill.serialize.string.StringConverter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Eric Angeli on 6/26/2018.
 */
public class Cast extends UnknownUtilType {

    private BleatBox LOGGER = BleatFactory.getLogger(getClass());
    private Object[] args = {};
    private Goate data;
    private Class dataSource;
    private Field field;

    public Cast(Goate data, Class dataSource){
        this.data = data;
        this.dataSource = dataSource;
    }

    public <T> T cast(Object value, Class<T> type) throws IllegalAccessException, InstantiationException {
        Object object = null;
        GoateReflection gr = new GoateReflection();
        if(gr.isPrimitive(type)){
            object = buildFromCastUtil(type, value);
        } else{
            if(type.getAnnotation(GoatePojo.class)!=null){
                DeSerializer serializer = new DeSerializer().data(data).from(dataSource);
                object = serializer.build(type);
            } else {
                if(type.equals(String.class)){
                    if(value instanceof String) {
                        object = value;
                    } else if(value == null){
                        object = null;
                    } else {
                        object = new StringConverter().value(value).convert();
                    }
                } else {
                    Constructor constructor = findConstructor(type, value);
                    try {
                        if (constructor != null) {
                            object = constructor.newInstance(args);
                        }
                    } catch (InvocationTargetException e) {
                        LOGGER.debug("Cast", "Failed to construct the object: " + e.getMessage(), e);
                    }
                    if (object == null) {
                        object = buildFromCastUtil(type, value);
                    }
                }
            }
        }
        return (T)object;
    }

    public Cast field(Field field){
        this.field = field;
        return this;
    }

    protected Object buildFromCastUtil(Class type, Object value){
        CastUtility caster = (CastUtility)buildUtil(type, CastUtil.class, value);
        if(caster==null){
            LOGGER.error("Cast","Could not build: " + type.getName() +". You may need to implement a CastUtility to support that object type.");
            throw new RuntimeException("Could not build: " + type.getName() +". You may need to implement a CastUtility to support that object type.");
        }
        LOGGER.debug("Cast","Cast utility found, building object.");
        caster.setData(data);
        return caster.dataSource(dataSource).field(field).cast(type);
    }

    protected Constructor findConstructor(Class type, Object value){
        Object[] asString = {""+value};
        Object[] selfie = {value};
        Object[] empty = {};

        GoateReflection gr = new GoateReflection();
        args = empty;
        Constructor constructor = gr.findConstructor(type, empty);
        if(constructor==null){
            args = selfie;
            constructor = gr.findConstructor(type, selfie);
            if(constructor==null){
                args = asString;
                constructor = gr.findConstructor(type, asString);
            }
        }
        return constructor;
    }
}
