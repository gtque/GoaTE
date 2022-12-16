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
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * Returns the shifted date as a string.
 * Created by gtque on 3/10/2018.
 */
@GoateDSL(word = "date shift")
@GoateDescription(description = "shifts the date, in the given pattern, by the specified number of units (defaults to days)",
        parameters = {"the date pattern, both the original date and the calculated date", "the original date", "the number of days to shift the date", "the time units to shift by: seconds(s), minutes(m), hours(h), days(d), weeks(w), months(n)"})
public class DateShiftDSL extends DSL {
    public DateShiftDSL(Object value) {
        super(value);
    }
    protected String datePattern;
    protected DateTimeFormatter dtf;
    protected String dateOrigin;
    protected long dateShift;
    protected TemporalUnit shiftUnits;

    public static String dateShift(String pattern, String date, int numberOfDaysToShift){
        return dateShift(pattern, date, numberOfDaysToShift, "d", new Goate());
    }

    public static String dateShift(String pattern, String date, int numberOfDaysToShift, Goate data) {
        return dateShift(pattern, date, numberOfDaysToShift, "d", data);
    }

    public static String dateShift(String pattern, String date, int numberToShiftBy, String units) {
        return dateShift(pattern, date, numberToShiftBy, units, new Goate());
    }

    public static String dateShift(String pattern, String date, int numberToShiftBy, String units, Goate data) {
        return ""+new DateShiftDSL("date shift::"+pattern+","+date+","+numberToShiftBy + "," + units)
                .evaluate(data);
    }

    @Override
    public Object evaluate(Goate data) {
        initShift(data);
        LocalDate date = LocalDate.parse(dateOrigin, dtf);
        date = date.plus(dateShift, shiftUnits);
        String dateShifted = date.format(dtf);
        LOG.debug("Date Shift", "New Date: " + dateShifted);
        return dateShifted;
    }

    protected void initShift(Goate data){
        datePattern = "" + get(1, data);
        dtf = DateTimeFormatter.ofPattern(datePattern);
        LOG.debug("Date Shift", "Pattern: " + datePattern);
        dateOrigin = "" + get(2, data);
        LOG.debug("Date Shift", "Original Date: " + dateOrigin);
        dateShift = Long.parseLong("" + get(3, data));
        Object unit = get(4, data);
        String units = unit==null?"d":(""+unit);
        shiftUnits = findTemporalUnit(units);
    }

    protected TemporalUnit findTemporalUnit(String unit){
        TemporalUnit u;
        switch(unit){
            case "s":
            case "seconds":
                u = ChronoUnit.SECONDS;
                break;
            case "m":
            case "minutes":
                u = ChronoUnit.MINUTES;
                break;
            case "h":
            case "hours":
                u = ChronoUnit.HOURS;
                break;
            case "d":
            case "days":
                u = ChronoUnit.DAYS;
                break;
            case "w":
            case "weeks":
                u = ChronoUnit.WEEKS;
                break;
            case "n":
            case "months":
                u = ChronoUnit.MONTHS;
                break;
            default:
                u = ChronoUnit.DAYS;
        }
        return u;
    }
}
