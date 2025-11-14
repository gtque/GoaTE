package com.thegoate.utils.fill.serialize.automap.pojos;

import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.Kid;

public class ComplexKid extends Kid {

    @GoateSource(key = "theTruth", source = Offspring.class)
    @GoateSource(key = "word", source = Children.class)
    public boolean theTruth;

    public InnerListKid theKids;
}
