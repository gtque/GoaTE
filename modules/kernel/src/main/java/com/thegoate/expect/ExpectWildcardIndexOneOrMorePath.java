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
 * Useful for building paths in expectations with a wildcard index expecting 1 or more, such as json arrays.
 * Created by Eric Angeli on 11/29/2018.
 */
@GoateDSL(word = "wildcardIndexOneOrMore")
@GoateDescription(description = "returns the pattern for a wild card index expecting 1 or more.",
parameters = {"expectedSize:=the expected size of the array, optional","startingIndex:=the index to start at, optional, if specified than expectedSize must also be set."})
public class ExpectWildcardIndexOneOrMorePath extends DSL {

    public ExpectWildcardIndexOneOrMorePath(Object value) {
        super(value);
    }

    public static String wildcardIndexOneOrMore(){
        return "+";
    }

    @Override
    public Object evaluate(Goate data) {
        String wildcard = "+";
        return wildcard;
    }
}
