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
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.reflection.GoateReflection;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Remember: GoaTE is intended for testing purposes, as such unsafe
 * methods, reflection, and access may be used or altered. Use in production code at your own risk.
 * Created by Eric Angeli on 6/26/2018.
 */
public class Serializer<T, S> extends Cereal {
    private BleatBox LOG = BleatFactory.getLogger(getClass());
    private T pojo;
    private S source;

    public Serializer(T pojo, S source) {
        this.pojo = pojo;
        this.source = source;
    }

    public void setPojo(T pojo) {
        this.pojo = pojo;
    }

    public T getPojo() {
        return pojo;
    }

    public void setSource(S source) {
        this.source = source;
    }

    public S getSource() {
        return source;
    }

    public Goate toGoate() {
        Goate data = new Goate();
        for (Map.Entry<String, Object> entry : toMap(HashMap.class).entrySet()) {
            data.put(entry.getKey(), entry.getValue());
        }
        return data;
    }

    public Map<String, Object> toMap(Class mapType) {
        Map<String, Object> data = null;
        try {
            data = (Map) mapType.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            LOG.error("Serialze Pojo", "The mapType could not be initialized: " + e.getMessage(), e);
        } catch (ClassCastException e) {
            LOG.error("Serialze Pojo", "The mapType was not a valid map type: " + e.getMessage(), e);
        }
        if (data != null) {
            GoateReflection gr = new GoateReflection();
            Map<String, Field> fields = gr.findFields(pojo.getClass());
            try {
                for (Map.Entry<String, Field> field : fields.entrySet()) {
                    GoateSource gs = findGoateSource(field.getValue(), (Class) source);
                    String fieldKey = field.getKey();
                    if (gs != null) {
                        fieldKey = gs.key();
                    }
                    boolean acc = field.getValue().isAccessible();
                    field.getValue().setAccessible(true);
                    try {
                        Object o = field.getValue().get(pojo);
                        if (o != null) {
                            if (field.getValue().getType().getAnnotation(GoatePojo.class) != null) {
                                addMap(data, o, fieldKey + ".");
                            } else if (o instanceof List) {
                                for (int i = 0; i < ((List) o).size(); i++) {
                                    Object io = ((List) o).get(i);
                                    if (io.getClass().getAnnotation(GoatePojo.class) != null) {
                                        addMap(data, io, fieldKey + "." + i + ".");
                                    } else {
                                        data.put(fieldKey + "." + i, io);
                                    }
                                }
                            } else if (o.getClass().isArray()) {
                                for(int i = 0; i< Array.getLength(o); i++){
                                    Object io = Array.get(o,i);
                                    if (io.getClass().getAnnotation(GoatePojo.class) != null) {
                                        addMap(data, io, fieldKey + "." + i + ".");
                                    } else {
                                        data.put(fieldKey + "." + i, io);
                                    }
                                }
                            } else if (o instanceof Map) {
                                int i = 0;
                                Iterator keys = ((Map)o).keySet().iterator();
                                while(keys.hasNext()) {
                                    Object keyValue = keys.next();
                                    Object io = ((Map)o).get(keyValue);
                                    if (io.getClass().getAnnotation(GoatePojo.class) != null) {
                                        addMap(data, io, fieldKey + "." + i + ".value.");
                                    } else {
                                        data.put(fieldKey + "." + i + ".value", io);
                                    }
                                    data.put(fieldKey + "." + i + ".key", keyValue);
                                    data.put(fieldKey + "." + i + ".class", io.getClass().getName());
                                }
                            } else {
                                data.put(fieldKey, o);
                            }
                        } else {
                            data.put(fieldKey, o);
                        }
                    } catch (IllegalAccessException e) {
                        LOG.error("Serialize Pojo", "Failed to get field: " + e.getMessage(), e);
                    }
                    field.getValue().setAccessible(acc);
                }
            } catch (ClassCastException e) {
                LOG.error("Serialze Pojo", "The source, second constructor parameter, must be a Class (ie Something.class)");
            }
        }
        return data;
    }

    private void addMap(Map<String, Object> data, Object o, String baseKey) {
        Map<String, Object> innerD = new Serializer(o, source).toMap(HashMap.class);
        for (Map.Entry<String, Object> entry : innerD.entrySet()) {
            data.put(baseKey + entry.getKey(), entry.getValue());
        }
    }
}
