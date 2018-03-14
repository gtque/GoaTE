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
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.testng.TestNG;
import com.thegoate.testng.TestNGEngineMethodDL;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;

/**
 * Wrapper class combines AbstractTestNGSpringContextTests and Test Engine functionality from GoaTE.
 * Created by Eric Angeli on 10/31/2017.
 */
public class SpringTestEngine extends AbstractTestNGSpringContextTests implements ITest, TestNG {
    TestNGEngineMethodDL engine = null;
    protected Goate runData = null;
    protected Goate constantData = null;
    protected BleatBox LOG = BleatFactory.getLogger(getClass());

    @LocalServerPort
    int randomServerPort;
//    @LocalManagementPort
//    int randomServerAdminPort;

    public SpringTestEngine(){
        engine = new TestNGEngineMethodDL();
        engine.setLOG(BleatFactory.getLogger(getClass()));
    }

    public SpringTestEngine(Goate data){
        engine = new TestNGEngineMethodDL(data);
        engine.setLOG(BleatFactory.getLogger(getClass()));
    }

    @Override
    @DataProvider(name = "dataLoader")
    public Object[][] dataLoader(ITestContext context) throws Exception {
        engine.initDataLoaders();
        defineDataLoaders();
        return engine.dataLoader(context);
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
    public void bumpRunNumber() {
        engine.bumpRunNumber();
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
        return engine.put(key, val);
    }

    @Override
    public String getTestName() {
        return engine.getTestName();
    }

    public void startUp(Method method) {
        engine.startUp(method);
    }

    @BeforeMethod(alwaysRun = true)
    public void initDataMethod(Object[] d, Method m) {
        if (d != null&&d.length>0) {
            engine.init((Goate)d[0]);
        }
        startUp(m);
    }

    @AfterMethod(alwaysRun = true)
    public void finishUp(Method method) {
        engine.finishUp(method);
    }
}
