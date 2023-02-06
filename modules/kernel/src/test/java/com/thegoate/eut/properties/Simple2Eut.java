package com.thegoate.eut.properties;

import com.thegoate.eut.Eut;

public class Simple2Eut extends Eut<Simple2Eut> {

    public static final Simple2Eut eut = load(Simple2Eut.class);

    public String FIELD_A = "Fuzzy Wuzzy had no hair";

    @Override
    public Simple2Eut eut() {
        return eut;
    }
}
