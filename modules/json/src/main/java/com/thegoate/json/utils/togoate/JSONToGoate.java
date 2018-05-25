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
package com.thegoate.json.utils.togoate;

import com.thegoate.Goate;
import com.thegoate.json.JsonUtil;
import com.thegoate.utils.togoate.ToGoateUtil;
import com.thegoate.utils.togoate.ToGoateUtility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Converts from json to goate.
 * Created by Eric Angeli on 5/19/2017.
 */
@ToGoateUtil
public class JSONToGoate extends JsonUtil implements ToGoateUtility {
    Goate result;
    boolean autoIncrement = true;

    public JSONToGoate(Object val) {
        super(val);
        takeActionOn = val;
        nested = null;
    }

    @Override
    protected Object processNested(Object subContainer) {
        return subContainer;
    }

    @Override
    public ToGoateUtility autoIncrement(boolean increment) {
        this.autoIncrement = increment;
        return this;
    }

    @Override
    public Goate convert() {
        if(takeActionOn instanceof String){
            try{
                    takeActionOn = new JSONObject(""+takeActionOn);
            }catch(JSONException je){
                try{
                    takeActionOn = new JSONArray(""+takeActionOn);
                }catch (Exception e){
                    LOG.warn("Failed to convert the string to a JSONObject or JSONArray.\n"+e.getMessage());
                }
            }
        }
        result = new Goate().autoIncrement(autoIncrement);
        if(takeActionOn!=null){
            if(takeActionOn instanceof JSONObject){
                find((JSONObject) takeActionOn, "");
            }else if(takeActionOn instanceof JSONArray){
                find((JSONArray) takeActionOn, "");
            }else{
                LOG.warn("It does not appear as though the takeActionOn is json.");
            }
        }
        return result;
    }

    protected void find(JSONObject json, String currentPath){
        for(String key:json.keySet()){
            Object o = json.get(key);
            result.put(currentPath+(currentPath.length()>0?".":"")+key,o);
            if(o instanceof JSONObject){
                find((JSONObject) o, currentPath+(currentPath.length()>0?".":"")+key);
            }else if(o instanceof JSONArray){
                find((JSONArray) o, currentPath+(currentPath.length()>0?".":"")+key);
            }
        }
    }

    protected void find(JSONArray json, String currentPath){
        for(int key = 0; key<json.length(); key++){
            Object o = json.get(key);
            result.put(currentPath+(currentPath.length()>0?".":"")+key,o);
            if(o instanceof JSONObject){
                find((JSONObject) o, currentPath+(currentPath.length()>0?".":"")+key);
            }else if(o instanceof JSONArray){
                find((JSONArray) o, currentPath+(currentPath.length()>0?".":"")+key);
            }
        }
    }
}
