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
import java.lang.reflect.Method;

/**
 * Created by Eric Angeli on 6/26/2018.
 */
public class Cast extends UnknownUtilType {

    private BleatBox LOGGER = BleatFactory.getLogger(getClass());
    private Object[] args = {};
    private Goate data;
    private Class dataSource;
    private Field field;
    private Object container;

    public Cast(Goate data, Class dataSource) {
        this.data = data;
        this.dataSource = dataSource;
    }

    public <T> T cast(Object value, Class<T> type) throws IllegalAccessException, InstantiationException {
        Object object = null;
        GoateReflection gr = new GoateReflection();
        if (gr.isPrimitive(type)) {
            object = buildFromCastUtil(type, value);
        } else {
            GoatePojo goatePojo = type.getAnnotation(GoatePojo.class);
            if (goatePojo != null) {
                if (goatePojo.forceCast()) {
                    object = buildFromCastUtil(type, value);
                } else {
                    DeSerializer serializer = new DeSerializer().data(data).from(dataSource);
                    object = serializer.build(type);
                }
            } else {
                if (type.equals(String.class)) {
                    if (value instanceof String) {
                        object = value;
                    } else if (value == null) {
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
        return (T) object;
    }

    public Cast container(Object o) {
        this.container = o;
        return this;
    }

    public Cast field(Field field) {
        this.field = field;
        return this;
    }

    protected Class getType(Class type) {
        Class t = type;
        if (type == TypeT.class) {
            if (container instanceof TypeT) {
                try {

                    Method get_type = container.getClass().getMethod("goateType", int.class);
                    type = (Class) get_type.invoke(container, 0);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    LOGGER.debug("problem detecting type T, will just assume it is the type: " + e.getMessage());
                }
            }
            LOGGER.debug("found a generic type: " + type);
        } else {
            GoateReflection gr = new GoateReflection();
            Class klass = gr.nullablePrimitiveType(t);
            if (klass != null) {
                t = klass;
            } else {
                klass = gr.nonPrimitiveType(t);
                if (klass != null) {
                    t = klass;
                }
            }
        }
        return t;
    }

    protected Object buildFromCastUtil(Class type, Object value) {
        CastUtility caster = (CastUtility) buildUtil(getType(type), CastUtil.class, value);
        if (caster == null) {
            LOGGER.error("Cast", "Could not build: " + type.getName() + ". You may need to implement a CastUtility to support that object type.");
            //throw new RuntimeException("Could not build: " + type.getName() + ". You may need to implement a CastUtility to support that object type.");
            return null;
        }
        LOGGER.debug("Cast", "Cast utility found, building object.");
        caster.setData(data);
        if (caster instanceof GoateCastUtility) {
            ((GoateCastUtility) caster).setContainer(container);
        }
        return caster.dataSource(dataSource).field(field).cast(type);
    }

    protected Constructor findConstructor(Class type, Object value) {
        Object[] asString = {"" + value};
        Object[] selfie = {value};
        Object[] empty = {};

        GoateReflection gr = new GoateReflection();

        args = selfie;
        Constructor constructor = gr.findConstructor(type, selfie);
        if (constructor == null) {
            args = empty;
            constructor = gr.findConstructor(type, empty);
            if (constructor == null) {
                args = asString;
                constructor = gr.findConstructor(type, asString);
            }
        }
        return constructor;
    }

    @Override
    public boolean checkType(Class tool, Class type) {
//        CastUtil tu = (CastUtil) tool.getAnnotation(CastUtil.class);
//        return tu.type()!=null?(tu.type() == type):(type == null);
        return false;
    }
}
