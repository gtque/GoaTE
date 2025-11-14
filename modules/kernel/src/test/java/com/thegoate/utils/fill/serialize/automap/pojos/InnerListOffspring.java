package com.thegoate.utils.fill.serialize.automap.pojos;

import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.Kid;

import java.util.List;

public class InnerListOffspring extends Kid {

    @GoateSource(key = "offspring", source = Offspring.class)
    public List<InnerListItemOffspring> kids;
}
