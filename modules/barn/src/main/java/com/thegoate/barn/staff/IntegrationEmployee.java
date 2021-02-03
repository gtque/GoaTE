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
import com.thegoate.staff.Employee;
import com.thegoate.staff.GoateJob;

/**
 * Runs a series of steps.
 * Created by Eric Angeli on 5/22/2017.
 */
@GoateJob(jobs = {"integration", "steps"})
public class IntegrationEmployee extends Employee {

    String steps = "";
    Goate results = new Goate();

    @Override
    public Employee init() {
        steps = "" + definition.get("steps", "[]");//default to get if not specified.
        return this;
    }

    @Override
    protected Object doWork() {
        StepsExecutor dosteps = new StepsExecutor(definition).ordered().override(!Boolean.parseBoolean(""+definition.get("doOverride","false")));
        Goate r = dosteps.doSteps(steps);
        return r;
    }

    @Override
    public String[] detailedScrub(){
        String[] scrub = {"steps", "doOverride"};
        return scrub;
    }
}
