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
import com.thegoate.annotations.IsDefault;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.reflection.GoateReflection;
import com.thegoate.utils.fill.FillUtil;
import com.thegoate.utils.fill.FillUtility;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Fills the given string from the given data collection.
 * Does not currently support nested fill.
 * Created by Eric Angeli on 5/5/2017.
 */
@FillUtil
@IsDefault
public class FillPojo implements FillUtility{
    BleatBox LOG = BleatFactory.getLogger(getClass());
    Goate health = new Goate();
    Object fill;
    Goate data;
    public FillPojo(Object fill){
        this.fill = fill;
    }

    @Override
    public boolean isType(Object check) {
        return check instanceof Object;
    }

    @Override
    public FillUtility setData(Goate data){
        this.data = data;
        return this;
    }

    @Override
    public Goate healthCheck(){
        return health;
    }

    @Override
    public Object with(Goate data) {
        GoateReflection gr = new GoateReflection();
        for(Map.Entry<String, Field> e : gr.findFields(fill.getClass()).entrySet()){
            if(data.keys().contains(e.getKey())) {
                boolean access = e.getValue().canAccess(fill);//.isAccessible();
                e.getValue().setAccessible(true);
                try {
                    e.getValue().set(fill, data.get(e.getKey()));
                } catch (IllegalAccessException ex) {
                    LOG.debug("Filling Pojo", "Failed to set field " + e.getKey());
                }
                e.getValue().setAccessible(access);
            }
        }
        return fill;
    }
}
