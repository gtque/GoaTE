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
package com.thegoate.json.utils.tojson;

import com.thegoate.json.JsonUtil;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Eric Angeli on 4/18/2018.
 */
@ToJsonUtil
public class JsonToJSON extends JsonUtil implements ToJsonUtility{

    public JsonToJSON(Object val) {
        super(val);
    }

    boolean strict = false;
    @Override
    protected Object processNested(Object subContainer) {
        return null;
    }

    @Override
    public String convertStrict(){
        strict = true;
        return convert();
    }

    @Override
    public String convert() {
        if(takeActionOn instanceof JSONObject){
            takeActionOn = ((JSONObject)takeActionOn).toString(4);
        } else if(takeActionOn instanceof JSONArray){
            takeActionOn = ((JSONArray)takeActionOn).toString(4);
        }
        return takeActionOn.toString();
    }

}
