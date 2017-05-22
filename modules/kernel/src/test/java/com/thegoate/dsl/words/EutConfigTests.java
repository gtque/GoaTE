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
import com.thegoate.testng.TestNGEngineAnnotatedDL;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Test loading of eut configs.
 * Created by Eric Angeli on 5/19/2017.
 */
public class EutConfigTests extends TestNGEngineAnnotatedDL {

    @Test(groups = {"unit"})
    public void loadLocalByDefault(){
        Goate d = new Goate().put("test", "eut::hello");
        String actual = ""+ d.get("test", "foobar");
        assertEquals(actual, "world!");
        assertEquals(d.get("test", "foobar"), "world!");
    }

    @Test(groups = {"unit"})
    public void loadLocalOverride(){
        Goate d = new Goate().put("eut", "local").put("test", "eut::hello").put("hello","chocolate");
        String actual = ""+ d.get("test", "foobar");
        assertEquals(actual, "chocolate");
        assertEquals(d.get("test", "foobar"), "chocolate");
    }

    @Test(groups = {"unit"})
    public void loadDooda(){
        Goate d = new Goate().put("eut", "dooda").put("test", "eut::hello");
        String actual = ""+ d.get("test", "foobar");
        assertEquals(actual, "dooda!");
        assertEquals(d.get("test", "foobar"), "dooda!");
    }
}
