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

package com.thegoate.utils.to;

import com.thegoate.utils.UnknownUtilType;
import com.thegoate.utils.fill.serialize.CastUtil;

/**
 * The generic togoate class.
 * This will attempt to look up the specific to goate utility for the type detected.
 * Created by Eric Angeli on 5/5/2017.
 */
public class To extends UnknownUtilType<To> implements ToUtility {
    ToUtility tool = null;
    protected Object original = null;

    public To(Object o) {
        this.original = o;
//        useCache = true;
    }

    @Override
    public boolean isType(Object check) {
        return false;
    }


    @Override
    public Object convert() {
        Object result = null;
        if (declaredType != null && declaredType.isAssignableFrom(original.getClass())) {
            result = original;
        } else {
            tool = (ToUtility) buildUtil(original, ToUtil.class);
            if (tool != null) {
                result = tool.convert();
            }
        }
        return result;
    }

    @Override
    public boolean checkType(Class tool, Class type) {
        ToUtil tu = (ToUtil) tool.getAnnotation(ToUtil.class);
        return tu != null ? tu.type() != null ? (tu.type() == type) : (type == null) : false;
    }
}
