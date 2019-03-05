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
package com.thegoate.json.staff;

import com.thegoate.Goate;
import com.thegoate.annotations.GoateDescription;
import com.thegoate.json.utils.togoate.JSONToGoate;
import com.thegoate.staff.Employee;
import com.thegoate.staff.GoateJob;
import com.thegoate.utils.get.Get;
import com.thegoate.utils.togoate.ToGoate;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds a json field by value.
 * Created by Eric Angeli on 8/27/2018.
 */
@GoateJob(jobs = {"json by value"})
@GoateDescription(description = "Find the value in the defined json, finds and returns the first match.",
        parameters = {"value: the value to find, required",
                "path: pattern for the field(s) to check, required",
                "from result: true if from result, optional, defaults to false",
                "json: the json to look in, required if not from result",})
public class GetJsonByFieldValue extends Employee {

    public static GetJsonByFieldValue value(Object value){
        return new GetJsonByFieldValue().valueToFind(value);
    }

    public GetJsonByFieldValue root(String rootPattern){
        definition.put("root", rootPattern);
        return this;
    }

    public GetJsonByFieldValue valueToFind(Object value){
        definition.put("value", value);
        return this;
    }

    public GetJsonByFieldValue pathPattern(String pattern){
        definition.put("key", pattern);
        return this;
    }

    public GetJsonByFieldValue fromResult(){
        return fromResult(true);
    }

    public GetJsonByFieldValue fromResult(boolean fromResult){
        definition.put("from result", fromResult);
        return this;
    }

    public GetJsonByFieldValue from(Object source){
        definition.put("from result", true);
        definition.put("_goate_result", source);
        return this;
    }

    public GetJsonByFieldValue json(Object json){
        definition.put("json", json).put("from result", false);
        return this;
    }

    @Override
    public Employee init() {
        return this;
    }

    @Override
    protected Object doWork() {
        Goate json;
        String foundKey = "";
        String root = definition.get("root", null, String.class);
        Object theJson = definition.get("json", "{}");
        boolean fromResult = definition.get("from result", false, Boolean.class);
        Object value = definition.get("value", null);
        String field = definition.get("key", definition.get("node.path", ".*"), String.class);
        if (!fromResult) {
            json = new JSONToGoate(theJson).convert();
        } else {
            json = new JSONToGoate(new Get("body as a string").from(definition.get("_goate_result", "{}"))).convert();
        }
        List<Object> roots = new ArrayList<>();
        if (root == null) {
            roots.add(json);
        } else {
            for(String key:json.filterStrict(root).keys()){
                roots.add(json.get(key));
            }
        }
        for (Object oj : roots) {
            Goate jt = new ToGoate(oj).convert();
            Goate filtered = jt.filterStrict(field);
            for (String key : filtered.keys()) {
                if (filtered.get(key) != null && filtered.get(key).equals(value)) {
                    foundKey = key;
                    break;
                }
            }
            if(!foundKey.isEmpty()) {
                if (root == null) {
                    if (foundKey.contains(".")) {
                        foundKey = foundKey.substring(0, foundKey.lastIndexOf("."));
                    } else {
                        foundKey = "";
                    }
                } else {
                    theJson = oj;
                    foundKey = "";
                }
                break;
            }
        }
        return foundKey.isEmpty() ? theJson : json.get(foundKey);
    }

    @Override
    public String[] detailedScrub() {
        String[] scrub = {"root", "path", "key", "value", "from result", "json"};
        return scrub;
    }
}
