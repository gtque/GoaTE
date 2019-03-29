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

import com.thegoate.utils.compare.CompareTool;
import com.thegoate.utils.compare.CompareUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Adds check for date type.
 * Just checks date, ignores time, if time is delimited by T.
 * Assumes date in the format of yyyy-MM-dd
 * Probably need some what to specify the format some how.
 * Created by Eric Angeli on 2019/03/12.
 */
public abstract class CompareDate extends CompareTool {
    protected String format = "yyyy-MM-dd";
    public CompareDate(Object actual) {
        super(actual);
    }

    @Override
    public boolean isType(Object check) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        boolean result = true;
        try {
            String d = parse("" + check);
            sdf.parse(d);
        }catch (Throwable t){
            result = false;
        }
        return result;
    }

    protected String parse(String d){
        int t = d.indexOf("T");
        if(t<0){
            t = d.length();
        }
        return d.substring(0,t);
    }
}
