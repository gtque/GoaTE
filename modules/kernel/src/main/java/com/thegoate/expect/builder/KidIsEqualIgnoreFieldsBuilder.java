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
package com.thegoate.expect.builder;

import com.thegoate.expect.Expectation;
import com.thegoate.utils.fill.serialize.Kid;

import java.util.ArrayList;
import java.util.List;

public class KidIsEqualIgnoreFieldsBuilder<Self extends KidIsEqualIgnoreFieldsBuilder> extends ExpectationBuilder<Self> {

    private List<String> fieldNames = new ArrayList<>();

    public Self ignoreField(String fieldName) {
        fieldNames.add(fieldName);
        return (Self) this;
    }

    @Override
    public List<Expectation> build() {
        if (actual instanceof Kid && expected instanceof Kid) {
            if (fieldNames.size() > 0) {
                if (actual != null) {
                    try {
                        actual = ((Kid) actual).cloneButIgnore(fieldNames);
                    } catch (CloneNotSupportedException e) {
                        LOG.error("Trying to Ignore Fields", "Problem cloning and ignore: " + e.getMessage(), e);
                    }
                }
                if (expected != null) {
                    try {
                        expected = ((Kid) expected).cloneButIgnore(fieldNames);
                    } catch (CloneNotSupportedException e) {
                        LOG.error("Trying to Ignore Fields", "Problem cloning and ignore: " + e.getMessage(), e);
                    }
                }
            }
        }
        expect(Expectation.build()
                .actual(actual)
                .isEqualTo(expected));
        return getExpectations();
    }
}
