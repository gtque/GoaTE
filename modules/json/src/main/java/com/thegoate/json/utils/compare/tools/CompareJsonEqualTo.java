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
package com.thegoate.json.utils.compare.tools;

import com.thegoate.utils.compare.CompareUtil;
import com.thegoate.utils.compare.CompareUtility;

/**
 * Compares two json objects for equality.
 * Created by Eric Angeli on 5/9/2017.
 */
@CompareUtil(operator = "==", type = "json")
public class CompareJsonEqualTo extends CompareJson implements CompareUtility {

    Object expected = null;

    public CompareJsonEqualTo(Object actual) {
        super(actual);
    }

    @Override
    protected Object processNested(Object subContainer) {
        return null;
    }

    @Override
    public boolean evaluate() {
        return comparison(""+takeActionOn,""+expected)==0;//takeActionOn!=null?takeActionOn.equals(expected):expected==null;
    }

    @Override
    public CompareUtility actual(Object actual) {
        this.takeActionOn = actual;
        return this;
    }

    @Override
    public CompareUtility to(Object expected) {
        this.expected = expected;
        return this;
    }

    @Override
    public CompareUtility using(Object operator) {
        return this;
    }
}
