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

package com.thegoate;

import com.thegoate.dsl.DSL;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by gtque on 4/19/2017.
 */
public class Goate {
    Map<String, Object> data = new ConcurrentHashMap<>();
    DSL dictionary;

    public Goate(){
        dictionary = new DSL(this);
    }
    public Object getStrict(String key) {
        return get(key, null, false);
    }

    public Object get(String key) {
        return get(key, null, true);
    }

    public Object get(String key, Object def) {
        return get(key, def, true);
    }

    public Object get(String key, Object def, boolean dsl) {
        Object value = System.getProperty(key);
        if (value == null) {
            value = System.getenv(key);
            if (value == null) {
                if (data.containsKey(key)) {
                    value = data.get(key);
                }else if(def!=null){
                    data.put(key, def);
                    value = def;
                }
            }
        }
        if (value == null) {
            value = def;
        }
        if (value != null && dsl) {
            value = processDSL(value);
        }
        return value;
    }

    public Object processDSL(Object value){
        String check = "" + value;
        if(check.contains("::")){
            check = check.substring(0,check.indexOf("::"));
        }else{
            check = null;
        }
        if(check!=null){
            value = dictionary.translate(check, value);
        }
        return value;
    }
}
