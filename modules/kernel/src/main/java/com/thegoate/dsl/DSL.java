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

package com.thegoate.dsl;

import com.thegoate.Goate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gtque on 4/20/2017.
 */
public abstract class DSL {

    List<String> definition;
    Object value;

    public DSL(Object value){
        define(value);
    }

    public String type(){
        String type = "undefined";

        if(definition!=null){
            if(definition.size()>0){
                type = definition.get(0);
            }
        }
        return type;
    }

    public List<String> define(Object value){
        this.value = value;
        definition = new ArrayList<>();
        String[] def = (""+value).split("::");
        for(String d:def){
            definition.add(d);
        }
        return definition;
    }

    public abstract Object evaluate(Goate data);
}
