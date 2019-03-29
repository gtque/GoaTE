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
package com.thegoate.utils.fill.serialize.string;

import com.thegoate.Goate;
import com.thegoate.annotations.IsDefault;
import com.thegoate.utils.Utility;

/**
 * Created by Eric Angeli on 3/28/2019.
 */
@StringConverterUtil
@IsDefault
public class StringConverterDefault implements StringConverterUtility {
    Goate health = new Goate();
    Goate data;
    Object value;

    public StringConverterDefault(){

    }

    public StringConverterDefault(Object value){
        this.value = value;
    }

    @Override
    public String convert() {
        String result = null;
        if(value != null){
            result = value.toString();
        }
        return result;
    }

    @Override
    public StringConverterUtility value(Object value) {
        this.value = value;
        return this;
    }

    @Override
    public Utility setData(Goate data) {
        this.data = data;
        return this;
    }

    @Override
    public boolean isType(Object check) {
        return false;
    }

    @Override
    public Goate healthCheck() {
        return health;
    }
}
