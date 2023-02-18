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
package com.thegoate.utils.compare.tools.list;

import com.thegoate.annotations.IsDefault;
import com.thegoate.utils.compare.CompareTool;
import com.thegoate.utils.compare.CompareUtil;

import java.util.List;

/**
 * Checks if a string contains the given string.
 * Created by Eric Angeli on 4/17/2018.
 */
@CompareUtil(operator = "contains", type = List.class)
@IsDefault(forType = true)
public class CompareListContains extends CompareTool {
    public CompareListContains(Object actual) {
        super(actual);
    }

    @Override
    public boolean evaluate() {
        List<?> act = (List)expected;
        return act.contains(actual);
    }

    @Override
    public boolean isType(Object check) {
        return check instanceof List;
    }
}
