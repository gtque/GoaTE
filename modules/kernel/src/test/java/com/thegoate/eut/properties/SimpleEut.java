package com.thegoate.eut.properties;

import com.thegoate.eut.Eut;
import com.thegoate.utils.fill.serialize.GoateSource;

@GoateSource(key = "something", flatten = true)
@GoateSource(key = "first", priority = 1)
@GoateSource(key = "third", priority = 3)
@GoateSource(key = "second", priority = 2)
@GoateSource(key = "")
@GoateSource(key = "threeB", priority = 3)
public class SimpleEut extends Eut<SimpleEut> {

    public static final SimpleEut eut = load(SimpleEut.class);

    @GoateSource(key = "field.a", priority = 1)
    @GoateSource(key = "just_a_name", flatten = true, priority = 3)
    @GoateSource(key = "some.field.a", priority = 2)
    @GoateSource(key = "theField", priority = 1)
    public String FIELD_A = "Fuzzy Wuzzy was a bear";

    @Override
    public SimpleEut eut() {
        return eut;
    }
}
