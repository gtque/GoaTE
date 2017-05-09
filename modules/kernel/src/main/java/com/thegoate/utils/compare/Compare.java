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
package com.thegoate.utils.compare;

import com.thegoate.utils.UnknownUtilType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic sompare service.
 * Created by Eric Angeli on 5/9/2017.
 */
public class Compare extends UnknownUtilType implements CompareUtility {
    final Logger LOG = LoggerFactory.getLogger(getClass());
    CompareUtility tool = null;
    Object actual = null;
    Object operator = null;
    Object expected = null;

    public Compare(Object actual){
        this.actual = actual;
    }

    @Override
    public boolean isType(Object check) {
        return false;
    }

    @Override
    public boolean evaluate() {
        try {
            tool = (CompareUtility) buildUtil(actual, CompareUtil.class, ""+operator, CompareUtil.class.getMethod("operator"));
        } catch (NoSuchMethodException e) {
            LOG.error("Problem finding the compare utility: " + e.getMessage(), e);
        }
        tool.to(expected).using(operator);
        return tool.evaluate();
    }

    @Override
    public CompareUtility to(Object expected) {
        this.expected = expected;
        return this;
    }

    @Override
    public CompareUtility using(Object operator) {
        this.operator = operator;
        return this;
    }
}
