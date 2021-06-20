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
package com.thegoate.json.utils.fill.serialize.to;

import com.thegoate.Goate;
import com.thegoate.Sponge;
import com.thegoate.json.utils.tojson.GoateToJSON;
import com.thegoate.reflection.GoateReflection;
import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.SerializerSponge;
import com.thegoate.utils.fill.serialize.to.SerializeTo;
import com.thegoate.utils.togoate.ToGoate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Eric Angeli on 4/2/2019.
 */
public class JsonString extends SerializeTo {

    @Override
    public Object serialize(Object pojo) {
        this.original = pojo;
        return new GoateToJSON(mapFields("", cereal, toJson(pojo))).convert();
    }

    private Object toJson(Object pojo) {
        Object json = null;
        if (pojo != null) {
            if (pojo instanceof List) {
                json = new JSONArray((List) pojo).toString(4);
            } else if (pojo instanceof Set) {
                json = new JSONArray((Set) pojo).toString(4);
            } else if (pojo.getClass().isArray()) {
                json = new JSONArray(Arrays.asList(pojo)).toString(4);
            } else {
                json = new JSONObject(pojo).toString(4);
            }
        }
        return json;
    }

    //    @Override
    public Object mapFields1(String base, Class cereal, Object so) {
        Goate value;
        if (!(so instanceof Goate)) {
            value = new ToGoate(so).convert();
        } else {
            value = (Goate) so;
        }
        return new GoateToJSON(value).convert();
    }

    @Override
    protected void scrub(Goate value) {
        for (String key : value.keys()) {
            Object v = value.get(key);
            if (v instanceof JSONObject) {
                value.put(key, new JSONObject());
            } else if (v instanceof JSONArray) {
                value.put(key, new JSONArray());
            }
        }
    }

    //    @Override
    public Object mapFields3(String base, Class cereal, Object so) {
        Goate value;
        if (!(so instanceof Goate)) {
            value = new ToGoate(so).convert();
        } else {
            value = (Goate) so;
        }
        GoateReflection gr = new GoateReflection();
        Map<String, Field> fields = gr.findFields(cereal);
        for (Map.Entry<String, Field> field : fields.entrySet()) {
            GoateSource gs = findGoateSource(field.getValue(), source);
            String fieldKey = field.getKey();
//            Object fieldValue = null;
            String altKey = fieldKey;
            if (gs != null) {
                altKey = gs.key();
            }

            if (gs != null && gs.serializeTo() != GoateSource.class) {
                boolean acc = field.getValue().isAccessible();
                field.getValue().setAccessible(true);
                try {
                    value.put(field.getKey(), doCast(field.getValue().get(original), gs.serializeTo()));
                } catch (IllegalAccessException | InstantiationException e) {
                    LOG.warn("Problem casting to the source type, using original value, value may not be what you expected: " + e.getMessage(), e);
                } finally {
                    field.getValue().setAccessible(acc);
                }
            }

            if (!fieldKey.equals(altKey)) {
                value.scrubSubKeys(base + "(\\.*[0-9]*\\.*)*" + fieldKey + ".*", fieldKey, altKey);
                fieldKey = altKey;
//                value = value.replace("\""+fieldKey+"\"","\""+altKey+"\"");
            }
            if (value.filter(fieldKey + ".\\.*").size() > 0) {//if(!gr.isPrimitive(field.getValue().getType())){
                //have to drop the high level entry, otherwise nested fields may not be scrubbed properly.
                dropHighLevelGoateEntry(value, base + "(\\.*[0-9]*\\.*)*" + fieldKey);
                value.merge((Goate) mapFields(base + "(\\.*[0-9]*\\.*)*" + fieldKey, field.getValue().getType(), value), false);
            }
        }
        return value;
    }

    private void dropHighLevelGoateEntry(Goate value, String pattern) {
        Goate filtered = value.filterStrict(pattern);
        for (String key : filtered.keys()) {
            value.drop(key);
        }
    }

    public Object mapFields2(String base, Class cereal, Object so) {
        String value = "" + so;
        GoateReflection gr = new GoateReflection();
        Map<String, Field> fields = gr.findFields(cereal);
        for (Map.Entry<String, Field> field : fields.entrySet()) {
            GoateSource gs = findGoateSource(field.getValue(), source);
            String fieldKey = field.getKey();
            String altKey = fieldKey;
            if (gs != null) {
                altKey = gs.key();
            }

            if (!fieldKey.equals(altKey)) {
                value = value.replace("\"" + fieldKey + "\"", "\"" + altKey + "\"");
            }
            if (!gr.isPrimitive(field.getValue().getType())) {
                value = "" + mapFields2(base, field.getValue().getType(), value);
            }
        }
        return value;
    }
}
