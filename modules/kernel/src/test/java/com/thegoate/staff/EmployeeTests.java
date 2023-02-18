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

package com.thegoate.staff;

import com.thegoate.Goate;
import com.thegoate.metrics.Stopwatch;
import com.thegoate.staff.test.EventuallyTrueInt;
import com.thegoate.testng.TestNGEngineMethodDL;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by gtque on 4/25/2017.
 */
public class EmployeeTests extends TestNGEngineMethodDL {

    @Test(groups = {"unit"})
    public void simpleEmployeeSize() {
        Stopwatch.global.clearAllLaps();
        Goate data = new Goate().put("val##", 1).put("val##", 2).put("val##", 3).put("lap", "simpleEmployeeSize");
        int r = Integer.parseInt("" + Employee.recruit("t_size", data).work());
        assertEquals(r, 4);
        Stopwatch.global.stop("simpleEmployeeSize");
        Stopwatch.global.stopAll();
        LOG.info("Simple Employee Size", "Lap time: " + Stopwatch.global.lap("simpleEmployeeSize").getTime() / 1000000L);
        assertEquals(Stopwatch.global.lap("simpleEmployeeSize").splits().size(), 1);
    }

    @Test(groups = {"unit"})
    public void simpleEmployeeLength() {
        Goate data = new Goate().put("val##", 1).put("val##", 2).put("val##", 3);
        int r = Integer.parseInt("" + Employee.recruit("t_length", data).work());
        assertEquals(r, 3);
    }

    @Test(groups = {"unit"})
    public void simpleEmployeeLengthNull() {
        Goate data = null;
        int r = Integer.parseInt("" + Employee.recruit("t_length", data).work());
        assertEquals(r, -1);
    }

    @Test(groups = {"unit"})
    public void untilConditionMet() {
        Goate data = new Goate();
        data.put("work", "{\"job\":\"eventually_true\"}")
                .put("expect", "[\"o::_goate_result,==,4\"]");
        int r = Integer.parseInt("" + Employee.recruit("work until", data).work());
        assertEquals(r, 4);
    }

    @Test(groups = {"unit"})
    public void employeeTyped() {
        Goate data = new Goate();
        data.put("work", "{\"job\":\"eventually_true\"}")
            .put("expect", "[\"o::_goate_result,==,4\"]");
        int r = new EventuallyTrueInt().work();//Integer.parseInt("" + Employee.recruit("work until", data).work());
        assertEquals(r, 1);
    }

    @Test(groups = {"unit"})
    public void employeeTypedRecruited() {
        Goate data = new Goate();
        data.put("work", "{\"job\":\"eventually_true\"}")
            .put("expect", "[\"o::_goate_result,==,4\"]");
        int r = Employee.recruit("eventually_true_int", data, Integer.class).work();
        assertEquals(r, 1);
    }

    @Test(groups = {"unit"})
    public void untilConditionMetTimedOut() {
        Goate data = new Goate();
        data.put("work", "{\"job\":\"eventually_true\"}")
                .put("expect", "[\"o::_goate_result,==,4\"]")
                .put("timeout", 1000)
                .put("period", 500);
        assertNull(Employee.recruit("work until", data).work());
    }

    @Test(groups = {"unit"})
    public void untilConditionMetTimedOutReturnLast() {
        Goate data = new Goate();
        data.put("work", "{\"job\":\"eventually_true\"}")
                .put("expect", "[\"o::_goate_result,==,4\"]")
                .put("timeout", 1000)
                .put("period", 500)
                .put("return last", true);
        int r = Integer.parseInt("" + Employee.recruit("work until", data).work());
        assertNotEquals(r, 4);
    }
}
