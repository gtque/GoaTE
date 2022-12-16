package com.thegoate.expect.builder;

import com.thegoate.expect.Expectation;

import java.util.List;

public class OneTwoThreeFourExpectationBuilder extends ExpectationBuilder<OneTwoThreeFourExpectationBuilder> {

    @Override
    public List<Expectation> build() {
        expect(Expectation.build()
                .actual(1)
                .isEqualTo(1));
        expect(Expectation.build()
                .actual(2)
                .isEqualTo(2));
        expect(Expectation.build()
                .actual(3)
                .isEqualTo(3));
        expect(Expectation.build()
                .actual(4)
                .isEqualTo(4));
        return getExpectations();
    }
}
