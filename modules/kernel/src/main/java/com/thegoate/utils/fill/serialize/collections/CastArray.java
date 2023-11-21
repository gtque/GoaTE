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

import com.thegoate.utils.fill.serialize.Cast;
import com.thegoate.utils.fill.serialize.CastUtil;

import java.lang.reflect.Array;

/**
 * Created by Eric Angeli on 6/27/2018.
 */
@CastUtil(type = Array.class)
public class CastArray extends CastCollection {

    public CastArray(Object value){
        this.value = value;
    }

    @Override
    public <T> T cast(Class<T> type) {
        try {
            if(value!=null) {
                return type.cast(value);
            }
        } catch (Throwable t){
            LOGGER.debug("Cast Array","Couldn't just cast to an array, now trying to build it from the data.");
        }
        LOGGER.debug("Cast Array","building from data.");
        Object o = Array.newInstance(type.getComponentType(),size(data));
        for(int i = 0; i<Array.getLength(o); i++){
            if(data.filter(""+i+"\\.").size()==0) {
                Array.set(o, i, data.get(""+i));
            } else {
                try {
                    Array.set(o, i, new Cast(data.filter("" + i).scrubKeys("" + i + "\\."), dataSource).cast(data.get("" + i), getType(""+i, type.getComponentType())));
                } catch (IllegalAccessException | InstantiationException e) {
                    LOGGER.error("Cast Array", "Failed to build array: " + e.getMessage(), e);
                    throw new RuntimeException("Failed to construct array: " + e.getMessage());
                }
            }
        }
        return type.cast(o);
    }

    @Override
    public boolean isType(Object check) {
        return ((Class)check).isArray();
    }
}
