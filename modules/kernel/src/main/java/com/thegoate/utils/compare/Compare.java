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

import com.thegoate.Goate;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.utils.UnknownUtilType;


/**
 * Generic sompare service.
 * Created by Eric Angeli on 5/9/2017.
 */
public class Compare extends UnknownUtilType implements CompareUtility {
    final BleatBox LOG = BleatFactory.getLogger(getClass());
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
    public Goate healthCheck(){
        return tool!=null?tool.healthCheck():new Goate();
    }
    @Override
    public boolean evaluate() {
        if(tool==null){
            buildTool();
        }
        boolean result = false;
        try {
            tool.to(expected).using(operator);
            result = tool.evaluate();
        }catch (Exception e){
            LOG.debug("Compare", "Failed to compare: " + e.getMessage(), e);
        }
        return result;
    }

    protected void buildTool(){
        try {
            tool = (CompareUtility) buildUtil(actual, CompareUtil.class, ""+operator, CompareUtil.class.getMethod("operator"));
        } catch (NoSuchMethodException e) {
            LOG.error("Problem finding the compare utility: " + e.getMessage(), e);
        }
    }
    public CompareUtility getTool(){
        if(tool==null){
            buildTool();
        }
        return tool;
    }

    @Override
    public CompareUtility actual(Object actual){
        this.actual = actual;
        return this;
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
