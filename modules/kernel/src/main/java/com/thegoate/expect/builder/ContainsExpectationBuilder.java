/*
 * Copyright (c) 2017. Eric Angeli
 *
 *  Permission is hereby granted, free of charge,
 *  to any person obtaining a copy of this software
 *  and associated documentation files (the "Software"),
 *  to deal in the Software without restriction,
 *  including without limitation the rights to use, copy,
 *  modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit
 *  persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission
 *  notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 *  AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 */
package com.thegoate.expect.builder;

import com.thegoate.expect.Expectation;

import java.util.List;

/**
 * Created by Eric Angeli on 3/26/2019.
 */
public class ContainsExpectationBuilder extends ExpectationBuilder {

    public ContainsExpectationBuilder lookFor(Object value){
        actualValue(value);
        return this;
    }

    public ContainsExpectationBuilder lookIn(Object value){
        expectedValue(value);
        return this;
    }

    public Expectation buildExpectation(String operator){
        Expectation expectation = Expectation.build()
                .actual(actual)
                .is(operator)
                .expected(expected);
        if(from!=null){
            expectation.from(from);
        }
        if(fromExpected!=null){
            expectation.fromExpected(fromExpected);
        }
        expect(expectation);
        return expectation;
    }

    @Override
    public List<Expectation> build() {
        String operator = "contains";
        if((expected instanceof String && ((""+expected).contains("+")||(""+expected).contains("*")||(""+expected).contains("%")))) {
            boolean invert = false;
            if (actual instanceof String) {
                if (!(("" + actual).contains("+") || ("" + actual).contains("*"))) {
                    operator = "invertedContains";
                    invert = true;
                }
            }
            if(invert){
                Value temp = actualValue;
                actualValue(expectedValue);
                expectedValue(temp);
            }
        }
        expect(buildExpectation(operator));
        return expectations;
    }

}
