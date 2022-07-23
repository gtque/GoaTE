package com.thegoate.utils.fill.serialize.model;

import com.thegoate.annotations.AnnotationFactory;
import com.thegoate.reflection.GoateReflection;
import com.thegoate.utils.fill.serialize.Cereal;
import com.thegoate.utils.fill.serialize.DefaultSource;
import com.thegoate.utils.fill.serialize.Serializer;
import com.thegoate.utils.fill.serialize.collections.ListType;
import com.thegoate.utils.fill.serialize.collections.MapKeyType;
import com.thegoate.utils.fill.serialize.collections.MapType;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ModelBuilder<M, T> extends Cereal {
    Class klass;
    Class source = DefaultSource.class;
    private List<Class> genericType = new ArrayList<>();

    public ModelBuilder(Class klass) {
        this.klass = klass;
    }

    public ModelBuilder<M, T> source(Class source) {
        this.source = source;
        return this;
    }

    public ModelBuilder<M, T> T(Class varType) {
        genericType.add(varType);
        return this;
    }

    public T build(Class blueprint) {
        T model = null;
        M definition = (M) assemble(klass);
        if (definition != null) {
            model = (T) new Serializer<>(definition, source).includeNulls().asSourced(true).to(blueprint);
        }
        return model;
    }

    private <A> A assemble(Class<A> klass) {
        A definition = null;
        try {
            definition = (A) new AnnotationFactory().constructor(null).build(klass);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            LOG.debug("Problem instantiating the pojo, while try casting instead (" + klass.getName() + "): " + e.getMessage(), e);
            try {
                definition = (A) doCast(definition, Object.class, klass);
            } catch (InstantiationException | IllegalAccessException castException) {
                LOG.error("failed to even cast to pojo, Goate cannot build a model of this properly: " + klass.getName(), castException);
            }
        }
        GoateReflection gr = new GoateReflection();
        if (definition == null) {
            definition = (A) gr.getDefaultPrimitive(klass);
        }
        if (definition != null) {
            if(!gr.isPrimitive(klass)) {
                Map<String, Field> fields = gr.findFields(klass);
                for (Map.Entry<String, Field> field : fields.entrySet()) {
                    Object value = gr.getFieldValue(definition, field.getValue());
                    if (value == null) {
                        boolean accessible = field.getValue().canAccess(definition);//.isAccessible();
                        field.getValue().setAccessible(true);
                        try {
                            if (gr.classIsNumber(field.getValue().getType())) {
                                field.getValue().set(definition, doCast("0", field.getValue().getType()));
                            } else if (gr.isBoolean(value)) {
                                field.getValue().set(definition, true);
                            } else if (gr.isPrimitiveOrNumerical(value)) {
                                field.getValue().set(value, doCast("", field.getValue().getType()));
                            } else if (field.getValue().getType().isArray()) { //check if array before checking if collection.
                                field.getValue().set(definition, buildArray(field.getValue(), field.getValue().getType()));
                            } else if (gr.isCollectionOrMap(field.getValue())) {
                                field.getValue().set(definition, buildCollection(field.getValue(), field.getValue().getType()));
                            } else {
                                Object val = assemble(field.getValue().getType());
                                field.getValue().set(definition, val);
                            }
                        } catch (IllegalAccessException | InstantiationException e) {
                            LOG.debug("problem modeling field: " + field.getKey() + ". " + e.getMessage(), e);
                        }
                        field.getValue().setAccessible(accessible);
                    }
                }
            }
        }
        return definition;
    }

    private <Z> Z buildCollection(Field field, Class<Z> zType) {
        Z collection = null;
        if (Collection.class.isAssignableFrom(field.getType())
                || Set.class.isAssignableFrom(field.getType())) {
            ListType type = field.getAnnotation(ListType.class);
            List<Object> list = new ArrayList<>();
            if (type != null) {
                list.add(assemble(type.type()));
            }
            collection = (Z) list;
        } else if (Map.class.isAssignableFrom(field.getType())) {
            Map<Object, Object> map = new HashMap<>();
            Object key = "key";
            MapKeyType mapKeyType = field.getAnnotation(MapKeyType.class);
            if (mapKeyType != null) {
                key = assemble(mapKeyType.type());
                if(key instanceof String && (key == null || ((String) key).isEmpty())) {
                    key = "key";
                }
            }
            MapType MapType = field.getAnnotation(MapType.class);
            if (MapType != null) {
                map.put(key, assemble(MapType.type()));
            }
            collection = (Z) map;
        }
        return collection;
    }

    private <Z> Z buildArray(Field field, Class<Z> zType) {
        Z collection = null;
        if (field.getType().isArray()) {
            Object v = assemble(field.getType().getComponentType());
            Object a = Array.newInstance(field.getType().getComponentType(), 1);
            Arrays.fill((Object[])a, v);
            collection = (Z) a;
        }
        return zType.cast(collection);
    }
}