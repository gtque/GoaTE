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
import com.thegoate.barn.staff.StepsExecutor;
import com.thegoate.expect.ExpectEvaluator;
import com.thegoate.expect.ExpectationThreadBuilder;
import com.thegoate.staff.Employee;
import com.thegoate.staff.GoateJob;
import com.thegoate.testng.TestNGEngine;
import com.thegoate.utils.togoate.ToGoate;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.InvocationTargetException;

import static org.testng.Assert.assertTrue;

/**
 * The base barn class for running complete data driven tests.
 * Created by Eric Angeli on 5/22/2017.
 */
public class Barn extends TestNGEngine {
    protected String label = "barn";//label will be used in the future, but not currently used.
    protected String baseTestCaseDir = "testcases";
    ExpectEvaluator ev = null;
    Throwable preFailure = null;

    public Barn() {
        super();
        includeClassMethodInName = false;
    }

    public Barn(Goate data) {
        super(data);
        includeClassMethodInName = false;
    }

    @Override
    public void defineDataLoaders() {
        runData.put("dl##", new BarnDataLoader().testCaseDirectory(baseTestCaseDir).groups(label));
    }

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        LOG.debug("----running setup----");
        try {
            steps("setup");
        } catch (Throwable t) {
            preFailure = t;
        }
        LOG.debug("----finished setup----");
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        LOG.debug("----running cleanup----");
        try {
            steps("cleanup");
        } catch (Throwable t) {
            LOG.warn(getTestName(), "Cleanup had a failure: " + t.getMessage(), t);
            throw t;
        }
        LOG.debug("----finished cleanup----");
    }

    protected void steps(String step) {
        if (data.size() > 0) {
            StepsExecutor dosteps = new StepsExecutor(data).notOrdered();
            String steps = ""+data.get(step,"[]");
            data.put(step+"_result##",dosteps.doSteps(steps));
        }
    }

    public void execute() {
        if (data.size() == 0) {
            LOG.skip(getTestName(), "Skipping test because there is nothing to execute.");
            throw new SkipException("No run data.");
        }
        if (preFailure != null) {
            LOG.skip(getTestName(), "Skipping test because pre-steps failed: " + preFailure.getMessage());
            throw new SkipException(preFailure.getMessage());
        } else {
            LOG.debug("----starting execution----");
            try {
                StepsExecutor steps = new StepsExecutor(data).notOrdered();
                data.put("_goate_result",steps.doSteps(null).get("0"));
            } catch (Throwable t) {
                LOG.fatal(getTestName(), "Encountered a problem executing the test: " + t.getMessage(), t);
                throw t;
            } finally {
                LOG.debug("----finished execution----");
                LOG.debug("----evaluating expectations----");
            }
            try {
                evaluateExpectations();
            } catch (Throwable t) {
//                LOG.warn(getTestName(), "Failed: " + t.getMessage());
                throw t;
            } finally {
                if (ev != null) {
                    for (Goate p : ev.passes()) {
                        LOG.pass(getTestName(), "PASSED: " + p.toString());
                    }
                    for (Goate f : ev.fails()) {
                        LOG.fail(getTestName(), "FAILED: " + f.toString());
                    }
                }
                LOG.debug("----finished expectations----");
            }
        }
    }

    protected void evaluateExpectations() {
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect(data.filter("expect."))
                .timeout(Long.parseLong(""+data.get("expect.timeout",500L)))
                .period(Long.parseLong(""+data.get("expect.period",50L)));
        ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        assertTrue(result, ev.failed());
    }
}
