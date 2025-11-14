package com.thegoate.utils.fill.serialize;

import com.thegoate.Goate;

public class AutoMap {

    Object source;
    Class dataSource;

    public AutoMap(Object source) {
        this.source = source;
    }

    public AutoMap dataSource(Class dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public <T> T to(Class<T> type) {
        Goate data = new Serializer(source, source.getClass()).detailed(true).strict().includeNulls().toGoate();
        return new DeSerializer()
                .data(data)
                .from(dataSource)
                .build(type);
    }
}
