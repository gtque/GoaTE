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

import static org.testng.Assert.assertTrue;

/**
 * Tests the dsl for getting the current system nano time.
 * Created by gtque on 4/21/2017.
 */
public class NanoTimeTest {
    @Test(groups = {"unit"})
    public void translateNanoTimeSet(){
        Goate data = new Goate();
        data.put("test", "nanotime::");
        Long start = System.nanoTime();
        Long nt = (Long) data.get("test");
        Assert.assertNotEquals(nt, 0L);
        assertTrue(nt>start,"the generate time was not greater than the start time.");
        assertTrue(nt<System.nanoTime(),"the generate time was not less than the end time.");
    }

    @Test(groups = {"unit"})
    public void translateNanoTimeDefault(){
        Goate data = new Goate();
        Long start = System.nanoTime();
        Long nt = (Long) data.get("test","nanotime::");
        Assert.assertNotEquals(nt, 0L);
        assertTrue(nt>start,"the generate time was not greater than the start time.");
        assertTrue(nt<System.nanoTime(),"the generate time was not less than the end time.");
    }
}
