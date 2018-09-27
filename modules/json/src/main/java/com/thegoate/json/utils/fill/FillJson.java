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
package com.thegoate.json.utils.fill;

import com.thegoate.Goate;
import com.thegoate.json.JsonUtil;
import com.thegoate.utils.fill.FillUtil;
import com.thegoate.utils.fill.FillUtility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

import static com.thegoate.utils.GoateUtils.tab;

/**
 * Fills the given json with corresponding date in from the given Goate collection.
 * Created by Eric Angeli on 3/5/2018.
 */
@FillUtil
public class FillJson extends JsonUtil implements FillUtility {
    private StringBuilder description;
    private int tabCount = 0;

    public FillJson(Object val) {
        super(val);
    }

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
    public String with(Goate data) {
        String result = "";
        description = new StringBuilder("");
        if (takeActionOn != null) {
            try {
                result = processJSON("" + takeActionOn, data);
            } catch (Exception e) {
                result = "" + takeActionOn;
            }
        }
        return result;
    }

    public String getDescription() {
        tabCount = 0;
        description = new StringBuilder("");
        processJSON("" + takeActionOn, new Goate());
        return description.toString();
    }

    private String processJSON(String json, Goate data) {
        Object jsonObject;
        boolean isJsonObject = true;
        try {
            if (json.startsWith("[")) {
                jsonObject = new JSONArray(json);
                isJsonObject = false;
            } else {
                jsonObject = new JSONObject(json);
            }
        } catch (JSONException je) {
            throw new RuntimeException("FillJson: converting to a json object: " + je.getMessage());
        }
        jsonObject = isJsonObject ? processJSONObject((JSONObject) jsonObject, "", data) : processJSONArray((JSONArray) jsonObject, "", data);
        json = isJsonObject?((JSONObject)jsonObject).toString(4):((JSONArray)jsonObject).toString(4);
        return json;
    }

    private JSONObject processJSONObject(JSONObject jsonData, String prekey, Goate data) {
        try {
            Iterator<?> keys = jsonData.keys();
            ArrayList<String> remove = new ArrayList<>();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Object jsonValue = jsonData.get(key);
                Object value = getValue(prekey + key, data);

                description.append(tab(tabCount)).append(prekey).append(key).append("\n");
                if (value != null) {
                    if (value.equals("drop field::"))
                        remove.add(key);
                    else if (value.equals("empty field::")||value.equals("empty::"))
                        jsonData.put(key, "");
                    else if (value.equals("null field::")||value.equals("null::"))
                        jsonData.put(key, JSONObject.NULL);
                    else if (!value.equals(""))
                        jsonData.put(key, value);
                } else if (jsonValue instanceof JSONObject) {
                    jsonData.put(key, processJSONObject((JSONObject) jsonValue, prekey + key + ".", data));
                } else if (jsonValue instanceof JSONArray) {
                    jsonData.put(key, processJSONArray((JSONArray) jsonValue, prekey + key + ".", data));
                }
            }
            for (String aRemove : remove) {
                jsonData.remove(aRemove);
            }
        } catch (Exception e) {
            throw new RuntimeException("FillJson: processing the json object: " + e + "\n" + Arrays.toString(e.getStackTrace()));
        }
        return jsonData;
    }

    private Object getValue(String keyPattern, Goate data){
        Object value = data.get(keyPattern);
        if(value==null){
            String kp = data.keys().parallelStream().filter(k -> {
                if(keyPattern.matches(k)){
                    return true;
                }
                return false;
            }).collect(Collectors.joining(","));
            if(!kp.isEmpty()) {
                value = data.get(kp);
            }
        }
        return value;
    }
    private JSONArray processJSONArray(JSONArray jsonArray, String prekey, Goate data) {
        tabCount++;
        try {
            ArrayList<String> drop = new ArrayList<>();
            for (int index = 0; index < jsonArray.length(); index++) {
                Object jsonValue = jsonArray.get(index);
                Object value = getValue(prekey + index, data);

                description.append(tab(tabCount)).append(prekey).append(index).append("\n");
                if (value != null) {
                    if (value.equals("drop field::"))
                        drop.add("" + index);//jsonData.remove(key);
                    else if (value.equals("empty field::"))
                        jsonArray.put(index, "");
                    else if (value.equals("null field::"))
                        jsonArray.put(index, JSONObject.NULL);
                    else if (!value.equals(""))
                        jsonArray.put(index, value);
                } else if (jsonValue instanceof JSONObject) {
                    jsonArray.put(index, processJSONObject((JSONObject) jsonValue, prekey + index + ".", data));
                } else if (jsonValue instanceof JSONArray) {
                    jsonArray.put(index, processJSONArray((JSONArray) jsonValue, prekey + index + ".", data));
                }
            }
            for (int i = drop.size() - 1; i >= 0; i--)
            {
                jsonArray.remove(Integer.parseInt(drop.get(i)));
            }
        } catch (Exception e) {
            throw new RuntimeException("FillJson: processing json array: " + e.getMessage());
        }
        tabCount--;
        return jsonArray;
    }
}
