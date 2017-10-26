package com.thegoate.json.utils.compare.tools;

import com.thegoate.json.JsonUtil;
import com.thegoate.utils.compare.Compare;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Base class for comparing json.
 * Created by Eric Angeli on 10/5/2017.
 */
public abstract class CompareJson extends JsonUtil {

    String json1 = null;
    String json2;
    boolean checkingContains = false;

    public CompareJson(Object val) {
        super(val);
    }

    public int comparison(String json1, String json2){
        int result = 0;
        int compare1 = compare(json1, json2);
        int compare2 = compare(json2, json1);
        result = compare1 + compare2;
        return result;
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
        result = isJsonObject ? compare((JSONObject) jsonObject, (JSONObject) jsonObject2) : compare((JSONArray) jsonObject, (JSONArray) jsonObject2);
        return result;
    }

    public int compare(JSONObject j1, JSONObject j2) {
        Iterator it = j1.keys();
        int result = 0;
        while (it.hasNext()) {
            String key = (String) it.next();
            try {
                Object o = j1.get(key);
                Object o2 = j2.get(key);
                if (o != null && o2 != null) {
                    if (o instanceof JSONObject) {
                        if (o2 instanceof JSONObject) {
                            result += compare((JSONObject) o, (JSONObject) o2);
                        } else {
                            LOG.debug(key + ": the object was a json object, but the other one was not.");
                            result++;
                        }
                    } else if (o instanceof JSONArray) {
                        if (o2 instanceof JSONArray) {
                            result += compare((JSONArray) o, (JSONArray) o2);
                        } else {
                            LOG.debug(key + ": the object was a json array, but the other one was not.");
                            result++;
                        }
                    } else if (!new Compare(o).to(o2).using("==").evaluate()){//o.equals(o2)) {
//                        if (!CompareNumeric.isNumeric("" + o) || (new CompareNumeric().actual(o).notEqualTo("" + o2))) {
                            LOG.debug(key + ": the objects values were not equal. " + o + " != " + o2);
                            result++;
//                        }
                    }
                } else if (o == null && o2 == null) {
                    LOG.debug(key + ": both objects were null");
                } else if (o != null) {
                    LOG.debug(key + ": the second object was null");
                    result++;
                } else if (o2 != null) {
                    LOG.debug(key + ": the first object was null");
                    result++;
                }
            } catch (JSONException je) {
                LOG.warn(key + ": there was a json exception: " + je.getMessage(), je);
                result++;
            }
        }
        return result;
    }

    public int compare(JSONArray j1, JSONArray j2) {
        int result = 0;
        if (j1.length() != j2.length()) {
            if (!checkingContains) {
                result++;
                LOG.debug("comparing two arrays of different lengths.");
            }
        }
        for (int i = 0; i < j1.length(); i++) {
            Object o = j1.get(i);
            boolean found = false;
            for (int i2 = 0; i2 < j2.length(); i2++) {
                int r2 = 0;
                Object o2 = j2.get(i2);
                if (o != null && o2 != null) {
                    if (o instanceof JSONObject) {
                        if (o2 instanceof JSONObject) {
                            r2 = compare((JSONObject) o, (JSONObject) o2);
                        } else {
                            LOG.debug(i2 + ": the object was a json object, but the other one was not.");
                            r2++;
                        }
                    } else if (o instanceof JSONArray) {
                        if (o2 instanceof JSONArray) {
                            r2 = compare((JSONArray) o, (JSONArray) o2);
                        } else {
                            LOG.debug(i2 + ": the object was a json array, but the other one was not.");
                            r2++;
                        }
                    } else if (!new Compare(o).to(o2).using("==").evaluate()){//o.equals(o2)) {
//                        if (!CompareNumeric.isNumeric("" + o) || (new CompareNumeric().actual("" + o).notEqualTo("" + o2))) {
                            LOG.debug(i2 + ": the objects values were not equal. " + o + " != " + o2);
                            r2++;
//                        }
                    }
                } else if (o != null) {
                    LOG.debug(i2 + ": the second object was null.");
                    r2++;
                } else if (o2 != null) {
                    LOG.debug(i2 + ": the object was null.");
                    r2++;
                }
                if (r2 == 0) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                LOG.debug("the object was not found in the other array.");
                result++;
            }
        }
        return result;
    }
}
