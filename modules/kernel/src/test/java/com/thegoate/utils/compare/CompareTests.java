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

import com.thegoate.utils.compare.tools.integer.*;
import com.thegoate.utils.compare.tools.l.CompareLongEqualTo;
import com.thegoate.utils.compare.tools.l.CompareLongGreaterThanEqualTo;
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
        assertTrue(new CompareIntEqualTo(42).to(42).using("==").evaluate());
    }

    @Test(groups = {"unit"})
    public void intEqualToFail(){
        assertFalse(new CompareIntEqualTo(43).to(42).using("==").evaluate());
    }

    @Test(groups = {"unit"})
    public void intNotEqualTo(){
        assertTrue(new CompareIntNotEqualTo(42).to(43).using("!=").evaluate());
    }

    @Test(groups = {"unit"})
    public void intNotEqualToFail(){
        assertFalse(new CompareIntNotEqualTo(42).to(42).using("!=").evaluate());
    }

    @Test(groups = {"unit"})
    public void intLessEqualTo(){
        assertTrue(new CompareIntLessThanEqualTo(42).to(42).using("<=").evaluate());
    }

    @Test(groups = {"unit"})
    public void intLessEqualToLess(){
        assertTrue(new CompareIntLessThanEqualTo(41).to(42).using("<=").evaluate());
    }

    @Test(groups = {"unit"})
    public void intLessEqualToFail(){
        assertFalse(new CompareIntLessThanEqualTo(42).to(41).using("<=").evaluate());
    }

    @Test(groups = {"unit"})
    public void intGreaterEqualTo(){
        assertTrue(new CompareIntGreaterThanEqualTo(42).to(42).using(">=").evaluate());
    }

    @Test(groups = {"unit"})
    public void intGreaterEqualToGreater(){
        assertTrue(new CompareIntGreaterThanEqualTo(43).to(42).using(">=").evaluate());
    }

    @Test(groups = {"unit"})
    public void intGreaterEqualToFail(){
        assertFalse(new CompareIntGreaterThanEqualTo(42).to(43).using(">=").evaluate());
    }

    @Test(groups = {"unit"})
    public void intGreater(){
        assertTrue(new CompareIntGreaterThan(43).to(42).using(">").evaluate());
    }

    @Test(groups = {"unit"})
    public void intGreaterEqualFail(){
        assertFalse(new CompareIntGreaterThan(42).to(42).using(">").evaluate());
    }

    @Test(groups = {"unit"})
    public void intGreaterFail(){
        assertFalse(new CompareIntGreaterThan(42).to(43).using(">").evaluate());
    }

    @Test(groups = {"unit"})
    public void intLesser(){
        assertTrue(new CompareIntLessThan(42).to(43).using("<").evaluate());
    }

    @Test(groups = {"unit"})
    public void intLesserEqualFail(){
        assertFalse(new CompareIntLessThan(42).to(42).using("<").evaluate());
    }

    @Test(groups = {"unit"})
    public void intLesserFail(){
        assertFalse(new CompareIntLessThan(43).to(42).using("<").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatEqualTo(){
        assertTrue(new Compare(42.0).to(42.0).using("==").evaluate());
    }

    @Test(groups = {"unit"})
    public void intFloatEqualTo(){
        assertTrue(new CompareIntEqualTo(42).to(42.0).using("==").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatIntEqualTo(){
        assertTrue(new Compare(42.0).to(42).using("==").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatEqualToFail(){
        assertFalse(new Compare(42.1).to(42.0).using("==").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatNotEqualTo(){
        assertTrue(new Compare(42.1).to(42.0).using("!=").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatNotEqualToFail(){
        assertFalse(new Compare(42.0).to(42.0).using("!=").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatLessEqualTo(){
        assertTrue(new Compare(42.0).to(42.0).using("<=").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatLessEqualToLess(){
        assertTrue(new Compare(42.0).to(42.1).using("<=").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatLessEqualToFail(){
        assertFalse(new Compare(42.0).to(41.0).using("<=").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatGreaterEqualTo(){
        assertTrue(new Compare(42.0).to(42.0).using(">=").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatGreaterEqualToGreater(){
        assertTrue(new Compare(43.0).to(42.0).using(">=").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatGreaterEqualToFail(){
        assertFalse(new Compare(42.0).to(43.0).using(">=").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatGreater(){
        assertTrue(new Compare(43.0).to(42.0).using(">").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatGreaterEqualFail(){
        assertFalse(new Compare(42.0).to(42.0).using(">").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatGreaterFail(){
        assertFalse(new Compare(42.0).to(43.0).using(">").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatLesser(){
        assertTrue(new Compare(42.0).to(43.0).using("<").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatLesserEqualFail(){
        assertFalse(new Compare(42.0).to(42.0).using("<").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatLesserFail(){
        assertFalse(new Compare(43.0).to(42.0).using("<").evaluate());
    }

    @Test(groups = {"unit"})
    public void longEqualTo(){
        assertTrue(new CompareLongEqualTo(42L).to(42L).using("==").evaluate());
    }

    @Test(groups = {"unit"})
    public void longEqualToFail(){
        assertFalse(new Compare(43L).to(42L).using("==").evaluate());
    }

    @Test(groups = {"unit"})
    public void longNotEqualTo(){
        assertTrue(new Compare(42L).to(43L).using("!=").evaluate());
    }

    @Test(groups = {"unit"})
    public void longNotEqualToFail(){
        assertFalse(new Compare(42L).to(42L).using("!=").evaluate());
    }

    @Test(groups = {"unit"})
    public void longLessEqualTo(){
        assertTrue(new Compare(42).to(42L).using("<=").evaluate());
    }

    @Test(groups = {"unit"})
    public void longLessEqualToLess(){
        assertTrue(new Compare(41L).to(42L).using("<=").evaluate());
    }

    @Test(groups = {"unit"})
    public void longLessEqualToFail(){
        assertFalse(new Compare(42L).to(41L).using("<=").evaluate());
    }

    @Test(groups = {"unit"})
    public void longGreaterEqualTo(){
        assertTrue(new CompareLongGreaterThanEqualTo(42L).to(42L).using(">=").evaluate());
    }

    @Test(groups = {"unit"})
    public void longGreaterEqualToGreater(){
        assertTrue(new CompareLongGreaterThanEqualTo(43L).to(42L).using(">=").evaluate());
    }

    @Test(groups = {"unit"})
    public void longGreaterEqualToFail(){
        assertFalse(new Compare(42L).to(43L).using(">=").evaluate());
    }

    @Test(groups = {"unit"})
    public void longGreater(){
        assertTrue(new Compare(43L).to(42L).using(">").evaluate());
    }

    @Test(groups = {"unit"})
    public void longGreaterEqualFail(){
        assertFalse(new Compare(42L).to(42L).using(">").evaluate());
    }

    @Test(groups = {"unit"})
    public void longGreaterFail(){
        assertFalse(new Compare(42L).to(43L).using(">").evaluate());
    }

    @Test(groups = {"unit"})
    public void longLesser(){
        assertTrue(new Compare(42L).to(43L).using("<").evaluate());
    }

    @Test(groups = {"unit"})
    public void longLesserEqualFail(){
        assertFalse(new Compare(42L).to(42L).using("<").evaluate());
    }

    @Test(groups = {"unit"})
    public void longLesserFail(){
        assertFalse(new Compare(43L).to(42L).using("<").evaluate());
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
    public void stringIsEmpty(){
        assertTrue(new Compare("").to(true).using("isEmpty").evaluate(),"The String was apparently not empty.");
    }

    @Test(groups = {"unit"})
    public void objectStringEqualTo(){
        assertTrue(new Compare("hello").to("hello").using("==").evaluate());
    }

    @Test(groups = {"unit"})
    public void dateIsPattern(){
        assertTrue(new Compare("2009-11-24").to("yyyy-MM-dd").using("dateIsPattern").evaluate(),"The date did not match the pattern.");
    }

    @Test(groups = {"unit"})
    public void dateIsPatternDoesNotMatch(){
        assertFalse(new Compare("2009/11/24").to("yyyy/MMM/dd").using("dateIsPattern").evaluate(),"The date matched the pattern.");
    }

    @Test(groups = {"unit"})
    public void isNullTrue(){
        assertTrue(new Compare(null).to(true).using("isNull").evaluate(),"They where not null?");
    }

    @Test(groups = {"unit"})
    public void isNullFalse(){
        assertFalse(new Compare(null).to(false).using("isNull").evaluate(),"They where not null?");
    }

    @Test(groups = {"unit"})
    public void isNullFalseTrue(){
        assertTrue(new Compare("mustard").to(false).using("isNull").evaluate(),"They where not null?");
    }

    @Test(groups = {"unit"})
    public void simpleDateLessThan(){
        assertTrue(new Compare("2017-06-11T04:00:00.000+0000").using("<").to("2017-06-30").evaluate(), "The date was not less than apparently.");
    }
}
