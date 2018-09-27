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

/**
 * Gets a specific result number.
 * Useful when used as part of expectations for an integration or multistep test case.
 * Created by Eric Angeli on 8/27/2018.
 */
@GoateJob(jobs = {"json by value"})
@GoateDescription(description = "Find the value in the defined json, finds and returns the first match.",
        parameters = {"value: the value to find, required",
                "path: pattern for the field(s) to check, required",
                "from result: true if from result, optional, defaults to false",
                "json: the json to look in, required if not from result",})
public class GetJsonByFieldValue extends Employee {

    @Override
    public Employee init() {
        return this;
    }

    @Override
    protected Object doWork() {
        Goate json;
        String foundKey = "";
        Object theJson = definition.get("json", "{}");
        boolean fromResult = definition.get("from result", false, Boolean.class);
        Object value = definition.get("value", null);
        String field = definition.get("key", ".*", String.class);
        if (!fromResult) {
            json = new JSONToGoate(theJson).convert();
        } else {
            json = new JSONToGoate(new Get("body as a string").from(data.get("_goate_result", "{}"))).convert();
        }
        Goate filtered = json.filter(field);
        for (String key : filtered.keys()) {
            if (filtered.get(key) != null && filtered.get(key).equals(value)) {
                foundKey = key;
                break;
            }
        }
        if (foundKey.contains(".")) {
            foundKey = foundKey.substring(0, foundKey.lastIndexOf("."));
        } else {
            foundKey = "";
        }
        return foundKey.isEmpty() ? theJson : json.get(foundKey);
    }

    @Override
    public String[] detailedScrub() {
        String[] scrub = {};
        return scrub;
    }
}
