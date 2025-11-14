package com.thegoate.utils.fill.serialize.automap.pojos;

import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.Kid;

public class InnerListItemKid extends Kid {

    @GoateSource(key = "label", source = Children.class)
    public String name;
}
