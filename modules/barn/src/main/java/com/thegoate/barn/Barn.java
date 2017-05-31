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
package com.thegoate.barn;

import com.thegoate.Goate;
import com.thegoate.annotations.AnnotationFactory;
import com.thegoate.barn.data.BarnDataLoader;
import com.thegoate.expect.ExpectEvaluator;
import com.thegoate.expect.ExpectationThreadBuilder;
import com.thegoate.staff.Employee;
import com.thegoate.staff.GoateJob;
import com.thegoate.testng.TestNGEngine;
import com.thegoate.utils.togoate.ToGoate;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.InvocationTargetException;

import static org.testng.Assert.assertTrue;

/**
 * The base barn class for running complete data driven tests.
 * Created by Eric Angeli on 5/22/2017.
 */
public class Barn extends TestNGEngine {
    String label = "barn";//label will be used in the future, but not currently used.
    String baseTestCaseDir = "testcases";

    public Barn(){}

    public Barn(Goate data){
        super(data);
    }

    @Override
    public void defineDataLoaders() {
        runData.put("dl##", new BarnDataLoader().testCaseDirectory(baseTestCaseDir));
    }

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        LOG.debug("----running setup----");
        steps("setup");
        LOG.debug("----finished setup----");
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        LOG.debug("----running cleanup----");
        steps("cleanup");
        LOG.debug("----finished cleanup----");
    }

    protected void steps(String step){
        Goate sup = new ToGoate(data.get(step, "[]")).convert();
        if(sup!=null) {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                if (sup.keys().contains("" + i)) {
                    Goate d = new ToGoate(sup.get("" + i, "{}")).convert();
                    doWork(d);
                } else {
                    break;
                }
            }
        }
    }

    public void execute(){
        LOG.debug("----starting execution----");
        doWork(data);
        LOG.debug("----finished execution----");
        LOG.debug("----evaluating expectations----");
        try {
            evaluateExpectations();
        }catch(Throwable t){
            throw t;
        }finally {
            LOG.debug("----finished expectations----");
        }
    }

    protected void doWork(Goate d){
        if(d!=null) {
            String type = "" + d.get("job", "api");
            AnnotationFactory af = new AnnotationFactory();
            try {
                Employee worker = (Employee) af.annotatedWith(GoateJob.class).find(type).using("jobs").build();
                worker.init(d);
                put("_goate_result", worker.doWork());
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                LOG.error("problem finding something to execute a " + type + "\nmake sure you have an implementation library included.", e);
            }
        }
    }

    protected void evaluateExpectations(){
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect(data.filter("expect."));
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        assertTrue(result, ev.failed());
    }
}
