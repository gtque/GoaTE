package com.thegoate.expect.builder;

import com.thegoate.expect.Expectation;

import java.util.List;

public class PassAlreadyBuiltListExpectationBuilder extends ExpectationBuilder<PassAlreadyBuiltListExpectationBuilder>{
    @Override
    public List<Expectation> build() {
        expect(new OneTwoThreeFourExpectationBuilder().build());
        expect(Expectation.build()
                .actual(5)
                .isEqualTo(5));
        return getExpectations();
    }
}
