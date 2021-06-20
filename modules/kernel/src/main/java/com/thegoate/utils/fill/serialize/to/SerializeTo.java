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
package com.thegoate.utils.fill.serialize.to;

import com.thegoate.Goate;
import com.thegoate.Sponge;
import com.thegoate.reflection.GoateReflection;
import com.thegoate.utils.fill.serialize.Cereal;
import com.thegoate.utils.fill.serialize.GoatePojo;
import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.SerializerSponge;
import com.thegoate.utils.fill.serialize.collections.ListType;
import com.thegoate.utils.fill.serialize.collections.MapType;
import com.thegoate.utils.togoate.ToGoate;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Eric Angeli on 4/2/2019.
 */
public abstract class SerializeTo extends Cereal {
    protected Class cereal;
    protected Class source;
    protected Object original;
    protected Map<String, Class> castList = new HashMap<>();

    public SerializeTo source(Class source) {
        this.source = source;
        return this;
    }

    public SerializeTo cereal(Class pojoType) {
        this.cereal = pojoType;
        return this;
    }

    protected GoateSource findSource(Field field) {
        GoateSource[] gs = field.getAnnotationsByType(GoateSource.class);
        for (GoateSource fieldSource : gs) {
            if (fieldSource.source().equals(this.source)) {
                return fieldSource;
            }
        }
        return null;
    }


    protected Map<String, String> buildMappings(Object object, String currentPath, String currentPathMapping) {
        Map<String, String> mappings = new HashMap<>();
        GoateReflection gr = new GoateReflection();
        Object type = object;
        Object collectionType = null;
//        String path = "";
//        String pathMapping = "";
        if (object instanceof Field) {
            type = ((Field) object).getType();
            collectionType = ((Field) object).getAnnotation(ListType.class);
            if (collectionType == null) {
                collectionType = ((Field) object).getAnnotation(MapType.class);
            }
        }
        if (object.getClass().isArray()) {
            type = Arrays.asList(object);
        }
        if ((type instanceof Class && Collection.class.isAssignableFrom((Class) type)) || type instanceof Collection) {
            if (type instanceof Class) {
                if (collectionType != null) {
                    mappings.put((currentPath.isEmpty() ? "" : (currentPath + "\\.")) + "[0-9]+", (currentPathMapping.isEmpty() ? "" : (currentPathMapping + ".")) + "[0-9]+");
                    mappings.putAll(buildMapping(((ListType) collectionType).type(), (currentPath.isEmpty() ? "" : (currentPath + "\\.")) + "[0-9]+", (currentPathMapping.isEmpty() ? "" : (currentPathMapping + ".")) + "[0-9]+"));
                }
            } else {
                Iterator it = ((Collection) type).iterator();
                while (it.hasNext()) {
                    Object next = it.next();
                    String modifiedPath = ".[0-9]+";
                    Map<String, String> subMappings = buildMappings(next, currentPath + "\\" + modifiedPath, currentPathMapping + modifiedPath);
                    mappings.put(currentPath + "\\" + modifiedPath, currentPathMapping + modifiedPath);
                    mappings.putAll(subMappings);
                }
            }
        } else if ((type instanceof Class && Map.class.isAssignableFrom((Class) type)) || type instanceof Map) {
            if (type instanceof Class) {
                if (collectionType != null) {
                    mappings.put((currentPath.isEmpty() ? "" : (currentPath + "\\.")) + ".+", (currentPathMapping.isEmpty() ? "" : (currentPathMapping + ".")) + ".+");
                    mappings.putAll(buildMapping(((MapType) collectionType).type(), (currentPath.isEmpty() ? "" : (currentPath + "\\.")) + ".+", (currentPathMapping.isEmpty() ? "" : (currentPathMapping + ".")) + ".+"));
                }
            } else {
                Iterator it = ((Map) type).entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry next = (Map.Entry) it.next();
                    String modifiedPath = "." + next.getKey();
                    Map<String, String> subMappings = buildMappings(next.getValue(), currentPath + "\\." + modifiedPath, currentPathMapping + modifiedPath);
                    mappings.put(currentPath + "\\" + modifiedPath, currentPathMapping + modifiedPath);
                    mappings.putAll(subMappings);
                }
            }
        } else {
            mappings = buildMapping(type instanceof Class ? (Class) type : type.getClass(), currentPath, currentPathMapping);
        }
        return mappings;
    }

    protected Map<String, String> buildMapping(Class type, String currentPath, String currentPathMapping) {
        Map<String, String> mappings = new HashMap<>();
        GoateReflection gr = new GoateReflection();
        Map<String, Field> fields = gr.findFields(type);
        for (Map.Entry<String, Field> field : fields.entrySet()) {
            GoateSource gs = findSource(field.getValue());
            String cp = (currentPath.isEmpty() ? "" : (currentPath + "\\."));
            String cpm = (currentPathMapping.isEmpty() ? "" : (currentPathMapping + "."));
            if (gs != null) {
                mappings.put(cp + field.getKey(), cpm + gs.key());
                if (gs.serializeTo() != GoateSource.class) {
                    castList.put(cpm + gs.key(), gs.serializeTo());
                }
            } else if (!cp.equals(cpm)) {
                mappings.put(cp + field.getKey(), cpm + field.getKey());
            }

            String fieldKey = cp + field.getKey();
            String fieldKeyMapping = cpm + field.getKey();
            if (mappings.containsKey(fieldKey)) {
                fieldKeyMapping = mappings.get(fieldKey);
            }
//            fieldKeyMapping = fieldKeyMapping;

            if (field.getValue().getType().getAnnotation(GoatePojo.class) != null || gr.isCollectionOrMap(field.getValue())) {
                Map<String, String> subMappings = buildMappings(field.getValue(), fieldKey, fieldKeyMapping);
                for (Map.Entry<String, String> subMapping : subMappings.entrySet()) {
                    mappings.put(subMapping.getKey(), subMapping.getValue());
                }
            }
        }
        return mappings;
    }

    public Object mapFields(String base, Class cereal, Object so) {
        Goate value;
        if (!(so instanceof Goate)) {
            value = new ToGoate(so).convert();
        } else {
            value = (Goate) so;
        }
        scrub(value);
        Map<String, String> mappings = buildMappings(original, "", "");
        for (Map.Entry<String, String> mapping : mappings.entrySet()) {
            value = value.scrubSubKeys(mapping.getKey(), null, getSponge(mapping.getValue()));
            if (castList.containsKey(mapping.getValue())) {
                for (String key : value.filter(mapping.getValue().replaceAll("\\.\\.", "_double_dots_23458ncakk0")
                        .replaceAll("\\.", "\\.").replaceAll("_double_dots_23458ncakk0", "\\..")).keys()) {
                    try {
                        value.put(key, doCast(value.get(key), castList.get(mapping.getValue())));
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                        LOG.error("Problem serializing", "failed to serialize (" + mapping.getValue() + ") to: " + castList.get(mapping.getValue()));
                    }
                }
            }
        }
        return value;
    }

    private Sponge getSponge(String mapping) {
        return new SerializerSponge(mapping);
    }

    public abstract Object serialize(Object pojo);

    protected abstract void scrub(Goate value);
}
