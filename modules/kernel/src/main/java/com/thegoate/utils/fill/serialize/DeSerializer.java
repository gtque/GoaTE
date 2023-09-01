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
import com.thegoate.eut.Eut;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.reflection.GoateReflection;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.thegoate.utils.fill.serialize.GoateSourceLister.FLATTEN;

/**
 * Remember: GoaTE is intended for testing purposes, as such unsafe
 * methods, reflection, and access may be used or altered. Use in production code at your own risk.
 * Created by Eric Angeli on 6/26/2018.
 */
public class DeSerializer extends Cereal {
    private BleatBox LOG = BleatFactory.getLogger(getClass());
    private Goate data;
    private Class dataSource;
    private List<Class> genericType = new ArrayList<>();
    private boolean OLD_SERIALIZER = false;
    private Object pojo = null;
    private Goate dupeData = new Goate();

    public DeSerializer pojo(Object pojo) {
        this.pojo = pojo;
        return this;
    }

    public Goate getDupeData(){
        return dupeData;
    }

    public <T> T build(Class<T> type) {
        try {
            if (pojo == null) {
                pojo = buildInstance(type);
            }
            if (data == null) {
                LOG.debug("Build Pojo", "The data was null, the pojo will be initialized but empty (unless defaults are set)");
            } else {
                dupeData.merge(data, false);
                if (dataSource == null) {
                    LOG.debug("Build Pojo", "The data source was not specified, defaulting to default");
                    dataSource = DefaultSource.class;
                }
                if (OLD_SERIALIZER) {
                    oldSerializer(pojo, type);
                } else {
                    orderedSerializer(pojo, type);
                }
            }
        } catch (IllegalAccessException | InstantiationException e) {
            LOG.error("Build Pojo", "Failed to build the pojo: " + e.getMessage(), e);
        }
        return (T) pojo;
    }

    private void oldSerializer(Object o, Class type) {
        GoateReflection gr = new GoateReflection();
        Map<String, Field> fields = gr.findFields(type);
        for (Map.Entry<String, Field> field : fields.entrySet()) {
            GoateSource gs = findGoateSource(field.getValue(), dataSource);
            String fieldKey = field.getKey();
            boolean flatten = false;
            if (gs != null) {
                fieldKey = gs.key();
                flatten = gs.flatten();
            }
            Object value = fieldKey.isEmpty() ? data : data.get(fieldKey);
            boolean acc = Modifier.isStatic(field.getValue().getModifiers()) ? field.getValue().canAccess(null) : field.getValue().canAccess(o);//field.getValue().canAccess(o);//.isAccessible();
            boolean isAcc = acc;
            try {
                field.getValue().setAccessible(true);
                isAcc = Modifier.isStatic(field.getValue().getModifiers()) ? field.getValue().canAccess(null) : field.getValue().canAccess(o);
            } catch (InaccessibleObjectException | SecurityException e) {
                LOG.debug("DeSerializer", "Failed to make " + field.getValue().getName() + " accessible, skipping for serialization unless already accessible");
            }
            if (isAcc) {
                try {
                    if (value != null
                            || data.filter(fieldKey + "\\.").size() > 0
                            || data.getStrict(fieldKey) != null
                            || field.getValue().getType().getAnnotation(GoatePojo.class) != null) {
                        Goate d = new Goate().merge(data, false);
                        if (!flatten) {
                            d = data.filter(fieldKey.replace("##", "[0-9]*"));
                            if (!fieldKey.isEmpty()) {
                                d = d.scrubKeys(fieldKey + "\\.");
                            }
                        } else {
                            d = data.filterStrict(fieldKey.replace("##", "[0-9]*"));
                        }
                        field.getValue().set(o, new Cast(d, dataSource).container(o).field(field.getValue()).cast(value, getType(field.getValue())));
                    }
                } catch (Exception e) {
                    LOG.error("Build Pojo", "Failed to set field: " + e.getMessage(), e);
                }
                try {
                    field.getValue().setAccessible(acc);
                } catch (InaccessibleObjectException | SecurityException exception) {
                    LOG.debug("DeSerializer", "Unable to reset accessibility: " + field.getKey());
                }
            }
        }
    }

    private Class getType(Field field) {
        Class c = field.getType();
        if (c == Object.class) {
            Generic t = field.getAnnotation(Generic.class);
            if (t != null) {
                int index = t.index();
                if (genericType.size() > index) {
                    c = genericType.get(index);
                }
            }
        }
        return c;
    }

    private Object getProperty(String key, Object def, Goate properties) {
        Object property = properties.get(key, def);
        return property;
    }

