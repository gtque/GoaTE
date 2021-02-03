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
import com.thegoate.dsl.GoateDSL;

import java.time.LocalDateTime;

/**
 * Returns the formatted date time as a string.
 * Created by gtque on 8/8/2019.
 */
@GoateDSL(word = "dateTime reformat")
@GoateDescription(description = "reformats the given date/time format.",
        parameters = {"the original date/time pattern", "the date/time string", "the new format"})
public class DateTimeReformatDSL extends DateReformatDSL {
    public DateTimeReformatDSL(Object value) {
        super(value);
    }

    public static String dateTimeReformat(String originalPattern, String date, String newPattern){
        return dateTimeReformat(originalPattern, date, newPattern, new Goate());
    }

    public static String dateTimeReformat(String originalPattern, String date, String newPattern, Goate data){
        return ""+new DateTimeReformatDSL("dateTime reformat::"+originalPattern+","+date+","+newPattern).evaluate(data);
    }

    @Override
    public Object evaluate(Goate data) {
        initFormat(data);
        LocalDateTime date = LocalDateTime.parse(dateOrigin, dtf);
        String dateFormatted = date.format(dtf2);
        LOG.debug("DateTime Reformat", "New Date/Time: " + dateFormatted);
        return dateFormatted;
    }
}
