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
import com.thegoate.locate.Locate;
import com.thegoate.staff.Employee;
import com.thegoate.staff.GoateJob;
import com.thegoate.utils.compare.Compare;
import com.thegoate.utils.get.Get;
import com.thegoate.utils.get.NotFound;
import com.thegoate.utils.togoate.ToGoate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    protected boolean notFound = false;//return original by default.

    public static GetJsonByFieldValue value(Object value) {
        return new GetJsonByFieldValue().valueToFind(value);
    }

    public GetJsonByFieldValue returnNotFound() {
        notFound = true;
        return this;
    }

    public GetJsonByFieldValue returnOriginal() {
        notFound = false;
        return this;
    }

    public GetJsonByFieldValue root(Locate path){
        return root(path.toPath());
    }

    public GetJsonByFieldValue root(String rootPattern) {
        definition.put("root", rootPattern);
        return this;
    }

    public GetJsonByFieldValue valueToFind(Object value) {
        definition.put("value", value);
        return this;
    }

    public GetJsonByFieldValue pathPattern(Locate path){
        return pathPattern(path.toPath());
    }

    public GetJsonByFieldValue pathPattern(String pattern) {
        definition.put("key", pattern);
        return this;
    }

    public GetJsonByFieldValue fromResult() {
        return fromResult(true);
    }

    public GetJsonByFieldValue fromResult(boolean fromResult) {
        definition.put("from result", fromResult);
        return this;
    }

    public GetJsonByFieldValue from(Object source) {
        definition.put("from result", true);
        definition.put("_goate_result", source);
        return this;
    }

    public GetJsonByFieldValue json(Object json) {
        definition.put("json", json).put("from result", false);
        return this;
    }

    public GetJsonByFieldValue secondaryCondition(String secondBase, Locate pathPattern, Object value) {
        return secondaryCondition(secondBase, pathPattern.toPath(), value);
    }
    public GetJsonByFieldValue secondaryCondition(Locate secondBase, String pathPattern, Object value) {
        return secondaryCondition(secondBase.toPath(), pathPattern, value);
    }
    public GetJsonByFieldValue secondaryCondition(Locate secondBase, Locate pathPattern, Object value) {
        return secondaryCondition(secondBase.toPath(), pathPattern.toPath(), value);
    }
    public GetJsonByFieldValue secondaryCondition(String secondBase, String pathPattern, Object value) {
        List<Condition> secondary = definition.get("secondary", new ArrayList<Condition>(), List.class);
        secondary.add(new Condition(secondBase, pathPattern, value));
        return this;
    }

    @Override
    public Employee init() {
        return this;
    }

    @Override
    protected Object doWork() {
        Goate json;
        List<Condition> secondary = definition.get("secondary", new ArrayList<Condition>(), List.class);
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
            for (String key : json.filterStrict(root).keys()) {
                roots.add(json.get(key));
            }
        }
        for (Object oj : roots) {
            Goate jt = new ToGoate(oj).convert();
            Goate filtered = jt.filterStrict(field);
            for (String key : filtered.keys()) {
                if (new Compare(filtered.get(key)).to(value).using("==").evaluate()) {
                    if (checkSecond(jt, key, secondary)) {
                        foundKey = key;
                        break;
                    }
                }
            }
            if (!foundKey.isEmpty()) {
                if (root != null) {
                    theJson = oj;
                    foundKey = "";
                }
                break;
            } else if(notFound){
                theJson = new NotFound(field + "("+ value + "): not found in the json");
            }
        }
        return foundKey.isEmpty() ? theJson : findValidParent(foundKey, json);
    }

    private boolean checkSecond(Goate jt, String key, List<Condition> secondary) {
        boolean result = true;
        if (secondary != null && secondary.size() > 0) {
            for (Condition second : secondary) {
                Pattern p = Pattern.compile(second.getBase());
                Matcher m = p.matcher(key);
                if (m.find()) {
                    String keyBase = m.group();
                    String secondKey = keyBase + (keyBase.isEmpty() ? "" : ".") + second.getPathPattern();
                    Goate filtered = jt.filterStrict(secondKey);
                    result = false;
                    for (String key2 : filtered.keys()) {
                        if (new Compare(filtered.get(key2)).to(second.getValue()).using("==").evaluate()) {
                            result = true;
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    private Object findValidParent(String foundKey, Goate json) {
        Object result = null;
        if (foundKey.contains(".")) {
            foundKey = foundKey.substring(0, foundKey.lastIndexOf('.'));
        } else {
            foundKey = "";
        }
        if (!foundKey.isEmpty()) {
            result = json.get(foundKey);
            if (result == null) {
                result = findValidParent(foundKey, json);
            }
        }
        return result;
    }

    @Override
    public String[] detailedScrub() {
        String[] scrub = {"root", "path", "key", "value", "from result", "json"};
        return scrub;
    }

    private class Condition {
        protected String base;
        protected String pathPattern;
        protected Object value;

        Condition(String base, String pathPattern, Object value) {
            this.base = base;
            this.pathPattern = pathPattern;
            this.value = value;
        }

        public String getBase() {
            return base;
        }

        public void setBase(String base) {
            this.base = base;
        }

        public String getPathPattern() {
            return pathPattern;
        }

        public void setPathPattern(String pathPattern) {
            this.pathPattern = pathPattern;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
}
