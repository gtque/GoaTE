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

import com.thegoate.dsl.Interpreter;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The manager for the collection of test data.
 * Created by gtque on 4/19/2017.
 */
public class Goate {
    Map<String, Object> data = new ConcurrentHashMap<>();
    Interpreter dictionary;

    public Goate() {
        dictionary = new Interpreter(this);
    }

    public int size(){
        int size = 0;
        if(data!=null){
            size = data.size();
        }
        return size;
    }

    public Set<String> keys() {
        return data.keySet();
    }

    public Goate put(String key, Object value) {
        if (data == null) {
            data = new ConcurrentHashMap<>();
        }
        if(value!=null) {
            if(key.contains("##")){
                key = buildKey(key);
            }
            data.put(key, value);
        }
        return this;
    }

    public String buildKey(String key){
        String fullKey = key;
        while(fullKey.contains("##")) {
            Goate billy = filter(key.substring(0, key.indexOf("##")));
            fullKey = key.replace("##", ""+billy.size());
        }
        return fullKey;
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
                } else if (def != null) {
                    data.put(key, def);
                    value = def;
                }
            }
        }
        if (value == null && !data.containsKey(key)) {
            value = def;
        }
        if (value != null && dsl) {
            value = processDSL(value);
        }
        return value;
    }

    public Object processDSL(Object value) {
        String check = "" + value;
        if (check.contains("::")) {
            check = check.substring(0, check.indexOf("::"));
        } else {
            check = null;
        }
        if (check != null) {
            value = dictionary.translate(check, value);
        }
        return value;
    }

    /**
     * Simple filter, matches if key starts with the given pattern.
     * @param pattern The pattern to match
     * @return A Goate collection containing matching elements.
     */
    public Goate filter(String pattern){
        Goate filtered = new Goate();
        if(data!=null){
            for(String key:keys()){
                if(key.startsWith(pattern)){
                    filtered.put(key, getStrict(key));
                }
            }
        }
        return filtered;
    }
}
