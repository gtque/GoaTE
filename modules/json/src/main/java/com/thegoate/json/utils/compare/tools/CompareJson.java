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
package com.thegoate.json.utils.compare.tools;

import com.thegoate.Goate;
import com.thegoate.json.JsonUtil;
import com.thegoate.utils.compare.Compare;
import com.thegoate.utils.compare.CompareUtility;
import com.thegoate.utils.get.NotFound;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import static com.thegoate.dsl.words.EutConfigDSL.eut;

/**
 * Base class for comparing json.
 * Created by Eric Angeli on 10/5/2017.
 */
public abstract class CompareJson extends JsonUtil implements CompareUtility {

    String json1 = null;
    String json2;
    boolean checkingContains = true;
    boolean secondCheck = false;
    boolean onSecondCheck = false;
    Goate arrayHealth = new Goate();

    public CompareJson(Object val) {
        super(val);
    }

    @Override
    protected void init(Object val) {
        processNested = false;
        super.init(val);
    }

    public int comparison(String json1, String json2) {
        checkingContains = false;
        secondCheck = false;
        onSecondCheck = false;
        int compare1 = compare(json1, json2);
        onSecondCheck = true;
        secondCheck = !Boolean.parseBoolean(eut("json.compare.strict",""+false));
        int compare2 = compare(json2, json1);
        return compare1 + compare2;
    }

    /**
     * Compares json1 to json2, if json1 equals or is in json2, returns 0.
     * Otherwise an int representing the difference between them is returned.
     *
     * @param json1s the thing to be compared
     * @param json2s the thing to be compared to
     * @return an integer representing the result of the comparison, 0 means equal, anything else means not equal.
     */
    public int compare(Object json1s, Object json2s) {
        this.json1 = "" + json1s;
        this.json2 = "" + json2s;
        if (json1 == null) {
            return 42;
        }
        if (json2 == null) {
            return 43;
        }
        int result = 0;
        boolean isJsonObject = true;
        Object jsonObject;
        try {
            if (json1.startsWith("[")) {
                isJsonObject = false;
                jsonObject = new JSONArray(json1);
            } else {
                jsonObject = new JSONObject(json1);
            }
        } catch (JSONException je) {
            return 1;
        }
        Object jsonObject2;
        try {
            if (isJsonObject) {
                if (json2.startsWith("{")) {
                    jsonObject2 = new JSONObject(json2);
                } else {
                    //result = -42;
                    return 1;
                }
            } else {
                if (json2.startsWith("[")) {
                    jsonObject2 = new JSONArray(json2);
                } else {
                    //result = -42;
                    return 1;
                }
            }
        } catch (JSONException je) {
            return 1;
        }
        //int compare2 = isJsonObject ? compare((JSONObject) jsonObject2, (JSONObject) jsonObject) : compare((JSONArray) jsonObject2, (JSONArray) jsonObject);
        result = isJsonObject ? compare((JSONObject) jsonObject, (JSONObject) jsonObject2, "", false) : compare((JSONArray) jsonObject, (JSONArray) jsonObject2, "");
        return result;
    }

