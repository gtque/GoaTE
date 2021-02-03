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
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the dsl for shifting the date.
 * Created by gtque on 3/10/2017.
 */
public class DateShiftTest {
    @Test(groups = {"unit"})
    public void simpleDateShift(){
        Goate data = new Goate();
        data.put("test", "date shift::MM-dd-yyyy,11-25-2009,-1");
        String shiftedDate = "" + data.get("test");
        Assert.assertEquals(shiftedDate,"11-24-2009");
    }

    @Test(groups = {"unit"})
    public void unitDateShift(){
        Goate data = new Goate();
        data.put("test", "date shift::MM-dd-yyyy,11-25-2009,1,n");
        String shiftedDate = "" + data.get("test");
        Assert.assertEquals(shiftedDate,"12-25-2009");
    }

    @Test(groups = {"unit"})
    public void secondDateTimeShift(){
        Goate data = new Goate();
        data.put("test", "dateTime shift::MM-dd-yyyy'T'HH:mm:ss'Z',11-25-2009T12:34:56Z,5,s");
        String shiftedDate = "" + data.get("test");
        Assert.assertEquals(shiftedDate,"11-25-2009T12:35:01Z");
    }

    @Test(groups = {"unit"})
    public void minuteDateTimeShift(){
        Goate data = new Goate();
        data.put("test", "dateTime shift::MM-dd-yyyy'T'HH:mm:ss'Z',11-25-2009T12:34:56Z,1,m");
        String shiftedDate = "" + data.get("test");
        Assert.assertEquals(shiftedDate,"11-25-2009T12:35:56Z");
    }

    @Test(groups = {"unit"})
    public void hourDateTimeShift(){
        Goate data = new Goate();
        data.put("test", "dateTime shift::MM-dd-yyyy'T'HH:mm:ss'Z',11-25-2009T12:34:56Z,-23,h");
        String shiftedDate = "" + data.get("test");
        Assert.assertEquals(shiftedDate,"11-24-2009T13:34:56Z");
    }
    @Test(groups = {"unit"})
    public void dayDateTimeShift(){
        Goate data = new Goate();
        data.put("test", "dateTime shift::MM-dd-yyyy'T'HH:mm:ss'Z',11-25-2009T12:34:56Z,-1,d");
        String shiftedDate = "" + data.get("test");
        Assert.assertEquals(shiftedDate,"11-24-2009T12:34:56Z");
    }
    @Test(groups = {"unit"})
    public void weekDateTimeShift(){
        Goate data = new Goate();
        data.put("test", "dateTime shift::MM-dd-yyyy'T'HH:mm:ss'Z',11-25-2009T12:34:56Z,1,w");
        String shiftedDate = "" + data.get("test");
        Assert.assertEquals(shiftedDate,"12-02-2009T12:34:56Z");
    }
    @Test(groups = {"unit"})
    public void monthDateTimeShift(){
        Goate data = new Goate();
        data.put("test", "dateTime shift::MM-dd-yyyy'T'HH:mm:ss'Z',11-25-2009T12:34:56Z,-1,months");
        String shiftedDate = "" + data.get("test");
        Assert.assertEquals(shiftedDate,"10-25-2009T12:34:56Z");
    }

    @Test(groups = {"unit"})
    public void nextMonthDateShift(){
        Goate data = new Goate();
        data.put("test", "date shift::yyyy-MM-dd,2009-11-24,7");
        String shiftedDate = "" + data.get("test");
        Assert.assertEquals(shiftedDate,"2009-12-01");
    }

}