    private void orderedSerializer(Object pojo, Class type) {
        GoateSourceLister sourceLister = new GoateSourceLister();
        List<String> sources = sourceLister.sourcePrefixList(type, dataSource, pojo instanceof Eut);
        GoateReflection gr = new GoateReflection();
        Map<String, Field> fields = gr.findFields(type);
        if (fields.size() > 0) {
            Field modifiersField = gr.findField(Field.class, "modifiers");
            boolean modifiersFieldAccessible = false;
            if (modifiersField != null) {
                try {
                    modifiersFieldAccessible = modifiersField.canAccess(pojo);
                } catch (IllegalArgumentException e) {
                    LOG.debug("Eut Load", "couldn't tell if modifiers is accessible or not");
                    modifiersField = null;
                }
                try {
                    if (modifiersField != null) {
                        modifiersField.setAccessible(true);
                    }
                } catch (InaccessibleObjectException | SecurityException e) {
                    LOG.debug("Eut Load", "failed to set modifiers to be accessible");
                    modifiersField = null;
                }
            }

            for (Map.Entry<String, Field> field : fields.entrySet()) {
                String fieldName = field.getKey();
                Field fieldself = field.getValue();
                int fieldModifiers = fieldself.getModifiers();
                boolean acc = Modifier.isStatic(field.getValue().getModifiers()) ? field.getValue().canAccess(null) : field.getValue().canAccess(pojo);//.isAccessible();
                boolean isAcc = acc;
                try {
                    field.getValue().setAccessible(true);
                    isAcc = Modifier.isStatic(field.getValue().getModifiers()) ? field.getValue().canAccess(null) : field.getValue().canAccess(pojo);
                } catch (InaccessibleObjectException | SecurityException e) {
                    LOG.debug("Serializer", "Failed to make " + field.getValue().getName() + " accessible, skipping for serialization unless already accessible");
                }
//                boolean _accessible = fieldself.canAccess((EUT) this);
                boolean _finalAfterLoad = fieldself.getAnnotation(IsFinal.class) != null;
//                fieldself.setAccessible(true);

                if (modifiersField != null) {
                    try {
                        modifiersField.setInt(this, fieldModifiers & ~Modifier.FINAL);
                    } catch (IllegalAccessException | IllegalArgumentException e) {
                        LOG.warn("EUT", "Couldn't remove \"final\" modifier, may encounter an issue trying to set the field.");
                    }
                }
                if (isAcc) {
//                    Class fieldType = gr.toType(fieldself.getType());
                    List<String> altNames = sourceLister.getAlternateNames(fieldself.getAnnotationsByType(GoateSource.class), fieldName, sources, dataSource, FLATTEN);//.toLowerCase().replace("_", "."), source.toLowerCase());
                    Object setValue1 = null;
                    String fieldKey = null;
                    boolean flatten = false;
                    for (String altName : altNames) {
                        String[] name = altName.split(FLATTEN);
                        Object setValue1b = getProperty(name[0], null, data);
                        if(fieldKey == null) {
                            fieldKey = name[0];
                            flatten = Boolean.parseBoolean(name[1]);
                        }
                        if (setValue1b != null) {
                            LOG.debug("Properties", "Found a property with the alternate name: " + fieldName + ">" + altName);
                            dupeData.put(name[0], setValue1b);
                            fieldKey = name[0];
                            flatten = Boolean.parseBoolean(name[1]);
                            setValue1 = setValue1b;
                            break;
                        } else if (name[0].isEmpty()) {
                            setValue1 = data;
                        }
                    }
                    try {
                        if (setValue1 != null
                                || data.filter(fieldKey + "\\.").size() > 0
                                || data.getStrict(fieldKey) != null
                                || field.getValue().getType().getAnnotation(GoatePojo.class) != null) {
                            Goate d = null;//new Goate().merge(data, false);
                            if (!flatten) {
                                d = data.filter(fieldKey.replace("##", "[0-9]*"));
                                if (!fieldKey.isEmpty()) {
                                    d = d.scrubKeys(fieldKey + "\\.");
                                }
                            } else {
                                d = data.filterStrict(fieldKey.replace("##", "[0-9]*"));
                            }
                            field.getValue().set(pojo, new Cast(d, dataSource).container(pojo).field(field.getValue()).cast(setValue1, getType(field.getValue())));
                        }
                    } catch (Exception e) {
                        LOG.error("Build Pojo", "Failed to set field: " + e.getMessage(), e);
                    }
//                    if (setValue1 != null) {
//                        try {
//                            fieldself.set(this, setValue1);
//                        } catch (IllegalAccessException e) {
//                            LOG.error("Eut", "Unable to set property \"" + fieldself.getName() + "\" in the eut/config pojo: " + e.getMessage());
//                        }
//                    }
                    if (_finalAfterLoad) {
                        fieldself.setAccessible(false);
                        if (modifiersField != null) {
                            try {
                                modifiersField.setInt(this, fieldModifiers ^ Modifier.FINAL);
                            } catch (IllegalAccessException e) {
                                LOG.warn("Eut", "Unable to make field final.");
                            }
                        }
                    } else {
                        fieldself.setAccessible(acc);
                        if (modifiersField != null) {
                            try {
                                modifiersField.setInt(this, fieldModifiers);
                            } catch (IllegalAccessException e) {
                                LOG.warn("EUT", "Couldn't reset field modifiers: " + e.getMessage());
                            }
                        }
                    }
                }
            }
            if (modifiersField != null) {
                modifiersField.setAccessible(modifiersFieldAccessible);
            }
        }
    }

    private Object buildInstance(Class type) throws IllegalAccessException, InstantiationException {
        if (type == null) {
            LOG.error("Build Pojo", "Can't build the pojo if you don't tell me what to build.");
            throw new RuntimeException("The pojo class was not specified.");
        }
        Object o = null;
        try {
            o = type.getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException | NoSuchMethodException e) {
            LOG.error("Building Pojo", "Problem instantiating new instance: " + e.getMessage(), e);
        }
        if (o instanceof TypeT) {
            ((TypeT) o).setGoateType(genericType);
        }
        return o;
    }

    public DeSerializer data(Goate data) {
        this.data = data;
        return this;
    }

    public DeSerializer T(Class type) {
        return genericType(type);
    }

    public DeSerializer genericType(Class type) {
        this.genericType.add(type);
        return this;
    }

    public DeSerializer data(Map<String, ?> data) {
        return data(new Goate(data));
    }

    public DeSerializer from(Class dataSource) {
        this.dataSource = dataSource;
        return this;
    }
}
