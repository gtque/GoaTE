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

package com.thegoate.utils.togoate;

import com.thegoate.Goate;
import com.thegoate.utils.UnknownUtilType;

/**
 * The generic get class.
 * This will attempt to look up the specific get utility for the type detected.
 * Created by Eric Angeli on 5/5/2017.
 */
public class ToGoate extends UnknownUtilType implements ToGoateUtility{
    boolean autoIncrement = true;
    ToGoateUtility tool = null;
    Object original = null;

    public ToGoate(Object o){
        this.original = o;
    }

    @Override
    public boolean isType(Object check) {
        return false;
    }

    @Override
    public ToGoateUtility autoIncrement(boolean increment) {
        this.autoIncrement = increment;
        return this;
    }

    @Override
    public Goate convert() {
        Goate result = null;
        if(original instanceof Goate){
            result = (Goate)original;
        } else {
            tool = (ToGoateUtility) buildUtil(original, ToGoateUtil.class);
            if (tool != null) {
                result = tool.autoIncrement(autoIncrement).convert();
            }
            if (result == null) {
                LOG.info("Failed to convert: " + original);
            }
        }
        return result;
    }
}
