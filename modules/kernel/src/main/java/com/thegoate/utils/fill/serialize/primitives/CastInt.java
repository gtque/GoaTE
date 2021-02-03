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
package com.thegoate.utils.fill.serialize.primitives;

import com.thegoate.utils.fill.serialize.CastUtil;
import com.thegoate.utils.fill.serialize.GoateCastUtility;

/**
 * Created by Eric Angeli on 6/26/2018.
 */
@CastUtil
public class CastInt extends GoateCastUtility {

    public CastInt(Object value) {
        super(value);
    }

    @Override
    public <T> T cast(Class<T> type) {
        return (T)(Object)Integer.parseInt("" + ((""+value).equalsIgnoreCase("true")?1:(""+value).equalsIgnoreCase("false")?0:value));
    }

    @Override
    public boolean isType(Object c) {
        return c.equals(Integer.class) || c.equals(Integer.TYPE);
    }
}
