package com.thegoate.utils.fill.serialize.pojos;

import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.Kid;
import com.thegoate.utils.fill.serialize.collections.ListType;
import com.thegoate.utils.fill.serialize.collections.MapKeyType;
import com.thegoate.utils.fill.serialize.collections.MapType;

import java.util.List;
import java.util.Map;

public class SerializeMe extends Kid {

    @GoateSource(source = SimpleSource.class, key = "athan")
    private String field;

    @GoateSource(source = SimpleSource.class, key = "ex", skipInModel = true)
    @GoateSource(key = "excluded", skipInModel = true)
    private String excluded;

    @GoateSource(source = SimpleSource.class, key = "parent")
    private SerializeLevel1 nested;

    @GoateSource(source = SimpleSource.class, key = "parentEx", skipInModel = true)
    @GoateSource(key = "nestedExcluded", skipInModel = true)
    private SerializeLevel1 nestedExcluded;

    @ListType(type = SerializeLevel1.class)
    @GoateSource(source = SimpleSource.class, key = "rents")
    private List<SerializeLevel1> list;

    @MapType(type = SerializeLevel1.class)
    @MapKeyType(type = String.class)
    private Map<String, SerializeLevel1> map;

    public String getField() {
        return field;
    }

    public SerializeMe setField(String field) {
        this.field = field;
        return this;
    }

    public SerializeLevel1 getNested() {
        return nested;
    }

    public SerializeMe setNested(SerializeLevel1 nested) {
        this.nested = nested;
        return this;
    }

    public List<SerializeLevel1> getList() {
        return list;
    }

    public SerializeMe setList(List<SerializeLevel1> list) {
        this.list = list;
        return this;
    }

    public Map<String, SerializeLevel1> getMap() {
        return map;
    }

    public SerializeMe setMap(Map<String, SerializeLevel1> map) {
        this.map = map;
        return this;
    }

    public String getExcluded() {
        return excluded;
    }

    public SerializeMe setExcluded(String excluded) {
        this.excluded = excluded;
        return this;
    }

    public SerializeLevel1 getNestedExcluded() {
        return nestedExcluded;
    }

    public SerializeMe setNestedExcluded(SerializeLevel1 nestedExcluded) {
        this.nestedExcluded = nestedExcluded;
        return this;
    }
}
