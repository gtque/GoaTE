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
package com.thegoate.expect.test;

import com.thegoate.annotations.IsDefault;
import com.thegoate.utils.compare.CompareTool;
import com.thegoate.utils.compare.CompareUtil;
import com.thegoate.utils.get.NotFound;

/**
 * Created by Eric Angeli on 3/19/2019.
 */
@CompareUtil(operator = "notFound", type="Object")
@IsDefault
public class IsNotFound extends CompareTool {

    public IsNotFound(Object actual) {
        super(actual);
    }

    @Override
    public boolean evaluate() {
        boolean result = actual instanceof NotFound;
        boolean exp = Boolean.parseBoolean(""+expected);
        return exp?result:!result;
    }

    @Override
    public boolean isType(Object check) {
        return true;
    }
}
