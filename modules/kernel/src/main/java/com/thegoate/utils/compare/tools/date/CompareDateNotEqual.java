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

import com.thegoate.utils.compare.CompareUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Checks if a date is not equal expected date.
 * Created by Eric Angeli on 7/7/2017.
 */
@CompareUtil(operator = "!=", type = Date.class)
public class CompareDateNotEqual extends CompareDate {
    public CompareDateNotEqual(Object actual) {
        super(actual);
    }

    @Override
    public boolean evaluate() {
        String exp = "" + expected;
        String act = "" + actual;
        boolean result;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date da = sdf.parse(parse(act));
            Date de = expected!=null?sdf.parse(parse(exp)):null;
            result = de==null?da!=null:da.compareTo(de)!=0;
        }catch (Throwable t){
            result = false;
        }
        return result;
    }
}
