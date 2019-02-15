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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Returns the formated date as a string.
 * Created by gtque on 3/10/2018.
 */
@GoateDSL(word = "date reformat")
@GoateDescription(description = "reformats the given date format.",
        parameters = {"the original date pattern", "the date string", "the new format"})
public class DateReformatDSL extends DSL {
    public DateReformatDSL(Object value) {
        super(value);
    }

    public static String dateReformat(String originalPattern, String date, String newPattern){
        return dateReformat(originalPattern, date, newPattern, new Goate());
    }

    public static String dateReformat(String originalPattern, String date, String newPattern, Goate data){
        return ""+new DateReformatDSL("date reformat::"+originalPattern+","+date+","+newPattern).evaluate(data);
    }

    @Override
    public Object evaluate(Goate data) {
        String datePattern = "" + get(1, data);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(datePattern);
        LOG.debug("Date Reformat", "Pattern: " + datePattern);
        String dateOrigin = "" + get(2, data);
        LOG.debug("Date Reformat", "Date: " + dateOrigin);
        String datePattern2 = "" + get(3, data);
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern(datePattern2);
        LocalDate date = LocalDate.parse(dateOrigin, dtf);
        String dateShifted = date.format(dtf2);
        LOG.debug("Date Reformat", "New Date: " + dateShifted);
        return dateShifted;
    }
}