    public int compare(JSONObject j1, JSONObject j2, String baseKey, boolean checkingArray) {
        Iterator it = j1.keys();
        int result = 0;
        while (it.hasNext()) {
            String key = (String) it.next();
            try {
                Object o = j1.get(key);
                Object o2 = new NotFound("Field Not found");
                try{
                    o2 = j2.get(key);
                } catch (Exception e){
                    LOG.debug("Compare Json", "Field not found in expected: " + key);
                }
                if ((o != null && o2 != null)&&(o != JSONObject.NULL && o2 != JSONObject.NULL)) {
                    if (o instanceof JSONObject) {
                        if (o2 instanceof JSONObject) {
                            result += compare((JSONObject) o, (JSONObject) o2, baseKey + key + ".", checkingArray);
                        } else {
                            if(!checkingArray&&!secondCheck) {
                                health.put("mismatched##", baseKey + key + ": " + o + " != " + o2);
                            }
                            LOG.debug(key + ": the object was a json object, but the other one was not.");
                            result++;
                        }
                    } else if (o instanceof JSONArray) {
                        if (o2 instanceof JSONArray) {
                            result += compare((JSONArray) o, (JSONArray) o2, baseKey + key + ".");
                        } else {
                            if(!checkingArray&&!secondCheck) {
                                health.put("mismatched##", baseKey + key + ": " + o + " != " + o2);
                            }
                            LOG.debug(key + ": the object was a json array, but the other one was not.");
                            result++;
                        }
                    } else if (!new Compare(o).to(o2).using("==").evaluate()) {//o.equals(o2)) {
//                        if (!CompareNumeric.isNumeric("" + o) || (new CompareNumeric().actual(o).notEqualTo("" + o2))) {
                        if(!checkingArray&&!secondCheck) {
                            health.put("mismatched##", baseKey + key + ": " + o + " != " + o2);
                        }
                        LOG.debug(key + ": the objects values were not equal. " + o + " != " + o2);
                        result++;
//                        }
                    }
                } else if ((o == null && o2 == null)||(o == JSONObject.NULL && o2 == JSONObject.NULL)) {
                    LOG.debug(key + ": both objects were null");
                } else if (o != null || o != JSONObject.NULL) {
                    if(!checkingArray&&!secondCheck) {
                        health.put("mismatched##", baseKey + key + ": " + o + " != " + o2);
                    }
                    LOG.debug(key + ": the second object was null");
                    result++;
                } else {//o2!=null && o1==null
                    if(!checkingArray&&!secondCheck) {
                        health.put("mismatched##", baseKey + key + ": " + o + " != " + o2);
                    }
                    LOG.debug(key + ": the first object was null");
                    result++;
                }
            } catch (JSONException je) {
                if(!checkingArray) {
                    String error = je.getMessage();
                    if(error.lastIndexOf("\n")==error.length()-1){
                        error = error.substring(0,error.length()-1);
                    }
                    health.put("json exception##", baseKey + key + ": " + error);
                }
                LOG.debug(key + ": there was a json exception(but this may not be an error): " + je.getMessage(), je);
                result++;
            }
        }
        return result;
    }

    public int compare(JSONArray j1, JSONArray j2, String baseKey) {
        int result = 0;
        if (j1.length() != j2.length()) {
            if (!checkingContains&&!secondCheck) {
                arrayHealth.put("mismatched##", "json arrays different length: " + j1.length() + " != " + j2.length());
                result++;
                LOG.debug("comparing two arrays of different lengths, but this isn't a contains check so they should be equal.");
            }
        }
        if(result == 0) {
            for (int i = 0; i < j1.length(); i++) {
                Object o = j1.get(i);
                boolean found = false;
                for (int i2 = 0; i2 < j2.length(); i2++) {
                    int r2 = 0;
                    Object o2 = j2.get(i2);
                    if ((o != null && o2 != null) && (o != JSONObject.NULL && o2 != JSONObject.NULL)) {
                        if (o instanceof JSONObject) {
                            if (o2 instanceof JSONObject) {
                                r2 = compare((JSONObject) o, (JSONObject) o2, baseKey + i + ".", true);
                            } else {
                                LOG.debug(i2 + ": the object was a json object, but the other one was not.");
                                r2++;
                            }
                        } else if (o instanceof JSONArray) {
                            if (o2 instanceof JSONArray) {
                                r2 = compare((JSONArray) o, (JSONArray) o2, baseKey + i + ".");
                            } else {
                                LOG.debug(i2 + ": the object was a json array, but the other one was not.");
                                r2++;
                            }
                        } else if (!new Compare(o).to(o2).using("==").evaluate()) {//o.equals(o2)) {
//                        if (!CompareNumeric.isNumeric("" + o) || (new CompareNumeric().actual("" + o).notEqualTo("" + o2))) {
                            LOG.debug(i2 + ": the objects values were not equal. " + o + " != " + o2);
                            r2++;
//                        }
                        }
                    } else if (o != null || o != JSONObject.NULL) {
                        LOG.debug(i2 + ": the second object was null.");
                        r2++;
                    } else if (o2 != null || o2 != JSONObject.NULL) {
                        LOG.debug(i2 + ": the object was null.");
                        r2++;
                    }
                    if (r2 == 0) {
                        found = true;
                        break;
                    }
                }
                if (!found && !secondCheck) {
                    health.put("not found##", baseKey + i + ":" + o + ">not in " + (onSecondCheck ? "actual" : "expected"));
                    if(arrayHealth.size()>0){
                        health.put("array mismatch##", arrayHealth);
                    }
                    arrayHealth = new Goate();
                    LOG.debug("the object was not found in the other array.");
                    result++;
                }
            }
        }
        return result;
    }
}
