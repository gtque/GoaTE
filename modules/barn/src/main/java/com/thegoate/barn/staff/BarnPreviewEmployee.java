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
package com.thegoate.barn.staff;

import com.thegoate.Goate;
import com.thegoate.annotations.AnnotationFactory;
import com.thegoate.barn.data.BarnDataLoader;
import com.thegoate.json.utils.tojson.GoateToJSON;
import com.thegoate.rest.staff.ApiEmployee;
import com.thegoate.staff.Employee;
import com.thegoate.staff.GoateJob;
import com.thegoate.utils.get.GetFileAsString;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

/**
 * Generates a preview of the evaluated barn json.
 * Created by Eric Angeli on 5/22/2017.
 */
@GoateJob(jobs = {"barn preview"})
public class BarnPreviewEmployee extends Employee {
    Object barnSource = null;
    String barnRoot = "";

    @Override
    public Employee init() {
        barnSource = data.get(parameterName("barn source"), "{}");
        barnRoot = data.get(parameterName("barn root"),"", String.class);
        return this;
    }

    @Override
    protected Object doWork() {
        Object result = null;
        if (barnSource instanceof File) {
            barnSource = new GetFileAsString(barnSource).from("file::");
        }
        if (barnSource instanceof JSONObject) {
            try {
                barnSource = barnSource.toString();
            } catch (JSONException je){
                LOG.error("Barn Preview", "Problem converting to string from json: " + je.getMessage());
            }
        }
        if (barnSource instanceof String) {
            BarnDataLoader bdl = new BarnDataLoader();
            bdl.testCaseDirectory(barnRoot);
            Goate preview = bdl.loadBarn(barnSource, null);
            result = new GoateToJSON(preview).convertStrict();
        }
        return result;
    }

    @Override
    public String[] detailedScrub() {
        String[] scrub = {};
        return scrub;
    }
}
