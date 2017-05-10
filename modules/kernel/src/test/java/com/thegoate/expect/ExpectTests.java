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

package com.thegoate.expect;

import com.thegoate.Goate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Test expect framework.
 * Created by Eric Angeli on 5/10/2017.
 */
public class ExpectTests {
    final Logger LOG = LoggerFactory.getLogger(getClass());

    @Test(groups = {"unit"})
    public void isEven(){
        Goate data = new Goate();
        data.put("check if even.value",4)
                .put("check if even#1.value",5);
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        Expectation e2 = new Expectation(data).from("check if even#1").actual("return").is("!=").expected(true);
        etb.expect("check if even>return,==,boolean::true").expect(e2)
                .expect("check if even>return,!=,boolean::false");
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        assertTrue(result, ev.failed());
    }

    @Test(groups = {"unit"})
    public void isEvenFail(){
        Goate data = new Goate();
        data.put("check if even.value",42);
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect("check if even>return,==,boolean::true");
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        assertFalse(result, ev.failed());
        LOG.debug("failed message:\n"+ev.failed());
    }
}
