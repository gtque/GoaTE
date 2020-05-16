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
package com.thegoate.testng;

import com.thegoate.Goate;
import com.thegoate.expect.ExpectEvaluator;
import com.thegoate.expect.ExpectThreadExecuter;
import com.thegoate.expect.Expectation;
import com.thegoate.expect.ExpectationThreadBuilder;
import com.thegoate.expect.conditional.ConditionalBuilder;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.metrics.Stopwatch;
import com.thegoate.statics.StaticScrubber;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.xml.XmlTest;

import java.lang.reflect.Method;
import java.util.List;

import static com.thegoate.dsl.words.EutConfigDSL.eut;
import static org.testng.Assert.assertTrue;


/**
 * The base class for writing TestNG classes.
 * Created by Eric Angeli on 5/11/2017.
 */
@Listeners({TestNGEvaluateListener.class})
public abstract class TestNGEngine implements ITest, TestNG {
    private final String expectSeparator = "\n--------------------\n";
    private Class testClass = null;
    protected BleatBox LOG = BleatFactory.getLogger(getClass());
    protected Goate data = null;
    protected Goate runData;
    protected Goate constantData;
    protected int runNumber = 0;
    protected String scenario = "";
    protected String methodName = "";
    protected boolean muteFrom = false;
    protected boolean includeClassMethodInName = true;
    ITestContext testContext = null;
    XmlTest xt = null;
    protected ExpectationThreadBuilder etb;
    protected ExpectEvaluator ev;

    public static int number = 0;

    public TestNGEngine() {
        setData(null);
    }

    public TestNGEngine(Goate data) {
        init(data);
    }

    public void setLOG(BleatBox box) {
        LOG = box;
    }

    @BeforeMethod(alwaysRun = true)
    public void startUp(Method method) {
        methodName = method.getName();
        if (data != null && data.get("lap", null) != null) {
            Stopwatch.global.start(data.get("lap", Thread.currentThread().getName(), String.class));
        }
        etb = new ExpectationThreadBuilder(data);
        String startMessage = "\n" +
                "***************************Starting Up***************************\n" +
                "*\t" + getTestName() + "\t*\n";
        startMessage += data.toString("*\t", "\t*");
        startMessage += "\n*****************************************************************";
        TestMap.tests.put(getTestName(), this);
        LOG.info("Start Up", startMessage);
    }

    @AfterMethod(alwaysRun = true)
    public void finishUp(Method method) {
        StaticScrubber scrubber = new StaticScrubber();
        if (data != null && data.get("lap", null) != null) {
            Stopwatch.global.stop(data.get("lap", Thread.currentThread().getName(), String.class));
        }
        scrubber.scrub();
        String endMessage = "\n" +
                "*****************************************************************\n" +
                "*\t" + getTestName() + "\t*\n";
        endMessage += data.toString("*\t", "\t*");
        endMessage += "\n***************************Finished Up***************************";
        LOG.info("Shut Down", endMessage);
    }

    public void init(Goate data) {
        setData(data);
        String sKey = getData().findKeyIgnoreCase("Scenario");
        setScenario(get(sKey, "empty::", String.class));
        bumpRunNumber();
    }

    @Override
    public String getTestName() {
        StringBuilder name = new StringBuilder("");
        if (includeClassMethodInName) {
            name.append((testClass==null?getClass().getCanonicalName():testClass.getCanonicalName())+ ":" + methodName + ":");
        }
        name.append(scenario);
        name.append("(" + runNumber + ")");
        return name.toString();
    }


    @Override
    @DataProvider(name = "dataLoader")
    public Object[][] dataLoader(ITestNGMethod method, ITestContext context) throws Exception {
        number = 0;//resets the count, assumes TestNG loads all the runs before processing the next class.
        this.testContext = context;
        if (context != null) {
            xt = context.getCurrentXmlTest();
        }
        initDataLoaders();
        return TestNGRunFactory.loadRuns(getRunDataLoader(), getConstantDataLoader(), true, testContext.getIncludedGroups(), testContext.getExcludedGroups());
    }

    public void initDataLoaders() {
        if (runData == null) {
            runData = new Goate();
        }
        if (constantData == null) {
            constantData = new Goate();
        }
        defineDataLoaders();
    }

    @Override
    public void defineDataLoaders(Goate runData, Goate constantData) {
        this.runData = runData;
        this.constantData = constantData;
    }

    @Override
    public Goate getRunDataLoader() {
        return this.runData;
    }

    @Override
    public Goate getConstantDataLoader() {
        return this.constantData;
    }

    @Override
    public Goate getData() {
        return this.data;
    }

    @Override
    public void setData(Goate data) {
        if (data == null) {
            data = new Goate();
        }
        this.data = data;
    }

    @Override
    public void setRunNumber(int number) {
        runNumber = number;
    }

    @Override
    public void bumpRunNumber() {
        number++;
        setRunNumber(number);
    }

    @Override
    public int getRunNumber() {
        return runNumber;
    }

    @Override
    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    @Override
    public String getScenario() {
        return scenario;
    }

    @Override
    public Object get(String key) {
        return get(key, null);
    }

    @Override
    public Object get(String key, Object def) {
        return get(key, def, Object.class);
    }

