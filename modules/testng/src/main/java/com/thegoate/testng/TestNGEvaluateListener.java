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

import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import org.testng.*;

/**
 * Created by Eric Angeli on 11/28/2017.
 */
public class TestNGEvaluateListener implements IInvokedMethodListener, ITestListener {

    BleatBox LOG = BleatFactory.getLogger(getClass());

    @Override
    public void onTestStart(ITestResult arg0) {
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {

    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {

    }

    @Override
    public void onFinish(ITestContext iTestContext) {

    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult result) {
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        if (method.isTestMethod()) {
            if (result.getTestName() != null) {
                TestNG test = TestMap.tests.get(result.getTestName());
                try {
                    if (test != null) {
                        test.evaluate(result);
                    }
                } catch (Throwable t) {
                    result.setStatus(ITestResult.FAILURE);
//                    if (eut("assert.printStackTrace", true, Boolean.class)) {
                        result.setThrowable(t);
//                    }
                } finally {
                    TestMap.tests.remove(result.getTestName());//clearing map for garbage collection
                    if(test != null) {
                        test.finishUp(method.getTestMethod().getConstructorOrMethod().getMethod());
                    }
                }
            }
        }
    }
}
