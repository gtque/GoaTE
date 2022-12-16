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
package com.thegoate.logging;

import com.thegoate.annotations.AnnotationFactory;
import com.thegoate.reflection.GoateReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Simple factory class to initialize the logger.
 * Assumes only one none default implementation of BleatBox, otherwise the first none default BleatBox is returned.
 * If no none default BleatBox is found, the default will be returned.
 * Created by Eric Angeli on 6/5/2017.
 */
public class BleatFactory {
    protected static Logger LOG = LoggerFactory.getLogger(BleatFactory.class);//don't want to use self just in case.
    public static BleatBox getLogger(Class logFromClass){
        BleatBox bb = null;
        AnnotationFactory af = new AnnotationFactory().doDefault().annotatedWith(Bleat.class).buildDirectory();
        Map<String, Class> dir = af.getDirectory(Bleat.class.getCanonicalName());
        Class def = dir.get("default");
        Class logger = null;
        for(String c:dir.keySet()){
            //always treat BleatBoxDefault as a default and don't set it if found even if the found default is something different.
            if(def!=null&&!def.equals(dir.get(c))&&!c.equals(BleatBoxDefault.class.getCanonicalName())){
                logger = dir.get(c);
                break;
            }
        }
        if(logger==null){
            logger = def;
        }
        if(logger!=null){
            GoateReflection gr = new GoateReflection();
            Object[] args = {logFromClass};
            try {
                bb = (BleatBox)gr.findConstructor(logger.getConstructors(), args).newInstance(args);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                LOG.error("Failed initializing the logger: " + e.getMessage(), e);
            }
        }
        return bb;
    }
}
