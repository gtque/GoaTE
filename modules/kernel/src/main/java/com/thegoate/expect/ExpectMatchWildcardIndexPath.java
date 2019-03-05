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
import com.thegoate.annotations.GoateDescription;
import com.thegoate.dsl.DSL;
import com.thegoate.dsl.GoateDSL;

/**
 * Useful for building paths in expectations with a wildcard index, such as json arrays.
 * Created by Eric Angeli on 11/29/2018.
 */
@GoateDSL(word = "matchWildcardIndex")
@GoateDescription(description = "uses the same index for the corresponding wildCardIndex, must be used in conjunction with wildcardIndex. Typically used in the expected path of an expectation.",
        parameters = {"matchIndex:=the index value to be matched in the 'actual' key path."})
public class ExpectMatchWildcardIndexPath extends DSL {

    public ExpectMatchWildcardIndexPath(Object value) {
        super(value);
    }

    public static String matchWildcardIndex() {
        return "%";
    }

    public static String matchWildcardIndex(int matchIndex) {
        StringBuilder wc = new StringBuilder("");
        for (; matchIndex > 0; matchIndex--) {
            wc.append("\\");
        }
        return wc.append("%").toString();
    }

    @Override
    public Object evaluate(Goate data) {
        Object first = get(1, data);
        return matchWildcardIndex(Integer.parseInt("" + first));
    }
}
