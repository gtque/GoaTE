package com.thegoate.expect.builder;

import com.thegoate.expect.Expectation;
import com.thegoate.utils.fill.serialize.Kid;

import java.util.ArrayList;
import java.util.List;

public class KidIsEqualIgnoreFieldsBuilder<Self extends KidIsEqualIgnoreFieldsBuilder> extends ExpectationBuilder<Self> {

    private List<String> fieldNames = new ArrayList<>();

    public Self ignoreField(String fieldName) {
        fieldNames.add(fieldName);
        return (Self) this;
    }

    @Override
    public List<Expectation> build() {
        if (actual instanceof Kid && expected instanceof Kid) {
            if (fieldNames.size() > 0) {
                if (actual != null) {
                    try {
                        actual = ((Kid) actual).cloneButIgnore(fieldNames);
                    } catch (CloneNotSupportedException e) {
                        LOG.error("Trying to Ignore Fields", "Problem cloning and ignore: " + e.getMessage(), e);
                    }
                }
                if (expected != null) {
                    try {
                        expected = ((Kid) expected).cloneButIgnore(fieldNames);
                    } catch (CloneNotSupportedException e) {
                        LOG.error("Trying to Ignore Fields", "Problem cloning and ignore: " + e.getMessage(), e);
                    }
                }
            }
        }
        expect(Expectation.build()
                .actual(actual)
                .isEqualTo(expected));
        return getExpectations();
    }
}
