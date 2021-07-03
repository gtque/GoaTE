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

import com.thegoate.Goate;
import com.thegoate.json.utils.fill.FillJson;
import com.thegoate.json.utils.get.GetJsonField;
import com.thegoate.json.utils.insert.InsertJson;
import com.thegoate.utils.GoateUtility;
import com.thegoate.utils.get.NotFound;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by Eric Angeli on 4/18/2018.
 */
@ToJsonUtil
public class GoateToJSON extends GoateUtility implements ToJsonUtility{

    public GoateToJSON(Object val) {
        super(val);
    }

    boolean strict = false;

    @Override
    protected void init(Object val){
        processNested = false;
        super.init(val);
    }

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
        String json = "{}";
        String result = "";
        if (takeActionOn != null && takeActionOn.size() > 0) {
            json = takeActionOn.get("", json, String.class);
            takeActionOn.drop("");
            for (String key : filteredKeys(takeActionOn.keys())) {
                try {
                    String dKey = key;
                    //key = key.replaceAll("_",".");
                    String keyFull = "";
                    if (key.contains(".")) {
                        keyFull = key.substring(0, key.lastIndexOf("."));
                        key = key.substring(key.lastIndexOf(".") + 1);
                    }
                    boolean exists = false;
                    try {
                        if (!keyFull.isEmpty()) {
                            exists = checkKey(json, keyFull);
                        }
                        if (!exists||!keyFull.isEmpty()) {
                            exists = checkKey(json, dKey);
                        }
                    } catch (Exception e) {
                        LOG.debug("the key, " + keyFull + ", did not exist");
                    }
                    if (exists) {
                        if(!strict) {
                            json = new FillJson(json).with(new Goate().put(dKey, takeActionOn.get(dKey)));
                        } else {
                            json = new FillJson(json).with(new Goate().put(dKey, takeActionOn.getStrict(dKey)));
                        }
                    } else {
                        if(!strict) {
                            json = ""+new InsertJson(key, takeActionOn.get(dKey)).into(json).in(keyFull).insert();
                        } else {
                            json = ""+new InsertJson(key, takeActionOn.getStrict(dKey)).into(json).in(keyFull).insert();
                        }
                    }
//                    json = Insert.json(key, to.getObject()).into(json).in(keyFull).insert();
                } catch (Exception e) {
                    LOG.warn("problem inserting the test object into a json string: " + e.getMessage(), e);
                }
            }
        }
        return prettyPrint(json);
    }

    protected String prettyPrint(String json){
        String pretty = json;
        try{
            pretty = new JSONObject(json).toString(3);
        } catch (Exception e){
            try{
                pretty = new JSONArray(json).toString(3);
            } catch(Exception e2) {
                LOG.warn("Goate To Json", "Failed to pretty print the json", e);
            }
        }
        return  pretty;
    }
    protected List<String> filteredKeys(Set<String> keys){
        List<String> list = new ArrayList<>();
        if(keys!=null){
            list.addAll(keys);
            Collections.sort(list);
        }
        return list;
    }

    protected boolean checkKey(String json, String key) {
        boolean exists = false;
        try {
            Object og = new GetJsonField(key).from(json);
            String g = ""+og;
            if (!g.equals("null") && !g.equals(json) && !(og instanceof NotFound)) {
                exists = true;
            }
        } catch (Exception e) {
            LOG.debug("the key, " + key + ", did not exist");
        }
        return exists;
    }

}
