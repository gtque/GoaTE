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
package com.thegoate.utils.fill.serialize.pojos;

import java.util.Map;

import com.thegoate.utils.fill.serialize.GoatePojo;
import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.collections.MapKeyType;
import com.thegoate.utils.fill.serialize.collections.MapType;

/**
 * Created by Eric Angeli on 6/26/2018.
 */
@GoatePojo(id = "NestedMap1")
public class NestedMap1 {

    private int fieldI = -42;
    private boolean fieldB = true;
    @MapType(type = String.class)
    @MapKeyType(type = String.class)
    private Map<String,String> fieldO;

    public int getFieldI() {
        return fieldI;
    }

    public void setFieldI(int fieldI) {
        this.fieldI = fieldI;
    }

    public boolean isFieldB() {
        return fieldB;
    }

    public void setFieldB(boolean fieldB) {
        this.fieldB = fieldB;
    }

    public Map<String, String> getFieldO() {
        return fieldO;
    }

    public void setFieldO(Map<String, String> fieldO) {
        this.fieldO = fieldO;
    }
}
