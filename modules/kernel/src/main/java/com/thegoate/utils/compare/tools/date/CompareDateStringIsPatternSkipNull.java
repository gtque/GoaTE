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
package com.thegoate.utils.compare.tools.date;

import com.thegoate.annotations.IsDefault;
import com.thegoate.utils.compare.CompareTool;
import com.thegoate.utils.compare.CompareUtil;

/**
 * Checks if a string is empty or not.
 * Returns true only if actual is not null and is empty and if expected is true.
 * Returns true if actual is not empty or null and if expected is false.
 * Use == null or != null to check for null.
 * example: field1,==,null::
 * Created by Eric Angeli on 7/7/2017.
 */
@CompareUtil(operator = "dateIsPatternSkipNull", type = "String")
@IsDefault
public class CompareDateStringIsPatternSkipNull extends CompareTool {
    public CompareDateStringIsPatternSkipNull(Object actual) {
        super(actual);
    }

    @Override
    public boolean evaluate() {
        String act = "" + actual;
        boolean result = true;
        if(actual!=null&&!act.equals("null")&&!act.trim().isEmpty()){
            result = new CompareDateStringIsPattern(actual).to(expected).evaluate();
        }
        return result;
    }

    @Override
    public boolean isType(Object check) {
        return true;
    }
}
