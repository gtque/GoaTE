package com.thegoate.utils.fill.serialize.automap.pojos;

import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.Kid;
import com.thegoate.utils.fill.serialize.collections.ListType;

import java.util.List;

public class InnerListKid extends Kid {

    @ListType(type = InnerListItemKid.class)
    @GoateSource(key = "offspring", source = Offspring.class)
    @GoateSource(key = "children", source = Children.class)
    public List<InnerListItemKid> kids;
}
