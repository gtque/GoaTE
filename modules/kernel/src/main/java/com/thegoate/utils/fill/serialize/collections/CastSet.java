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
import com.thegoate.utils.togoate.ToGoate;
import com.thegoate.utils.type.types.SetType;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Eric Angeli on 6/27/2018.
 */
@CastUtil(type = Set.class)
public class CastSet extends CastCollection {
    public CastSet(Object value) {
        super(value);
        typeAnnotation = SetType.class;
    }

    @Override
    public <T> T cast(Class<T> type) {
        try {
            if (value != null) {
                return type.cast(value);
            }
        } catch (Throwable t) {
            LOGGER.debug("Cast set", "Couldn't just cast to an set, now trying to build it from the data.");
        }
        LOGGER.debug("Cast set", "building from data.");
        Set<Object> o = new HashSet<>();//this needs to be set up from the serialization somehow.
        int i = 0;
        if(data.filter("" + i).size() == 0){
            data = new ToGoate(data.get(0)).convert();
        }
        while (data.filter("" + i).size() > 0) {
            try {
                o.add(new Cast(data.filter("" + i).scrubKeys("" + i + "\\."), dataSource).container(container).cast(data.get("" + i), getType("" + i, data.get("" + i))));
            } catch (IllegalAccessException | InstantiationException e) {
                LOGGER.error("Cast Array", "Failed to build set: " + e.getMessage(), e);
                throw new RuntimeException("Failed to construct set: " + e.getMessage());
            }
            i++;
        }
        return type.cast(o);
    }

    @Override
    public boolean isType(Object check) {
        Set<Object> test = new HashSet<>();
        boolean result = false;
        try {
            ((Class) check).cast(test);
            result = true;
        } catch (Throwable t) {
        }
        return result;
    }
}
