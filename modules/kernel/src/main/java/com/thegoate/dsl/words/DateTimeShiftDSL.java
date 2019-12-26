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
 * Returns the shifted date time as a string.
 * Created by gtque on 3/10/2018.
 */
@GoateDSL(word = "dateTime shift")
@GoateDescription(description = "shifts the dateTime, in the given pattern, by the specified number of weeks(w), days(d), hours(h), minutes(m), seconds(s)",
        parameters = {"the date pattern, both the original date and the calculated date", "the original date", "the number of units (defaults to days) to shift the date", "the time units to shift by: seconds(s), minutes(m), hours(h), days(d), weeks(w), months(n)"})
public class DateTimeShiftDSL extends DateShiftDSL {
    public DateTimeShiftDSL(Object value) {
        super(value);
    }

    public static String dateTimeShift(String pattern, String date, int numberOfDaysToShift){
        return dateTimeShift(pattern, date, numberOfDaysToShift, new Goate());
    }

    public static String dateTimeShift(String pattern, String date, int numberOfDaysToShift, Goate data){
        return dateTimeShift(pattern, date, numberOfDaysToShift, "d", new Goate());
    }

    public static String dateTimeShift(String pattern, String date, int numberToShiftBy, String units) {
        return dateTimeShift(pattern, date, numberToShiftBy, units, new Goate());
    }

    public static String dateTimeShift(String pattern, String date, int numberToShiftBy, String units, Goate data) {
        return ""+new DateTimeShiftDSL("dateTime shift::"+pattern+","+date+","+numberToShiftBy + "," + units)
                .evaluate(data);
    }

    @Override
    public Object evaluate(Goate data) {
        initShift(data);
        LocalDateTime date = LocalDateTime.parse(dateOrigin, dtf);
        date = date.plus(dateShift, shiftUnits);
        String dateShifted = date.format(dtf);
        LOG.debug("Date Shift", "New Date: " + dateShifted);
        return dateShifted;
    }
}
