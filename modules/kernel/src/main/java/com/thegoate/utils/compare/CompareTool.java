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
 * Base class for Compare Utilities. Adds logger, and class level variables.
 * It is recommend that Compare utilities extend this class, but they really only
 * need to implement the CompareUtility interface.
 * Created by Eric Angeli on 5/5/2017.
 */
public abstract class CompareTool implements CompareUtility{
    protected final BleatBox LOG = BleatFactory.getLogger(getClass());
    protected Object actual = null;
    protected Object expected = null;
    protected Object operator = null;
    protected boolean nested = false;
    protected Goate health = new Goate();
    protected Goate data;

    public CompareTool(Object actual){
        this.actual = actual;
    }

    public CompareTool nested(){
        this.nested = true;
        return this;
    }

    public boolean isNested(){
        return nested;
    }

    protected boolean tryExpectedType(String op){
        boolean result = false;
        if(!isNested()) {
            Compare compare = new Compare(expected);
            compare.using(op).to(actual);
            CompareUtility cu = compare.getTool();
            if (cu instanceof CompareTool) {
                ((CompareTool) cu).nested();
            }
            result = compare.actual(actual).to(expected).evaluate();
        }
        return result;
    }

    @Override
    public CompareTool setData(Goate data){
        this.data = data;
        return this;
    }

    @Override
    public CompareUtility actual(Object actual){
        this.actual = actual;
        return this;
    }

    @Override
    public CompareUtility to(Object expected){
        this.expected = expected;
        return this;
    }
    @Override
    public CompareUtility using(Object operator){
        this.operator = operator;
        return this;
    }

    @Override
    public Goate healthCheck(){
        return health;
    }
}
