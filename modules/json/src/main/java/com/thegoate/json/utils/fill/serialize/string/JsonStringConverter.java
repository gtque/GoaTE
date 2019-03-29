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
package com.thegoate.json.utils.fill.serialize.string;

import com.thegoate.Goate;
import com.thegoate.annotations.IsDefault;
import com.thegoate.json.JsonUtil;
import com.thegoate.utils.Utility;
import com.thegoate.utils.fill.serialize.string.StringConverterUtil;
import com.thegoate.utils.fill.serialize.string.StringConverterUtility;
import org.json.JSONObject;

/**
 * Created by Eric Angeli on 3/28/2019.
 */
@StringConverterUtil
public class JsonStringConverter extends JsonUtil implements StringConverterUtility {

    public JsonStringConverter(){
        super(null);
    }

    public JsonStringConverter(Object val) {
        super(val);
    }

    @Override
    public String convert() {
        String result = null;
        if(takeActionOn != null && takeActionOn != JSONObject.NULL){
            result = takeActionOn.toString();
        }
        return result;
    }

    @Override
    public StringConverterUtility value(Object value) {
        this.takeActionOn = value;
        return this;
    }

    @Override
    public JsonUtil setData(Goate data) {
        this.data = data;
        return this;
    }

    @Override
    public Goate healthCheck() {
        return health;
    }

    @Override
    protected Object processNested(Object subContainer) {
        return null;
    }
}
