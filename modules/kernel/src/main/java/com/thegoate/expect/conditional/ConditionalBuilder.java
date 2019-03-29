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
package com.thegoate.expect.conditional;

import com.thegoate.expect.Expectation;
import com.thegoate.utils.get.Get;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric Angeli on 3/26/2019.
 */
public abstract class ConditionalBuilder {
    protected Object from;
    protected Object fromExpected;
    protected Object actual;
    protected Object expected;
    protected List<Expectation> expectations = new ArrayList<>();

//    public List<Expectation> build(Expectation expectation){
//        Goate exp = (Goate)expectation.getExpectations().get(0);
//        setFrom(expectation.getFrom());
//        setActual(exp.get("actual"));
//        return build();
//    }

    public abstract List<Expectation> build();

    protected ConditionalBuilder expect(Expectation expectation){
        expectations.add(expectation);
        return this;
    }

    public Object getFrom() {
        return from;
    }

    public ConditionalBuilder setFrom(Object from) {
        this.from = from;
        return this;
    }

    public Object getFromExpected() {
        return fromExpected;
    }

    public ConditionalBuilder setFromExpected(Object fromExpected) {
        this.fromExpected = fromExpected;
        return this;
    }

    public Object getActual() {
        Object a = actual;
        if(from!=null){
            a = new Get(actual).from(from);
        }
        return a;
    }

    public ConditionalBuilder setActual(Object actual) {
        this.actual = actual;
        return this;
    }

    public Object getExpected() {
        return expected;
    }

    public ConditionalBuilder setExpected(Object expected) {
        this.expected = expected;
        return this;
    }
}
