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
import com.thegoate.annotations.GhostProtocol;
import com.thegoate.data.GoateProvider;
import com.thegoate.expect.ExpectEvaluator;
import com.thegoate.expect.Expectation;
import com.thegoate.expect.ExpectationError;
import com.thegoate.expect.ExpectationThreadBuilder;
import com.thegoate.expect.amp.FailAmplifier;
import com.thegoate.expect.amp.PassAmplifier;
import com.thegoate.expect.amp.SkippedAmplifier;
import com.thegoate.expect.amp.ZeroOrMoreAmplifier;
import com.thegoate.expect.builder.ExpectationBuilder;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.metrics.Stopwatch;
import com.thegoate.statics.StaticScrubber;
import com.thegoate.utils.UnknownUtilType;
import com.thegoate.utils.get.Get;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.xml.XmlTest;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.thegoate.dsl.words.EutConfigDSL.eut;
import static org.testng.Assert.assertTrue;


/**
 * The base class for writing TestNG classes.
 * Created by Eric Angeli on 5/11/2017.
 */
@Listeners({TestNGEvaluateListener.class})
@GhostProtocol(ghosts = {"_testng_test_name"})
public abstract class TestNGEngine implements ITest, TestNG {

    private Class testClass = getClass();
    protected GoateProvider provider = null;
    //    protected volatile boolean dd = false;
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
    private List<ExpectEvaluator> executedExpectations = new ArrayList<>();
    protected volatile boolean expectationsSet = false;
    protected volatile boolean expectationsNotEvaluated = true;
    protected volatile boolean expectationsNow = false;

    public static Map<String, Integer> number = new ConcurrentHashMap<>();

    public TestNGEngine() {
        setData(null);
    }

    public TestNGEngine(Goate data) {
        init(data);
    }

    public void setLOG(BleatBox box) {
        LOG = box;
    }

    protected boolean isFactoryDriven() {
        Constructor[] constructors = testClass.getConstructors();
        for (Constructor constructor : constructors) {
            if (constructor.getAnnotation(Factory.class) != null) {
                return true;
            }
        }
        return false;
    }

    protected boolean isDataDriven(Test test, boolean gp) {
        return (!test.dataProvider().isEmpty()) || (gp);
    }


    @BeforeMethod(alwaysRun = true, dependsOnMethods = "initGoate")
    @Override
    public void startUp(Method method, ITestResult testResult) {
        methodName = method.getName();
//        Test test = method.getAnnotation(Test.class);
//        boolean gp = isFactoryDriven();
//        if (isDataDriven(test, gp)) {
//            if (gp) {
//                if (!number.containsKey("" + testClass.getCanonicalName() + ":" + methodName)) {
//                    number.put("" + testClass.getCanonicalName() + ":" + methodName, 0);
//                }
//                getData().drop("_testng_test_name");
////                initGoate(testResult.getParameters(), method);//change to getFactoryParameters() after upgrading verison of TestNG
//            } else {
//                if (!number.containsKey("" + testClass.getCanonicalName() + ":" + methodName)) {
//                    number.put("" + testClass.getCanonicalName() + ":" + methodName, 0);
//                }
////                initGoate(testResult.getParameters(), method);
//            }
//            bumpRunNumber(methodName);
//            setRunNumber(number.get("" + testClass.getCanonicalName() + ":" + methodName));
//        } else {
//            data = new Goate();
//            setRunNumber(0);
//        }
        if (data != null) {
            if (getScenario() == null || getScenario().isEmpty()) {
                setScenario((String) data.get("Scenario"));
            }
            if (data.get("lap", null) != null) {
                Stopwatch.global.start(data.get("lap", Thread.currentThread().getName(), String.class));
            }
        }
        etb = new ExpectationThreadBuilder(data);
        executedExpectations = new ArrayList<>();
        String startMessage = "\n" +
                "***************************Starting Up***************************\n" +
                "*\t" + getTestName() + "\t*\n";
        startMessage += data.toString("*\t", "\t*");
        startMessage += "\n*****************************************************************";
        TestMap.tests.put(getTestName(), this);
        LOG.info("Start Up", startMessage);
    }

    @BeforeMethod(alwaysRun = true)
    public void initDataMethod() {
        UnknownUtilType.clearCache(Get.class);
    }

