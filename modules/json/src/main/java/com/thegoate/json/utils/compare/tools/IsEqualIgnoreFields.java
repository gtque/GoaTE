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
import com.thegoate.annotations.IsDefault;
import com.thegoate.json.utils.fill.FillJson;
import com.thegoate.utils.compare.CompareUtil;
import com.thegoate.utils.compare.CompareUtility;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Checks to see if a permission grant matches expected.
 * The actual is expected to be a json object that has at least two fields:
 * permissionName and granted
 * the expected is expected to be a json object that contains an element with a name matching permissionName
 * and the value is the expected grant state.
 * Created by Eric Angeli on 4/9/2018.
 */
@IsDefault
@CompareUtil(operator = "isEqualIgnoreFields", type = "json")
public class IsEqualIgnoreFields extends CompareJson {
    Object expected = null;

    public IsEqualIgnoreFields(Object actual) {
        super(actual);
    }

    @Override
    protected Object processNested(Object o) {
        return null;
    }

    @Override
    public boolean evaluate() {
        JSONObject expectedDef = new JSONObject(expected.toString());
        Goate drop = new Goate();
        Object expectedJson = expectedDef.has("expected json") ? expectedDef.get("expected json") : expectedDef;
        if(expectedDef.has("_goate_ignore")) {
            JSONArray ignored = expectedDef.getJSONArray("_goate_ignore");
            defineIgnore(drop, ignored);
        }
        if (drop.size()>0) {
            expectedJson = new FillJson(expectedJson).with(drop);
            takeActionOn = new FillJson(takeActionOn).with(drop);
        }
        return new CompareJsonEqualTo(takeActionOn).to(expectedJson).evaluate();
    }

    protected void defineIgnore(Goate drop, JSONArray ignored) {
        for (int i = 0; i < ignored.length(); i++) {
            drop.put(ignored.getString(i), "drop field::");
        }
    }

    public CompareUtility actual(Object actual) {
        this.takeActionOn = actual;
        return this;
    }

    public CompareUtility to(Object expected) {
        this.expected = expected;
        return this;
    }

    public CompareUtility using(Object operator) {
        return this;
    }
}
