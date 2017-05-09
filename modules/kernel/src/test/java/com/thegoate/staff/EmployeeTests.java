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
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by gtque on 4/25/2017.
 */
public class EmployeeTests {

    @Test(groups = {"unit"})
    public void simpleEmployeeSize(){
        Goate data = new Goate().put("val##", 1).put("val##", 2).put("val##", 3);
        int r = Integer.parseInt("" + Employee.recruit("t_size", data).work());
        assertEquals(r, 3);
    }

    @Test(groups = {"unit"})
    public void simpleEmployeeLength(){
        Goate data = new Goate().put("val##", 1).put("val##", 2).put("val##", 3);
        int r = Integer.parseInt("" + Employee.recruit("t_length", data).work());
        assertEquals(r, 3);
    }

    @Test(groups = {"unit"})
    public void simpleEmployeeLengthNull(){
        Goate data = null;
        int r = Integer.parseInt("" + Employee.recruit("t_length", data).work());
        assertEquals(r, -1);
    }
}
