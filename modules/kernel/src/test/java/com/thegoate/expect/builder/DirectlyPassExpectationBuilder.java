package com.thegoate.expect.builder;

import com.thegoate.expect.Expectation;

import java.util.List;

public class DirectlyPassExpectationBuilder extends ExpectationBuilder<DirectlyPassExpectationBuilder>{
    @Override
    public List<Expectation> build() {
        expect(new OneTwoThreeFourExpectationBuilder());
        expect(Expectation.build()
                .actual(5)
                .isEqualTo(5));
        return getExpectations();
    }
}
