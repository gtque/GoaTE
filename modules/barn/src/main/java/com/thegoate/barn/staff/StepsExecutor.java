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
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.staff.Employee;
import com.thegoate.staff.GoateJob;
import com.thegoate.utils.get.Get;
import com.thegoate.utils.get.NotFound;
import com.thegoate.utils.togoate.ToGoate;

import java.lang.reflect.InvocationTargetException;

/**
 * Executes the described steps.
 * Created by Eric Angeli on 6/28/2017.
 */
public class StepsExecutor {
    BleatBox LOG = BleatFactory.getLogger(getClass());
    boolean ordered = false;
    boolean override = true;//default to true, then you specify, using an array, what you want to come from the step definition.
    Goate data = new Goate();

    public StepsExecutor(Goate data) {
        this.data = data;
    }

    public StepsExecutor ordered(boolean ordered) {
        this.ordered = ordered;
        return this;
    }

    public StepsExecutor ordered() {
        return ordered(true);
    }

    public StepsExecutor notOrdered() {
        return ordered(false);
    }

    public StepsExecutor override(boolean override){
        this.override = override;
        return this;
    }

    public StepsExecutor overrideAll(){
        return override(false);//this uses negative logic, so false here means the data in the step wins.
    }

    public StepsExecutor doNotOverrideAll(){
        return override(true);//this means the data from the parent wins.
    }

    public Goate doSteps(String steps) {
        Goate result = new Goate();
        if (steps != null) {
            Goate sup = new ToGoate(steps).convert();
            if (sup != null) {
                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    Goate d = findStep(i, sup);//new ToGoate(sup.get("" + i, null)).convert();
                    if (d != null) {
                        Object r = doWork(d);
                        result.put("##", r);
                    } else {
                        break;
                    }
                }
            }
        } else {
            if (data.get("job") != null) {
                result.put("##", doWork(data));
            }
        }
        return result;
    }

    protected Object doWork(Goate d) {
        Object result = null;
        if (d != null) {
            String type = "" + d.get("job", "api");
            AnnotationFactory af = new AnnotationFactory();
            try {
                Employee worker = (Employee) af.annotatedWith(GoateJob.class).find(type).using("jobs").build();
                Goate scrubbed = worker.scrub(data);//scrubb required fields
                Goate fullData = new Goate().merge(d, override);
                fullData.merge(scrubbed, override);//merge the parent data with the step data based on the selected override scheme.
                Object overrides = fullData.get("override", "[]");//process the override values.
                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    Object key = new Get(""+i).from(overrides);
                    if (key != null && !(key instanceof NotFound)) {
                        fullData.put("" + key, d.get("" + key, "null::"));
                    }else{
                        break;
                    }
                }
                worker.init(fullData);
                result = worker.work();
                Object carryover = fullData.get("carryover", "[]");//process the override values.
                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    Object key = new Get(""+i).from(carryover);
                    if (key != null && !(key instanceof NotFound)) {
                        data.put("" + key, fullData.get("" + key, "null::"));
                    }else{
                        break;
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                LOG.error("Step doWork","problem finding something to execute a " + type + "\nmake sure you have an implementation library included.", e);
            }
        }
        return result;
    }

    protected Goate findStep(int index, Goate sup) {
        Goate found = null;
        if (ordered) {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                Goate d = new ToGoate(sup.get("" + i, null)).convert();
                if (d != null) {
                    if (("" + d.get("#")).equals("" + index)) {
                        found = d;
                        break;
                    }
                } else {
                    break;
                }
            }
        } else {
            found = new ToGoate(sup.get("" + index, null)).convert();
        }
        return found;
    }

}
