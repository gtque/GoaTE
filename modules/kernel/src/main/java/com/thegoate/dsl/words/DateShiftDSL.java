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
 * Returns the shifted date as a string.
 * Created by gtque on 3/10/2018.
 */
@GoateDSL(word = "date shift")
@GoateDescription(description = "shifts the date, in the given pattern, by the specified number of days",
        parameters = {"the date pattern, both the original date and the calculated date", "the original date", "the number of days to shift the date"})
public class DateShiftDSL extends DSL {
    public DateShiftDSL(Object value) {
        super(value);
    }

    public static String dateShift(String pattern, String date, int numberOfDaysToShift){
        return dateShift(pattern, date, numberOfDaysToShift, new Goate());
    }

    public static String dateShift(String pattern, String date, int numberOfDaysToShift, Goate data){
        return ""+new DateReformatDSL("date shift::"+pattern+","+date+","+numberOfDaysToShift).evaluate(data);
    }

    @Override
    public Object evaluate(Goate data) {
        String datePattern = "" + get(1, data);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(datePattern);
        LOG.debug("Date Shift", "Pattern: " + datePattern);
        String dateOrigin = "" + get(2, data);
        LOG.debug("Date Shift", "Original Date: " + dateOrigin);
        long dateShift = Long.parseLong("" + get(3, data));
        LocalDate date = LocalDate.parse(dateOrigin, dtf);
        date = date.plusDays(dateShift);
        String dateShifted = date.format(dtf);
        LOG.debug("Date Shift", "New Date: " + dateShifted);
        return dateShifted;
    }
}
