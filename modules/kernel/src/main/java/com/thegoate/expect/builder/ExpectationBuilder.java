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
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.utils.get.Get;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric Angeli on 3/26/2019.
 */
public abstract class ExpectationBuilder<Self extends ExpectationBuilder> {
    BleatBox LOG = BleatFactory.getLogger(getClass());
    protected Object from;
    protected Object fromExpected;
    protected Object actual;
    protected Object expected;
    private List<Expectation> expectations = new ArrayList<>();
    protected Value actualValue;
    protected Value expectedValue;
    protected long period = -42;
    protected long timeoutMS = -42;
//    public List<Expectation> build(Expectation expectation){
//        Goate exp = (Goate)expectation.getExpectations().get(0);
//        setFrom(expectation.getFrom());
//        setActual(exp.get("actual"));
//        return build();
//    }

    public abstract List<Expectation> build();

    protected Self expect(Expectation expectation) {
        expectations.add(expectation.retryTimeout(timeoutMS).retryPeriod(period));
        return (Self)this;
    }

    protected Self expect(ExpectationBuilder builder){
        if(builder != null){
            expect(builder.build());
        }
        return (Self)this;
    }

    protected Self expect(List<Expectation> expectations){
        if(expectations!= null && expectations.size()>0){
            expectations.stream().forEach(expectation -> expect(expectation));
        }
        return (Self)this;
    }

    protected List<Expectation> getExpectations() {
        return expectations;
    }

    public Self actualValue(Object value) {
        if(value instanceof Value) {
            this.actualValue = (Value)value;
            if (value != null) {
                this.setActual(this.actualValue.getLocator());
                this.setFrom(this.actualValue.getContainer());
            }
        } else {
            setActual(value);
        }
        return (Self)this;
    }

    public Self expectedValue(Object value) {
        if(value instanceof Value) {
            this.expectedValue = (Value)value;
            if (value != null) {
                this.setExpected(this.expectedValue.getLocator());
                this.setFromExpected(this.expectedValue.getContainer());
            }
        } else {
            setExpected(value);
        }
        return (Self)this;
    }

    public Self timeout(long timeoutMS){
        this.timeoutMS = timeoutMS;
        return (Self)this;
    }

    public Self period(long period){
        this.period = period;
        return (Self)this;
    }

    public Object getFrom() {
        return from;
    }

    public Self setFrom(Object from) {
        if(actualValue == null){
            actualValue = new Value().from(from);
        }
        this.from = from;
        return (Self)this;
    }

    public Object getFromExpected() {
        return fromExpected;
    }

    public Self setFromExpected(Object fromExpected) {
        if(expectedValue == null){
            expectedValue = new Value().from(fromExpected);
        }
        this.fromExpected = fromExpected;
        return (Self)this;
    }

    public Object getActual() {
        Object a = actual;
        if (from != null) {
            if (!("" + actual).contains("*") && !("" + actual).contains("+")) {
                a = new Get(actual).from(from);
            } else {
                a = from;
            }
        }
        return a;
    }

    public Self setActual(Object actual) {
        if(actualValue == null){
            actualValue = new Value(actual);
        }
        this.actual = actual;
        return (Self)this;
    }

    public Object getExpected() {
        return expected;
    }

    public Self setExpected(Object expected) {
        if(expectedValue == null){
            expectedValue = new Value(expected);
        }
        this.expected = expected;
        return (Self)this;
    }
}
