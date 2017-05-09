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
package com.thegoate.utils.compare;

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Tests compare utilities.
 * Created by Eric Angeli on 5/9/2017.
 */
public class CompareTests {

    @Test(groups = {"unit"})
    public void intEqualTo(){
        assertTrue(new Compare(42).to(42).using("==").evaluate());
    }

    @Test(groups = {"unit"})
    public void intEqualToFail(){
        assertFalse(new Compare(43).to(42).using("==").evaluate());
    }

    @Test(groups = {"unit"})
    public void intNotEqualTo(){
        assertTrue(new Compare(42).to(43).using("!=").evaluate());
    }

    @Test(groups = {"unit"})
    public void intNotEqualToFail(){
        assertFalse(new Compare(42).to(42).using("!=").evaluate());
    }

    @Test(groups = {"unit"})
    public void intLessEqualTo(){
        assertTrue(new Compare(42).to(42).using("<=").evaluate());
    }

    @Test(groups = {"unit"})
    public void intLessEqualToLess(){
        assertTrue(new Compare(41).to(42).using("<=").evaluate());
    }

    @Test(groups = {"unit"})
    public void intLessEqualToFail(){
        assertFalse(new Compare(42).to(41).using("<=").evaluate());
    }

    @Test(groups = {"unit"})
    public void intGreaterEqualTo(){
        assertTrue(new Compare(42).to(42).using(">=").evaluate());
    }

    @Test(groups = {"unit"})
    public void intGreaterEqualToGreater(){
        assertTrue(new Compare(43).to(42).using(">=").evaluate());
    }


    @Test(groups = {"unit"})
    public void intGreaterEqualToFail(){
        assertFalse(new Compare(42).to(43).using(">=").evaluate());
    }

    @Test(groups = {"unit"})
    public void intGreater(){
        assertTrue(new Compare(43).to(42).using(">").evaluate());
    }

    @Test(groups = {"unit"})
    public void intGreaterEqualFail(){
        assertFalse(new Compare(42).to(42).using(">").evaluate());
    }

    @Test(groups = {"unit"})
    public void intGreaterFail(){
        assertFalse(new Compare(42).to(43).using(">").evaluate());
    }

    @Test(groups = {"unit"})
    public void intLesser(){
        assertTrue(new Compare(42).to(43).using("<").evaluate());
    }

    @Test(groups = {"unit"})
    public void intLesserEqualFail(){
        assertFalse(new Compare(42).to(42).using("<").evaluate());
    }

    @Test(groups = {"unit"})
    public void intLesserFail(){
        assertFalse(new Compare(43).to(42).using("<").evaluate());
    }

    @Test(groups = {"unit"})
    public void booleanEqualTo(){
        assertTrue(new Compare(true).to(true).using("==").evaluate());
    }

    @Test(groups = {"unit"})
    public void booleanEqualToFail(){
        assertFalse(new Compare(false).to(true).using("==").evaluate());
    }

    @Test(groups = {"unit"})
    public void booleanNotEqualTo(){
        assertTrue(new Compare(true).to(false).using("!=").evaluate());
    }

    @Test(groups = {"unit"})
    public void booleanNotEqualToFail(){
        assertFalse(new Compare(false).to(false).using("!=").evaluate());
    }

    @Test(groups = {"unit"})
    public void objectStringEqualTo(){
        assertTrue(new Compare("hello").to("hello").using("==").evaluate());
    }
}
