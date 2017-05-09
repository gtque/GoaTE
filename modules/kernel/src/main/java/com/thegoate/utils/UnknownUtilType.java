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

package com.thegoate.utils;

import com.thegoate.annotations.AnnotationFactory;
import com.thegoate.annotations.IsDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Base Class for utils. Determines the type of util to use.
 * The util(s) should have a method called isType that checks to see if the input
 * is of the type that service can process. All generic utility classes should extend this class
 * and call buildUtil in the constructor.
 * Created by gtque on 5/4/2017.
 */
public class UnknownUtilType {
    final Logger LOG = LoggerFactory.getLogger(getClass());

    protected Object buildUtil(Object obj, Class<? extends java.lang.annotation.Annotation> util) {
        return buildUtil(obj, util, null, null);
    }

    protected Object buildUtil(Object obj, Class<? extends java.lang.annotation.Annotation> util, String id, Method identifier) {
        Object utility = null;
        Class def = null;
        Object[] args = {obj};
        AnnotationFactory af = new AnnotationFactory();
        af.constructorArgs(args);
        Map<String, Class> utils = af.annotatedWith(util).getDirectory(util.getCanonicalName(), id, identifier);
        if(utils!=null) {
            for (String key : utils.keySet()){
                try {
                    Class c = utils.get(key);
                    Annotation d = c.getAnnotation(IsDefault.class);
                    if(d!=null){
                        def = c;
                    }else {
                        Class[] types = {Object.class};
                        Method check = c.getMethod("isType", types);
                        Object u = af.constructor(null).build(c);
                        if (check != null && Boolean.parseBoolean(""+check.invoke(u, args))) {
                            utility = u;
                        }
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException |InstantiationException e) {
                    LOG.warn("The class did not have isType, cannot determine if that class is the correct type. " + e.getMessage());
                }
            }
        }
        if(utility==null){
            if(def!=null){
                try{
                    utility = af.constructor(null).build(def);
                } catch (IllegalAccessException | InvocationTargetException |InstantiationException e){
                    LOG.warn("Problem instantiating the default utility: " + e.getMessage(), e);
                }
            }
        }
        return utility;
    }
}
