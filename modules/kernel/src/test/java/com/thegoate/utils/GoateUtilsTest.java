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
package com.thegoate.utils;

import com.thegoate.Goate;
import org.testng.annotations.Test;

import static com.thegoate.dsl.words.EutConfigDSL.eut;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

/**
 * Test the helper methods in GoateUtils.
 * Created by Eric Angeli on 5/10/2017.
 */
public class GoateUtilsTest {

    @Test(groups = {"unit"})
    public void setEnv(){
        assertNull(GoateUtils.getProperty("finklestein"));
        GoateUtils.setEnvironment("finklestein", "dooda");
        assertEquals(GoateUtils.getProperty("finklestein","man taco"),"dooda");
        GoateUtils.removeEnvironment("finklestein");
        assertEquals(GoateUtils.getProperty("finklestein","man taco"),"man taco");
    }
    @Test(groups = {"unit"})
    public void setEnvGetByEut(){
        assertNull(GoateUtils.getProperty("finklestein"));
        GoateUtils.setEnvironment("finklestein", "grippers");
        assertEquals(eut("finklestein", "man taco"),"grippers");
        GoateUtils.removeEnvironment("finklestein");
        assertEquals(eut("finklestein", "man taco"),"man taco");
    }

    @Test(groups = {"unit"})
    public void toGoateFromString(){
        Goate g = new Goate()
            .put("test", "toGoate::a=42;b=hello world;c=boolean::true");
        Goate g2 = g.get("test", new Goate(), Goate.class);
        assertEquals(g2.get("a", "43", String.class), "42");
        assertEquals(g2.get("b", "42", String.class), "hello world");
        assertTrue(g2.get("c", false, Boolean.class));
    }
}
