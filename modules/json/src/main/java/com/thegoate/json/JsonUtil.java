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
package com.thegoate.json;

import com.thegoate.utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base json util class that implements the isType method.
 * Created by Eric Angeli on 5/19/2017.
 */
public abstract class JsonUtil implements Utility {
    protected final Logger LOG = LoggerFactory.getLogger(getClass());
    protected Object takeActionOn = null;
    protected Object nested = null;

    public JsonUtil(Object val){
        if(val instanceof String){
            String select = ""+val;
            if((select).contains(">")){
                nested = select.substring(select.indexOf(">")+1);
                val = select.substring(0,select.indexOf(">"));
            }
        }
        this.takeActionOn = val;
    }

    @Override
    public boolean isType(Object check) {
        boolean istype = true;
        try{
            if(!(check instanceof JSONObject)) {
                if (new JSONObject(""+check) == null) {
                    istype = false;
                }
            }
        }catch(JSONException je){
            try{
                if(!(check instanceof JSONArray)) {
                    if (new JSONArray(check) == null) {
                        istype = false;
                    }
                }
            }catch (Exception e){
                istype = false;
            }
        }
        return istype;
    }
    protected abstract Object processNested(Object subContainer);
}