    //    @AfterMethod(alwaysRun = true)
    @Override
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

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "initDataMethod")
    public void initGoate(Object[] data, Method method) {
        doInitData(data, method, this);
    }

    @Override
    public void initRunNumber(Method method) {
        methodName = method.getName();
        Test test = method.getAnnotation(Test.class);
        boolean gp = isFactoryDriven();
        if (isDataDriven(test, gp)) {
            if (gp) {
                if (!number.containsKey("" + testClass.getCanonicalName() + ":" + methodName)) {
                    number.put("" + testClass.getCanonicalName() + ":" + methodName, 0);
                }
                getData().drop("_testng_test_name");
//                initGoate(testResult.getParameters(), method);//change to getFactoryParameters() after upgrading verison of TestNG
            } else {
                if (!number.containsKey("" + testClass.getCanonicalName() + ":" + methodName)) {
                    number.put("" + testClass.getCanonicalName() + ":" + methodName, 0);
                }
//                initGoate(testResult.getParameters(), method);
            }
            bumpRunNumber(methodName);
            setRunNumber(number.get("" + testClass.getCanonicalName() + ":" + methodName));
        } else {
            data = new Goate();
            setRunNumber(0);
        }
    }

    public static void doInitData(Object[] data, Method method, TestNG test) {
        if (data != null) {
            if (data.length > 0) {
                if (data[0] instanceof Goate) {
                    test.init((Goate) data[0]);
                } else {
                    Goate dGoate = new Goate();
                    if (data[0] instanceof Map) {
                        for (Map.Entry<String, String> entry : ((Map<String, String>) data[0]).entrySet()) {
                            dGoate.put(entry.getKey(), entry.getValue());
                        }
                    } else {
                        Parameter[] parameters = method.getParameters();
                        for (int i = 0; i < parameters.length; i++) {
                            Parameter parameter = parameters[i];
                            dGoate.put(parameter.getName(), data[i]);
                        }
                    }
                    test.init(dGoate);
                }
            } else {
                if (test.getData() == null) {
                    test.init(new Goate());
                }
            }
        }
        test.initRunNumber(method);
    }

    public void init(Goate data) {
        LOG.debug("Init", "setting test data for the class.");
        setData(data);
        String sKey = getData().findKeyIgnoreCase("Scenario");
        setScenario(get(sKey, "empty::", String.class));
//        setRunNumber(number);
//        bumpRunNumber();
    }

    @Override
    public String getTestName() {
        String name = get("_testng_test_name", "init::" + eut("testng.test.name.pattern", "default"), String.class);
        if (name.equalsIgnoreCase("default")) {
            name = defaultTestName();
            put("_testng_test_name", name);
        }
        return name;
    }

    private String defaultTestName() {
        StringBuilder name = new StringBuilder("");
        if (includeClassMethodInName) {
            name.append(methodName)
                    .append(":");
        }
        if (scenario != null) {
            name.append(scenario);
        }
        name.append("(" + runNumber + ")");
        return name.toString();
    }

    @Override
    @DataProvider(name = "dataLoader")
    public Object[][] dataLoader(ITestNGMethod method, ITestContext context) throws Exception {
//        number = 0;//resets the count, assumes TestNG loads all the runs before processing the next class.
//        number.put("" + method.getRealClass().getCanonicalName() + ":" + "class", 0);
        testClass = method.getRealClass();
        this.testContext = context;
        if (context != null) {
            xt = context.getCurrentXmlTest();
        }
        initDataLoaders();
        return TestNGRunFactory.loadRuns(method.getConstructorOrMethod().getMethod(), provider, getRunDataLoader(), getConstantDataLoader(), true, testContext.getIncludedGroups(), testContext.getExcludedGroups());
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
    public void bumpRunNumber(String name) {
        if (number.containsKey("" + testClass.getCanonicalName() + ":" + name)) {
            int num = number.get("" + testClass.getCanonicalName() + ":" + name);
            number.put("" + testClass.getCanonicalName() + ":" + name, num + 1);
        }
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
    public boolean expectNow(Expectation expectation) {
        return expectNow(expectation, false);
    }

    @Override
    public boolean expectNow(Expectation expectation, boolean failImmediately){
        expect(expectation);
        return evaluateNow(failImmediately);
    }

    @Override
    public boolean expectNow(ExpectationBuilder expectation) {
        return expectNow(expectation, false);
    }

    @Override
    public boolean expectNow(ExpectationBuilder expectation, boolean failImmediately){
        expect(expectation);
        return evaluateNow(failImmediately);
    }

    @Override
    public boolean expectNow(List<Expectation> expectation) {
        return expectNow(expectation, false);
    }

    @Override
    public boolean expectNow(List<Expectation> expectation, boolean failImmediately){
        expect(expectation);
        return evaluateNow(failImmediately);
    }

    private boolean evaluateNow(boolean failImmediately){
        boolean result = false;
        try {
            evaluate();
            result = true;
        } catch (ExpectationError expectationError) {
            if(failImmediately){
                throw expectationError;
            }
        } finally {
            expectationsNow = true;
        }
        return result;
    }

    @Override
    public TestNGEngine expect(Expectation expectation) {
        if (etb.isBuilt()) {
            clearExpectations();
        }
        if (etb != null) {
            etb.expect(expectation);
            expectationsSet = true;
        }
        return this;
    }

    @Override
    public TestNGEngine expect(ExpectationBuilder expectationBuilder) {
        return expect(expectationBuilder.build());
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

    public void evaluate(boolean clearAfterRunning) {
        evaluate(null, clearAfterRunning);
    }

    @Override
    public void evaluate(ITestResult testResult) {
        evaluate(testResult, false);
    }

    protected void clearEvaluated(){
        executedExpectations = new ArrayList<>();
    }

    public void evaluate(ITestResult testResult, boolean clearAfterRunning) {
        if (etb.isBuilt()) {
            LOG.info("Evaluate", "Expectations have already been evaluated and will not be re-evaluated.");
            if(executedExpectations.size()>0 && expectationsNow){
                LOG.debug("Evaluate", "but there were expectations that have already been displayed, so I am going to reprint the results");
                ev = new ExpectEvaluator(executedExpectations);
                logStatuses(ev, true);
                assertTrue(ev.fails().size()==0, getFailures(ev));
            }
            expectationsNow = false;
        } else {
            expectationsNotEvaluated = false;
            ev = new ExpectEvaluator(etb);
            boolean result = ev.evaluate();
            executedExpectations.add(ev);
            if (ev.fails().size() > 0) {
                LOG.warn("failures detected...");
                result = false;
            }
            if (ev.skipped().size() > 0) {
                LOG.warn("skipped tests detected...");
                result = false;
            }
            if(testResult != null){
                ev = new ExpectEvaluator(executedExpectations);
                result = ev.fails().size()==0;
            }
            logStatuses(ev, clearAfterRunning);
            try {
                assertTrue(result, ev.failed());
            } catch (Throwable e) {
                LOG.info("Evaluate", "assert" + e.getMessage(), e);
                StringBuilder failures = new StringBuilder();
                executedExpectations.stream().forEach(ex -> failures.append(getFailures(ex)));
                throw new ExpectationError(failures.toString());
//                throw e;
            } finally {
                if (clearAfterRunning) {
                    executedExpectations = new ArrayList<>();
                }
            }
        }
    }

    private String getFailures(ExpectEvaluator ev) {
        StringBuilder failures = new StringBuilder();
        String fails = new FailAmplifier(null)
                .muteFrom(eut("expect.mute", muteFrom, Boolean.class))
                .testName(getTestName())
                .amplify(ev);
        if (!fails.isEmpty()) {
            failures.append("\nfailed").append(fails);
        }

        String skips = new SkippedAmplifier(null)
                .muteFrom(eut("expect.mute", muteFrom, Boolean.class))
                .testName(getTestName())
                .amplify(ev);
        if (!skips.isEmpty()) {
            failures.append(skips);
        }
        return failures.toString();
    }

    public void logStatuses(ExpectEvaluator ev) {
        logStatuses(ev, true);
    }

    public void logStatuses(ExpectEvaluator ev, boolean logFailuresDefault) {
        Goate.innerGoate = -1;
        try {
            String passes = new PassAmplifier(null)
                    .muteFrom(eut("expect.mute", muteFrom, Boolean.class))
                    .testName(getTestName())
                    .amplify(ev);
            if (!passes.isEmpty()) {
                LOG.info("Status", "\npassed:" + passes);
            }
        } catch (Throwable t) {
            LOG.info("TestNGEngine", "Something really bad happened here while logging passed expectations: " + t.getMessage());
            t.printStackTrace();
            throw t;
        }

        try {
            if (eut("assert.printStackTrace", logFailuresDefault, Boolean.class)) {
                LOG.fail("Status", getFailures(ev));
            }
        } catch (Throwable t) {
            LOG.info("TestNGEngine", "Something really bad happened here while logging failed expectations: " + t.getMessage());
            t.printStackTrace();
            throw t;
        }
        try {
            String skipZero = new ZeroOrMoreAmplifier(null)
                    .muteFrom(eut("expect.mute", muteFrom, Boolean.class))
                    .testName(getTestName())
                    .amplify(ev);
            if (!skipZero.isEmpty()) {
                LOG.info("Expectations", "may not have been evaluated:" + skipZero);
            }
        } catch (Throwable t) {
            LOG.info("TestNGEngine", "Something really bad happened here while logging skipped expectations: " + t.getMessage());
            t.printStackTrace();
            throw t;
        }
        Goate.innerGoate = 0;
    }

    @Override
    public TestNGEngine clearExpectations() {
        etb = new ExpectationThreadBuilder(data);
        expectationsSet = false;
        expectationsNotEvaluated = true;
        return this;
    }

    @Override
    public boolean isExpectationsSet() {
        return this.expectationsSet;
    }

    @Override
    public boolean isExpectationsNotEvaluated() {
        return this.expectationsNotEvaluated;
    }

    public void setTestClass(Class testClass) {
        this.testClass = testClass;
    }

    public Class getTestClass() {
        return this.testClass;
    }
}
