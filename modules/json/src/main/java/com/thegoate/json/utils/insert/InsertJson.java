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
package com.thegoate.json.utils.insert;

import com.thegoate.Goate;
import com.thegoate.json.utils.tojson.GoateToJSON;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.utils.insert.InsertUtil;
import com.thegoate.utils.insert.InsertUtility;
import com.thegoate.utils.togoate.ToGoate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Eric Angeli on 4/18/2018.
 */
@InsertUtil
public class InsertJson implements InsertUtility {
    protected BleatBox LOG = BleatFactory.getLogger(getClass());
    String key = null;
    Object value = null;
    String original = null;
    String after = null;
    String in = null;
    String before = null;
    boolean append = false;
    boolean replace = true;

    public InsertJson(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public InsertUtility value(String id, Object value) {
        this.key = id;
        this.value = value;
        return this;
    }

    @Override
    public InsertUtility into(String original) {
        this.original = original;
        return this;
    }

    @Override
    public InsertUtility after(String location) throws Exception {
        if (before != null || append || in != null)
            throw new Exception("Trying to set 'after' location, but another location is already set. You can only set one location.");
        this.after = location;
        return this;
    }

    @Override
    public InsertUtility before(String location) throws Exception {
        if (after != null || append || in != null)
            throw new Exception("Trying to set 'before' location, but another location is already set. You can only set one location.");
        this.before = location;
        return this;
    }

    @Override
    public InsertUtility in(String location) throws Exception {
        if (before != null || append || after != null)
            throw new Exception("Trying to set 'in' location, but another location is already set. You can only set one location.");
        this.in = location;
        return this;
    }

    @Override
    public InsertUtility append() throws Exception {
        if (before != null || after != null || in != null)
            throw new Exception("Trying to set 'append' location, but another location is already set. You can only set one location.");
        this.append = true;
        return this;
    }

    @Override
    public InsertUtility replaceExisting(boolean replace) {
        this.replace = replace;
        return this;
    }

    @Override
    public InsertUtility resetLocation() {
        this.after = null;
        this.before = null;
        this.in = null;
        this.append = false;
        return this;
    }

    @Override
    public InsertUtility resetInsertValue() {
        this.key = null;
        this.value = null;
        return this;
    }

    @Override
    public String insert(boolean setAsOriginal) throws Exception {
        String result = insert();
        if (setAsOriginal)
            original = result;
        return result;
    }

    @Override
    public String insert(Goate data) throws Exception{
        //probably can't use merge. will need to do a custom merge
        //based on the mapping if present, otherwise default to after.
        Goate od = new ToGoate(original).convert().merge(data,true);
        return new GoateToJSON(od).convert();
    }

    @Override
    public String insert() throws Exception {
        if (before == null && after == null && in == null && append == false)
            throw new Exception("Failed to insert: No Location set. You must set \"after\" or \"before\" location.");
        if (original == null)
            throw new Exception("Failed to insert: The original json has not been set.");
        if (key == null)
            throw new Exception("Failed to insert: The key has not been set.");
        //if (value == null)
        //    throw new Exception("Failed to insert: The value cannot be null");
        Object jsonObject;
        boolean isJsonObject = true;
        if (original.startsWith("{")) {
            jsonObject = new JSONObject(original);
        } else {
            jsonObject = new JSONArray(original);
            if (append) {
                append = false;
                in = "";
            }
            isJsonObject = false;
        }
        if (append)
            if (replace)
                ((JSONObject) jsonObject).put(key, value);
            else {
                try {
                    ((JSONObject) jsonObject).putOnce(key, value);
                } catch (JSONException je) {
                    LOG.warn("key already exists: " + key);
                }
            }
        else if (before != null)
            jsonObject = insertBefore(jsonObject);
        else if (after != null)
            jsonObject = insertAfter(jsonObject);
        else if (in != null)
            jsonObject = insertIn(jsonObject);
        return isJsonObject?((JSONObject)jsonObject).toString(4):((JSONArray)jsonObject).toString(4);
    }

    /**
     * Simply appends the value as order does not matter in json.<br>
     * Unless you are adding to a JSONArray, in which case use "in" instead.
     *
     * @param jsonObject
     * @return
     */
    private Object insertBefore(Object jsonObject) throws Exception {
        in = before.substring(0, before.lastIndexOf("."));
        before = null;
        //jsonObject.append(key, value);
        insertIn(jsonObject);
        return jsonObject;
    }

    /**
     * Simply appends the value as order does not matter in json.<br>
     * Unless you are adding to a JSONArray, in which case use "in" instead.
     *
     * @param jsonObject
     * @return
     */
    private Object insertAfter(Object jsonObject) throws Exception {
        in = after.substring(0, after.lastIndexOf("."));
        after = null;
        //jsonObject.append(key, value);
        insertIn(jsonObject);
        return jsonObject;
    }

    private Object insertIn(Object jsonObject) throws Exception {
        String ids[] = in.split("\\.");
        Object j = jsonObject;
        for (int i = 0; i < ids.length; i++) {//String id:ids) {
            String id = ids[i];
            if(!id.isEmpty()) {
                String next = (i < ids.length - 1 ? ids[i + 1] : key);
                int aid = -42;
                Object j2 = getJson(j, id);
                if (j2 == null) {
                    boolean isInt = false;
                    try {
                        aid = Integer.parseInt(next);
                        isInt = true;
                    } catch (Exception e) {
                        LOG.debug("Insert JSON", "Looks like it is a json object not a json array.");
                    }
                    if (isInt) {
                        j2 = new JSONArray();
                    } else {
                        j2 = new JSONObject();
                    }
                    if (j instanceof JSONObject) {
                        ((JSONObject) j).put(id, j2);
                    } else if (j instanceof JSONArray) {
                        ((JSONArray) j).put(aid, j2);
                    }
                }
                j = j2;
            }
//            if (j == null)
//                j = getJson(jsonObject, id);//jsonObject.get(id);
//            else {
//                if (j instanceof JSONArray) {
//                    j = ((JSONArray) j).get(Integer.parseInt(id));
//                } else if (j instanceof JSONObject) {
//                    j = ((JSONObject) j).get(id);
//                } else
//                    throw new Exception("Failed to insert: One of the elements in the id was not a JSONArray or JSONObject.");
//            }
        }
        if (j instanceof JSONArray)
            ((JSONArray) j).put(value);
        else if (j instanceof JSONObject) {
            if (replace)
                ((JSONObject) j).put(key, value);
            else {
                try {
                    ((JSONObject) j).putOnce(key, value);
                } catch (JSONException je) {
                    LOG.warn("key already exists: " + key);
                }
            }
        } else
            throw new Exception("Failed to insert into json element: The element was not an JSONArray or JSONObject");
        return jsonObject;
    }

    private Object getJson(Object jsonObject, String id) {
        Object result = null;
        if (jsonObject!=null&&jsonObject instanceof JSONObject) {
            JSONObject jo = (JSONObject)jsonObject;
            if (id != null && !id.isEmpty() && jo.has(id)) {
                result = jo.get(id);
            }
        } else if (jsonObject != null && jsonObject instanceof JSONArray){
            try {
                JSONArray ja = (JSONArray)jsonObject;
                if (id != null && !id.isEmpty() && ja.length()>Integer.parseInt(id)) {
                    result = ja.get(Integer.parseInt(id));
                }
            } catch (Exception e) {
                //result = jsonObject;
            }
        }
        return result;
    }
}
