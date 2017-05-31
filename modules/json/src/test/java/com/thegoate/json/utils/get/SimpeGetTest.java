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
package com.thegoate.json.utils.get;

import com.thegoate.testng.TestNGEngineAnnotatedDL;
import com.thegoate.utils.get.Get;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * test get from json.
 * Created by Eric Angeli on 5/19/2017.
 */
public class SimpeGetTest extends TestNGEngineAnnotatedDL {

    @Test(groups = {"unit"})
    public void simpleGet(){
        String a = "" + new Get("a").from(new Get("simple.json").from("file::"));
        assertEquals(a, "b");
        Object cSize = new Get("c>size()").from(new Get("simple.json").from("file::"));
        Object cSize2 = new Get("c>length()").from(new Get("simple.json").from("file::"));
        assertEquals(cSize, 2);
        assertEquals(cSize2, 2);
    }
}
