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
package com.thegoate.utils.fill.serialize.collections;

import com.thegoate.Goate;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.utils.fill.serialize.CastUtil;
import com.thegoate.utils.fill.serialize.GoateCastUtility;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Eric Angeli on 6/27/2018.
 */
public abstract class CastCollection extends GoateCastUtility {
    protected BleatBox LOGGER = BleatFactory.getLogger(getClass());
    protected Class typeAnnotation;

    public CastCollection(){}

    public CastCollection(Object value) {
        super(value);
    }

    protected Class getType(String id, Object value) {
        Object type = null;
        Class c = null;
        String path = "";
        if(typeAnnotation!=null){
            if(typeAnnotation.equals(MapKeyType.class)){
                path = ".key";
            } else if(typeAnnotation.equals(MapType.class)) {
                path = ".value";
            }
        }
        if(field!=null&&typeAnnotation!=null){
            if(field.getAnnotation(typeAnnotation)!=null){
                Class[] types = {};
                try {
                    Method check = typeAnnotation.getMethod("type", types);
                    type = check.invoke(field.getAnnotation(typeAnnotation), types);
//                    Object u = af.constructor(null).build(c);
//                    if (check != null && Boolean.parseBoolean("" + check.invoke(field.getAnnotation(typeAnnotation), checkArgs))) {
//                        utility = u;
//                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    LOGGER.debug("Cast Collection", "The type annotation does not have a 'type' field.");
                }
//                type = field.getAnnotation(typeAnnotation).type();
            }
        }
        Object t = data.get(id + path + ".class", type != null? type : (value != null ? value.getClass() : "i.should.have.bought.a.squirrel"));
        if (t instanceof String) {
            try {
                c = Class.forName("" + t);
            } catch (ClassNotFoundException e) {
                LOGGER.error("Cast Collection", "Can't find the class: " + t + ": if this is an object you may need to specify <index>.class=<class path>", e);
                throw new RuntimeException("Failed to find class: " + t);
            }
        } else if (!(t instanceof Class)) {
            c = t.getClass();
        } else {
            if(value==null){
                c = (Class) t;
            }
            else if(value instanceof Class) {
                c = (Class) value;
            } else {
                c = value.getClass();
            }
        }
        return c;
    }

    protected int size(Goate data){
        int size = 0;
        while(data.filter(""+size).size()>0){
            size++;
        }
        return size;
    }
}
