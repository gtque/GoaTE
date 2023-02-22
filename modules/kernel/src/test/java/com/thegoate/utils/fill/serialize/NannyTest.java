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
package com.thegoate.utils.fill.serialize;

import com.thegoate.expect.Expectation;
import com.thegoate.testng.TestNGEngineMethodDL;
import com.thegoate.utils.fill.serialize.pojos.Doug;
import com.thegoate.utils.fill.serialize.pojos.Steve;
import org.testng.annotations.Test;

public class NannyTest extends TestNGEngineMethodDL {

    @Test(groups = {"unit"})
    public void cloneAllKids() throws CloneNotSupportedException {
        Doug og = new Doug()
                .setTheAnswer(84)
                .setTheClone(new Steve()
                        .setTheWord("birds"));
        Doug clone = (Doug) og.clone();
        Doug clone2a = (Doug) og.clone();
        Doug clone2b = (Doug) clone.clone();
        Doug clone3 = (Doug) clone.clone();
        clone.getTheClone().setTheWord("thrilled");

        expect(Expectation.build()
                .setId("2a2b")
                .actual(clone2a)
                .isEqualTo(clone2b)
                .failureMessage("clone2a was not equal to clone2b"));
        expect(Expectation.build()
                .setId("og3")
                .actual(og)
                .isEqualTo(clone3)
                .failureMessage("og was not equal to clone3"));
        expect(Expectation.build()
                .actual(og.hashCode())
                .isNotEqualTo(clone3.hashCode()));
        expect(Expectation.build()
                .actual(clone.getTheClone().getTheWord())
                .isNotEqualTo(clone3.getTheClone().getTheWord())
                .failureMessage("clone was equal to clone3"));

    }
}
