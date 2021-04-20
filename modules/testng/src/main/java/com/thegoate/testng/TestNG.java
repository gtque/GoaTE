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
import com.thegoate.expect.Expectation;
import com.thegoate.expect.builder.ExpectationBuilder;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import java.lang.reflect.Method;
import java.util.List;

/**
 * The interface for TestNG based test classes.
 * Created by Eric Angeli on 5/11/2017.
 */
public interface TestNG {
    void startUp(Method method, ITestResult result);
    void finishUp(Method method);
    void init(Goate data);
    Object[][] dataLoader(ITestNGMethod method, ITestContext context) throws Exception;
    void defineDataLoaders();
    void defineDataLoaders(Goate runData, Goate constantData);
    Goate getRunDataLoader();
    Goate getConstantDataLoader();
    Goate getData();
    void setData(Goate data);
    void setRunNumber(int number);
    void bumpRunNumber(String name);
    int getRunNumber();
    void initRunNumber(Method method);
    void setScenario(String scenario);
    String getScenario();
    Object get(String key);
    Object get(String key, Object def);
    <T>T get(String key, Object def, Class<T> type);
    TestNG put(String key, Object val);
    TestNG expect(Expectation expectation);
    TestNG expect(ExpectationBuilder expectationBuilder);
    TestNG expect(List<Expectation> expectation);
    TestNG evalPeriod(long periodMS);
    TestNG evalTimeout(long periodMS);
    void evaluate();
    void evaluate(ITestResult testResult);
    TestNG clearExpectations();
    boolean isExpectationsSet();
    boolean isExpectationsNotEvaluated();
}
