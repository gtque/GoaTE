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

package com.thegoate.dsl.words;

import com.thegoate.Goate;
import com.thegoate.annotations.GoateDescription;
import com.thegoate.dsl.DSL;
import com.thegoate.dsl.GoateDSL;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Returns the value of the referenced object.
 * Created by gtque on 3/10/2018.
 */
@GoateDSL(word = "date")
@GoateDescription(description = "returns current (local) date time in the given format.",
        parameters = {"the format for the date"})
public class DateCurrentDSL extends DSL {
    public DateCurrentDSL(Object value) {
        super(value);
    }

    public static String date(String format) {
        return date(format, new Goate());
    }

    public static String date(String format, Goate data){
        return ""+new DateCurrentDSL("date::"+format).evaluate(data);
    }

    @Override
    public Object evaluate(Goate data) {
        String datePattern = "" + get(1, data);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(datePattern);
        LOG.debug("Date Reformat", "Pattern: " + datePattern);
        LocalDateTime date = LocalDateTime.now();
        return date.format(dtf);
    }
}
