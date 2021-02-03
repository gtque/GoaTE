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

import com.thegoate.annotations.GoateDescription;
import com.thegoate.staff.Employee;
import com.thegoate.staff.GoateJob;
import org.json.JSONArray;

/**
 * Does an api call.
 * Created by Eric Angeli on 5/22/2017.
 */
@GoateJob(jobs = {"init data"})
@GoateDescription(description = "A job for initializing data," +
        "\n especially for anything that needs to be init or set on first.",
    parameters = {"init = [], a json array of strings representing the fields to init."})
public class InitData extends Employee {

    @Override
    public String[] detailedScrub() {
        String[] scrub = {"init", "carryover"};
        return scrub;
    }

    @Override
    public Employee init() {
        return this;
    }

    @Override
    protected Object doWork() {
        String carryOver = ""+definition.get("init", "[]");
        JSONArray ja =  new JSONArray(carryOver);
        for(int i = 0; i<ja.length(); i++){
            String key = ja.getString(i);
            definition.get(key);
        }
        definition.put("carryover", carryOver);
        return this;
    }
}
