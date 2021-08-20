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
import com.thegoate.annotations.GoateDescription;
import com.thegoate.dsl.DSL;
import com.thegoate.dsl.GoateDSL;

import java.util.ArrayList;
import java.util.List;

/**
 * Returns a list of things generated from the comma separated list of parameters.
 * Created by gtque on 8/19/2021.
 */
@GoateDSL(word = "list from csv")
@GoateDescription(description = "Returns list of objects generated from the comma separated list of parameters.",
        parameters = {"comma separated list of values"})
public class ListFromCsvDSL extends DSL {
    public ListFromCsvDSL(Object value) {
        super(value);
    }

    @Override
    public Object evaluate(Goate data) {
        List<Object> list = new ArrayList<>();
        for(int i = 1; i<numberOfParameters(); i++) {
            list.add(get(i,data));
        }
        return list;
    }
}
