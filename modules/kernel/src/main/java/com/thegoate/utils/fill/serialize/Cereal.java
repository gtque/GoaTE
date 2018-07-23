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

import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;

import java.lang.reflect.Field;

/**
 * Created by Eric Angeli on 7/10/2018.
 */
public class Cereal {
    private BleatBox LOG = BleatFactory.getLogger(getClass());
//    protected Class dataSource;
//
//    public Cereal from(Class dataSource){
//        this.dataSource = dataSource;
//        return this;
//    }

    protected GoateSource findGoateSource(Field field, Class dataSource){
        GoateSource[] annotations = field.getAnnotationsByType(GoateSource.class);
        GoateSource gs = null;
        if(dataSource!=null) {
            for (GoateSource source : annotations) {
                if (source.source().equals(dataSource)) {
                    gs = source;
                    break;
                }
            }
        }
        return gs;
    }
}
