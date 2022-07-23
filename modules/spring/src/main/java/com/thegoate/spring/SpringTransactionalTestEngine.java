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
package com.thegoate.spring;

import com.thegoate.Goate;
import com.thegoate.expect.Expectation;
import com.thegoate.expect.builder.ExpectationBuilder;
import com.thegoate.logging.BleatFactory;
import com.thegoate.testng.TestNG;
import com.thegoate.testng.TestNGEngine;
import com.thegoate.testng.TestNGEngineMethodDL;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Wrapper class combines AbstractTestNGSpringContextTests and Test Engine functionality from GoaTE.
 * Created by Eric Angeli on 10/31/2017.
 */
public class SpringTransactionalTestEngine extends AbstractTransactionalTestNGSpringContextTests implements ITest, TestNG {
    TestNGEngineMethodDL engine = null;
    protected Goate runData = null;
    protected Goate constantData = null;
    protected Goate data = null;

    public SpringTransactionalTestEngine(){
        engine = new TestNGEngineMethodDL();
        engine.setLOG(BleatFactory.getLogger(getClass()));
        this.data = engine.getData();
    }

    public SpringTransactionalTestEngine(Goate data){
        engine = new TestNGEngineMethodDL(data);
        engine.setLOG(BleatFactory.getLogger(getClass()));
        this.data = engine.getData();
    }

    @Override
    @DataProvider(name = "dataLoader")
    public Object[][] dataLoader(ITestNGMethod method, ITestContext context) throws Exception {
        engine.initDataLoaders();
        defineDataLoaders();
        return engine.dataLoader(method, context);
    }

    @DataProvider(name = "methodLoader")
    public Object[][] dataLoader(ITestContext context, Method method) throws Exception {
        engine.initDataLoaders();
        defineDataLoaders();
        return engine.dataLoader(context, method);
    }

    @Override
    /**
     * You should override this method, but call super.defineDataLoaders() as the first line,
     * then you can use runData and constantData like you would normally.
     */
    public void defineDataLoaders() {
        runData = engine.getRunDataLoader();
        constantData = engine.getConstantDataLoader();
    }

    @Override
    public void defineDataLoaders(Goate runData, Goate constantData) {
        engine.defineDataLoaders(runData, constantData);
    }

    @Override
    public Goate getRunDataLoader() {
        return engine.getRunDataLoader();
    }

    @Override
    public Goate getConstantDataLoader() {
        return engine.getConstantDataLoader();
    }

    @Override
    public Goate getData() {
        return engine.getData();
    }

    @Override
    public void setData(Goate data) {
        engine.setData(data);
    }

    @Override
    public void setRunNumber(int number) {
        engine.setRunNumber(number);
    }

    @Override
    public void bumpRunNumber(String name) {
        engine.bumpRunNumber(name);
    }

    @Override
    public int getRunNumber() {
        return engine.getRunNumber();
    }

    @Override
    public void setScenario(String scenario) {
        engine.setScenario(scenario);
    }

    @Override
    public String getScenario() {
        return engine.getScenario();
    }

    @Override
    public Object get(String key) {
        return engine.get(key);
    }

    @Override
    public Object get(String key, Object def) {
        return engine.get(key, def);
    }

    @Override
    public <T> T get(String key, Object def, Class<T> type) {
        return engine.get(key, def, type);
    }

    @Override
    public TestNG put(String key, Object val) {
        engine.put(key, val);
        return this;
    }

    @Override
    public boolean expectNow(Expectation expectation) {
        return expectNow(expectation, false);
    }

    @Override
    public boolean expectNow(ExpectationBuilder<? extends ExpectationBuilder> expectationBuilder) {
        return expectNow(expectationBuilder, false);
    }

    @Override
    public boolean expectNow(List<Expectation> expectation) {
        return expectNow(expectation, false);
    }

    @Override
    public boolean expectNow(Expectation expectation, boolean failImmediately) {
        return engine.expectNow(expectation, failImmediately);
    }

    @Override
    public boolean expectNow(ExpectationBuilder<? extends ExpectationBuilder> expectationBuilder, boolean failImmediately) {
        return engine.expectNow(expectationBuilder, failImmediately);
    }

    @Override
    public boolean expectNow(List<Expectation> expectation, boolean failImmediately) {
        return engine.expectNow(expectation, failImmediately);
    }

    @Override
    public TestNG expect(Expectation expectation) {
        engine.expect(expectation);
        return this;
    }

    @Override
    public TestNG expect(ExpectationBuilder<? extends ExpectationBuilder> expectationBuilder){
        engine.expect(expectationBuilder.build());
        return this;
    }

    @Override
    public TestNG expect(List<Expectation> expectation) {
        engine.expect(expectation);
        return this;
    }

    @Override
    public TestNG evalPeriod(long periodMS) {
        engine.evalPeriod(periodMS);
        return this;
    }

    @Override
    public TestNG evalTimeout(long timeoutMS) {
        engine.evalTimeout(timeoutMS);
        return this;
    }

    @Override
    public void evaluate() {
        engine.evaluate();
    }

    @Override
    public void evaluate(ITestResult testResult) {
        engine.evaluate(testResult);
    }

    @Override
    public TestNG clearExpectations() {
        engine.clearExpectations();
        return this;
    }

    @Override
    public String getTestName() {
        return engine.getTestName();
    }

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "initDataMethod")
    public void startUp(Method method, ITestResult result) {
        engine.startUp(method, result);
    }

    @BeforeMethod(alwaysRun = true)
    public void initDataMethod(Object[] d, Method m) {
        TestNGEngine.doInitData(d, m, this);
    }

    @AfterMethod(alwaysRun = true)
    public void finishUp(Method method) {
        engine.finishUp(method);
    }

    @Override
    public void init(Goate data) {
        engine.init(data);
        this.data = data;
    }

    @Override
    public boolean isExpectationsSet() {
        return engine.isExpectationsSet();
    }

    @Override
    public boolean isExpectationsNotEvaluated() {
        return engine.isExpectationsNotEvaluated();
    }

    @Override
    public void initRunNumber(Method method) {
        engine.initRunNumber(method);
    }
}
