package com.thegoate.utils.fill.serialize.pojos;

import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.Kid;
import com.thegoate.utils.fill.serialize.collections.ListType;

import java.util.List;

public class SerializeNested extends Kid {

    @ListType(type = Cereals.class)
    @GoateSource(source = SimpleSource.class, key = "names")
    @GoateSource(source = RootSource.class, key = "")
    private List<Cereals> ids;

    public List<Cereals> getIds() {
        return ids;
    }

    public SerializeNested setIds(List<Cereals> ids) {
        this.ids = ids;
        return this;
    }
}
