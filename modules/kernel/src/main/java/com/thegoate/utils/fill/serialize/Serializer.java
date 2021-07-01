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
import com.thegoate.utils.fill.serialize.to.SerializeTo;
import com.thegoate.utils.to.To;
import com.thegoate.utils.togoate.ToGoate;

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
public class Serializer<T, S, U> extends Cereal {
    private T pojo;
    private S source;
    private U cereal;
    private boolean serializeNested = true;
    private boolean alwaysSerializeGoatePojo = false;
    private boolean asSourced = true;
    private boolean includeNulls = false;

    public Serializer(T pojo, S source, U cereal) {
        this.pojo = pojo;
        this.source = source;
        this.cereal = cereal;
    }

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

    public Serializer<T,S,U> doSerializeNested() {
        this.serializeNested = true;
        return this;
    }

    public Serializer<T,S,U> asSourced(boolean asSourced) {
        this.asSourced = asSourced;
        return this;
    }

    public Serializer<T,S,U> includeNulls() {
        this.includeNulls = true;
        return this;
    }

    public Serializer<T,S,U> excludeNulls() {
        this.includeNulls = false;
        return this;
    }
    public Serializer<T,S,U> skipSerializingObjects() {
        this.serializeNested = false;
        return this;
    }

    public Serializer<T,S,U> alwaysSerializeGoatePojos() {
        this.alwaysSerializeGoatePojo = true;
        return this;
    }

    public Serializer<T,S,U> skipSerializingGoatePojos() {
        this.alwaysSerializeGoatePojo = false;
        return this;
    }

    public Goate toGoate() {
        Goate data = new Goate();
        for (Map.Entry<String, Object> entry : toMap(HashMap.class).entrySet()) {
            data.put(entry.getKey(), entry.getValue());
        }
        return data;
    }

    public U to(SerializeTo serializer) {
        return (U) serializer.source((Class) source).cereal(pojo.getClass()).asSourced(asSourced).serialize(pojo);
    }

    public U to(Class goateTo){
        return (U) new To(toGoate()).type(goateTo).convert();
    }

    public Map<String, Object> toMap(Class mapType) {
        Map<String, Object> data = null;
        try {
            data = (Map) mapType.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            LOG.error("Serialize Pojo", "The mapType could not be initialized: " + e.getMessage(), e);
        } catch (ClassCastException e) {
            LOG.error("Serialize Pojo", "The mapType was not a valid map type: " + e.getMessage(), e);
        }
        if (data != null) {
            GoateReflection gr = new GoateReflection();
            Map<String, Field> fields = gr.findFields(pojo.getClass());
            try {
                for (Map.Entry<String, Field> field : fields.entrySet()) {
                    GoateSource gs = findGoateSource(field.getValue(), (Class) source);
                    String fieldKey = field.getKey();
                    boolean exclude = false;
                    if (gs != null) {
                        fieldKey = gs.key();
                        if(asSourced){
                            exclude = gs.skipInModel();
                        }
                    }
                    if(exclude) {
                        LOG.debug("Excluding", fieldKey);
                    } else {
                        boolean acc = field.getValue().isAccessible();
                        field.getValue().setAccessible(true);
                        try {
                            Object o = field.getValue().get(pojo);
                            if (gs != null && gs.serializeTo() != GoateSource.class) {
                                o = doCast(o, gs.serializeTo());
                            }
                            if (o != null) {
                                Class type = field.getValue().getType();
                                if (!java.lang.reflect.Modifier.isStatic(field.getValue().getModifiers())) {
                                    if (checkNotPrimitive(type) && doSerialize(pojo.getClass())) {
                                        if (!type.equals(pojo.getClass())) {
                                            addMap(data, o, fieldKey);
                                        }
                                    } else {
                                        data.put(fieldKey, o);
                                    }
                                }
                            } else if(includeNulls) {
                                data.put(fieldKey, "null::");
                            }
                        } catch (IllegalAccessException | InstantiationException e) {
                            LOG.error("Serialize Pojo", "Failed to get field: " + e.getMessage(), e);
                        }
                        field.getValue().setAccessible(acc);
                    }
                }
            } catch (ClassCastException e) {
                LOG.error("Serialize Pojo", "The source, second constructor parameter, must be a Class (ie Something.class)");
            }
        }
        return data;
    }

    private boolean doSerialize(Class pojoType) {
        boolean serialize = true;
        if (!serializeNested) {
            if (alwaysSerializeGoatePojo) {
                if (pojoType.getAnnotation(GoatePojo.class) == null) {
                    serialize = false;
                }
            } else {
                serialize = false;
            }
        }
        return serialize;
    }

    private void addMap(Map<String, Object> data, Object o, String baseKey) {
        if (o instanceof List) {
            data.put(baseKey, o);//ToDo: figure out how to put the correct thing here...
            for (int i = 0; i < ((List) o).size(); i++) {
                Object io = ((List) o).get(i);
                process(data, io, baseKey + "." + i);
            }
        } else if (o.getClass().isArray()) {
            data.put(baseKey, o);
            for (int i = 0; i < Array.getLength(o); i++) {
                Object io = Array.get(o, i);
                process(data, io, baseKey + "." + i);
            }
        } else if (o instanceof Map) {
            int i = 0;
            data.put(baseKey, o);
            Iterator keys = ((Map) o).keySet().iterator();
            while (keys.hasNext()) {
                Object keyValue = keys.next();
                Object io = ((Map) o).get(keyValue);
                process(data, io, baseKey + "." + i + ".value");
//                if (io.getClass().getAnnotation(GoatePojo.class) != null) {
//                    addMap(data, io, baseKey + "." + i + ".value");
//                } else {
//                    data.put(baseKey + "." + i + ".value", io);
//                }
                data.put(baseKey + "." + i + ".key", keyValue);
                data.put(baseKey + "." + i + ".class", io.getClass().getName());
            }
        } else {
            Map<String, Object> innerD = new Serializer(o, source).toMap(HashMap.class);
            data.put(baseKey, innerD);
            for (Map.Entry<String, Object> entry : innerD.entrySet()) {
                data.put(baseKey + "." + entry.getKey(), entry.getValue());
            }
        }
    }

    private void process(Map<String, Object> data, Object io, String baseKey) {
        Class type = io.getClass();
        if (checkNotPrimitive(type)) {//if (io.getClass().getAnnotation(GoatePojo.class) != null) {
            addMap(data, io, baseKey);
        } else {
            data.put(baseKey, io);
        }
    }
}