    @Override
    public <T> T get(String key, Object def, Class<T> type) {
        return data.get(key, def, true, type);
    }

    @Override
    public TestNG put(String key, Object val) {
        data.put(key, val);
        return this;
    }

    @Override
    public TestNGEngine expect(Expectation expectation) {
        if(etb.isBuilt()){
            clearExpectations();
        }
        if (etb != null) {
            etb.expect(expectation);
        }
        return this;
    }

    @Override
    public TestNGEngine expect(ConditionalBuilder conditionalBuilder) {
        return expect(conditionalBuilder.build());
    }

    @Override
    public TestNGEngine expect(List<Expectation> expectationList) {
        expectationList.forEach(this::expect);
        return this;
    }

    @Override
    public TestNGEngine evalPeriod(long periodMS) {
        if (etb != null) {
            etb.period(periodMS);
        }
        return this;
    }

    @Override
    public TestNGEngine evalTimeout(long timeoutMS) {
        if (etb != null) {
            etb.timeout(timeoutMS);
        }
        return this;
    }

    public ExpectEvaluator getEv() {
        return ev;
    }

    @Override
    public void evaluate() {
        evaluate(null);
    }

    @Override
    public void evaluate(ITestResult testResult){
        if (etb.isBuilt()) {
            LOG.info("Evaluate", "Expectations have already been evaluated and will not be re-evaluated.");
        } else {
            ev = new ExpectEvaluator(etb);
            boolean result = ev.evaluate();
            result = logStatuses(ev, result);
            assertTrue(result, ev.failed());
        }
    }

    public boolean logStatuses(ExpectEvaluator ev) {
        return logStatuses(ev, true);
    }

    private void logVolume(Goate p){
        if (eut("expect.scenario", false, Boolean.class)) {
            p.put("_scenario", getTestName());
        }
        if (eut("expect.mute", muteFrom, Boolean.class)) {
            p.drop("from");
            p.drop("fromExpected");
        }
    }

    public boolean logStatuses(ExpectEvaluator ev, boolean currentStatus) {
        StringBuilder ps = new StringBuilder();
        boolean passes = false;
        for (Goate p : ev.passes()) {
            passes = true;
            ps.append(expectSeparator);
            try {
                logVolume(p);
                ps.append(p.toString("\t", ""));
            } catch (Exception e) {
                LOG.debug("Evaulate", "A problem was encountered trying to publish the passed expectations: " + e.getMessage(), e);
            }
        }
        if (passes) {
            ps.append(expectSeparator);
            LOG.info("\npassed:" + ps.toString());
        }
        StringBuilder fs = new StringBuilder();
        boolean fails = false;
        for (Goate p : ev.fails()) {
            fails = true;
            fs.append(expectSeparator);
            try {
                logVolume(p);
                fs.append(p.toString("\t", ""));
            } catch (Exception e) {
                LOG.info("Evaulate", "A problem was encountered trying to publish the failed expectations: " + e.getMessage(), e);
            }
        }
        if (fails) {
            fs.append(expectSeparator);
            LOG.error("\nfailed:" + fs.toString());
        }
        StringBuilder ss = new StringBuilder();
        boolean skipped = false;
        StringBuilder zs = new StringBuilder();
        boolean zero = false;
        for (ExpectThreadExecuter expectation : ev.expectations()) {
            Expectation ex = expectation.getExpectation();
            Goate eval = ex.getExpectations();
            for (String key : eval.keys()) {
                Goate exp = eval.get(key, null, Goate.class);
                if (!checkInExpectationList(exp.get("actual"), exp.get("operator", null, String.class), ev.passes())) {
                    if (!checkInExpectationList(exp.get("actual"), exp.get("operator", null, String.class), ev.fails())) {
                        if (!("" + exp.get("actual")).contains("*")&&!("" + exp.get("actual")).contains("+")) {
                            logVolume(exp);
                            skipped = true;
                            ss.append(expectSeparator);
                            ss.append(exp.toString("\t", ""));
                            currentStatus = false;
                            ev.fails().add(exp);
                        } else {
                            if(!("" + exp.get("actual")).contains("+")) {
                                exp.drop("from");
                                exp.drop("fromExpected");
                                zs.append(expectSeparator);
                                zs.append(exp.toString("\t", ""));
                                zero = true;
                            }
                        }
                    }
                }
            }
        }
        if (skipped) {
            ss.append(expectSeparator);
            LOG.error("\nfailed (skipped):" + ss.toString());
        }
        if (zero) {
            zs.append(expectSeparator);
            LOG.info("\nzero or more:" + zs.toString());
        }
        return currentStatus;
    }

    protected boolean checkInExpectationList(Object actual, String operator, List<Goate> list) {
        String act = "" + actual;
        boolean result = false;
        for (Goate expectation : list) {
            if (act.equals("" + expectation.get("actual", null))) {
                if (operator.equals("" + expectation.get("operator", null, String.class))) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public TestNGEngine clearExpectations() {
        etb = new ExpectationThreadBuilder(data);
        return this;
    }

    public void setTestClass(Class testClass){
        this.testClass = testClass;
    }

    public Class getTestClass(){
        return this.testClass;
    }
}
