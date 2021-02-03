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
import com.thegoate.expect.ExpectedBuilder;
import com.thegoate.json.utils.fill.FillJson;
import com.thegoate.json.utils.tojson.ToJson;
import com.thegoate.utils.compare.Compare;
import com.thegoate.utils.compare.CompareUtil;
import com.thegoate.utils.compare.CompareUtility;
import com.thegoate.utils.togoate.ToGoate;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Checks to see if a permission grant matches expected.
 * The actual is expected to be a json object that has at least two fields:
 * permissionName and granted
 * the expected is expected to be a json object that contains an element with a name matching permissionName
 * and the value is the expected grant state.
 * Created by Eric Angeli on 4/9/2018.
 */
@IsDefault
@CompareUtil(operator = "isEqualIgnoreFields", type = JSONObject.class)
public class EqualIgnoreFields extends CompareJson implements ExpectedBuilder {
    Object expected = null;
    Object fromExpected = null;

    public EqualIgnoreFields(Object actual) {
        super(actual);
    }

    @Override
    public void init(Object val){
        processNested = false;
        super.init(val);
    }

    @Override
    protected Object processNested(Object o) {
        return null;
    }

    @Override
    public boolean evaluate() {
        Goate expectedDef = new ToGoate(expected).convert();
        Goate drop = new Goate();
        JSONArray ignored = new JSONArray("" + expectedDef.get("_goate_ignore", "[]"));
        expectedDef.merge(data, false);
        Object expectedJson = expectedDef.get("expected json", new JSONObject(expected.toString()));
        defineIgnore(drop, ignored);
        Object tao = null;
        if (drop.size() > 0) {
            expectedJson = new FillJson(expectedJson).with(drop);
            tao = new ToJson(takeActionOn).convert();
            tao = new FillJson(tao).with(drop);
        }
        CompareUtility compare = new Compare(tao).to(expectedJson).using("==");
        boolean result = compare.evaluate();
        health = compare.healthCheck();
        return result;
    }

    protected void defineIgnore(Goate drop, JSONArray ignored) {
        for (int i = 0; ignored != null && i < ignored.length(); i++) {
            drop.put(ignored.getString(i), "drop field::");
        }
        drop.put("_goate_ignore", "drop field::");
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

    public static EqualIgnoreFields expected(Object expected) {
        return new EqualIgnoreFields(null).setExpected(expected);
    }

    public EqualIgnoreFields fromExpected(Object fromExpected){
        this.fromExpected = fromExpected;
        return this;
    }

    @Override
    public Object fromExpected(){
        return this.fromExpected;
    }
    JSONArray ignore = new JSONArray();

    public EqualIgnoreFields setExpected(Object expected) {
        this.expected = expected;
        return this;
    }

    public EqualIgnoreFields ignoreField(String field) {
        ignore.put(field);
        return this;
    }

    public EqualIgnoreFields ignoreFields(List<?> fields) {
        fields.forEach(field -> ignore.put(field));
        return this;
    }

    public EqualIgnoreFields ignoreFields(JSONArray fields) {
        return ignoreFields(fields.toList());
    }

    public Object generateExpected() {
        JSONObject jo = new JSONObject();
        jo.put("expected json", expected);
        jo.put("_goate_ignore", ignore);
        return jo;
    }
}
