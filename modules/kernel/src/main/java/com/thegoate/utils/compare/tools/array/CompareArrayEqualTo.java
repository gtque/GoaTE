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
package com.thegoate.utils.compare.tools.array;

import com.thegoate.utils.compare.Compare;
import com.thegoate.utils.compare.CompareTool;
import com.thegoate.utils.compare.CompareUtil;

import java.lang.reflect.Array;

/**
 * Checks if an array equals another array, order enforced.
 * Created by Eric Angeli on 03/01/2022.
 */
@CompareUtil(operator = "==", type = Array.class)
public class CompareArrayEqualTo extends CompareTool {
    public CompareArrayEqualTo(Object actual) {
        super(actual);
    }

    @Override
    public boolean evaluate() {
        int actLength = Array.getLength(actual);
        int expLength = Array.getLength(expected);
        boolean result = true;
        if(actLength == expLength) {
            result = checkArrays(actLength, expLength, actual, expected) && checkArrays(expLength, actLength, expected, actual);
        } else {
            result = false;
        }
        return result;
    }

    private boolean checkArrays(int actLength, int expLength, Object act, Object exp){
        boolean result = true;
        for (int i = 0; i < actLength && result; i++) {
            Object actO = Array.get(act, i);
            try {
                if (!new Compare(actO).to(Array.get(exp, i)).using("==").evaluate()) {
                    result = false;
                }
            } catch (Exception e){
                healthCheck().put("exception", e.getMessage());
                result = false;
            }
        }
        return result;
    }

    @Override
    public boolean isType(Object check) {
        return check != null && check.getClass().isArray();
    }
}
