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
package com.thegoate.mock;

import com.thegoate.mock.stubs.Simple1;
import com.thegoate.mock.stubs.SimpleWrapper;
import com.thegoate.testng.TestNGEngineMethodDL;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * Test mocks.
 * Created by Eric Angeli on 9/20/2017.
 */
public class MockTests extends TestNGEngineMethodDL {

    @Test(groups = {"unit"})
    public void simpleMockAnyClassTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        MockBuilder mb = new MockBuilder();
        String[] greetings = {"1", "2", "3"};
        mb.object().setClass(Simple1.class)
                .method("aXb").returnValue(20091124)
                .method("sayHello").methodParameter("anyClass::java.lang.String", "java.lang.String").returnValues(greetings);
        SimpleWrapper sw = new SimpleWrapper();
        sw.sample = (Simple1) mb.build();
        String word = "" + System.nanoTime();
        assertEquals(sw.getAxB(), 20091124);
        assertEquals(sw.greeting(word), "1");
        assertEquals(sw.greeting(word), "2");
        assertEquals(sw.greeting(word), "3");
        assertEquals(sw.greeting(word), "3");
        assertEquals(sw.greeting(word), "3");
    }

    @Test(groups = {"unit"})
    public void simpleMockAnyStringTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        MockBuilder mb = new MockBuilder();
        String[] greetings = {"1", "2", "3"};
        mb.object().setClass(Simple1.class)
                .method("aXb").returnValue(20091124)
                .method("sayHello").methodParameter("anyString::").returnValues(greetings);
        SimpleWrapper sw = new SimpleWrapper();
        sw.sample = (Simple1) mb.build();
        String word = "" + System.nanoTime();
        assertEquals(sw.getAxB(), 20091124);
        assertEquals(sw.greeting(word), "1");
        assertEquals(sw.greeting(word), "2");
        assertEquals(sw.greeting(word), "3");
        assertEquals(sw.greeting(word), "3");
        assertEquals(sw.greeting(word), "3");
    }

    @Test(groups = {"unit"})
    public void simpleMockThrowExceptionTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        MockBuilder mb = new MockBuilder();
        String[] greetings = {"1", "2", "3"};
        mb.object().setClass(Simple1.class)
                .method("aXb").returnValue(20091124).methodThrows(RuntimeException.class)
                .method("sayHello").methodParameter("anyString::").returnValues(greetings);
        SimpleWrapper sw = new SimpleWrapper();
        sw.sample = (Simple1) mb.build();
        int actual = -42;
        try {
            actual = sw.getAxB();
            fail("should have thrown an exception");
        }catch(Throwable e){
            LOG.debug("threw an exception, HOORAY!!!");
        }
        assertEquals(actual, -42);
    }

    @Test(groups = {"unit"})
    public void simpleSpyTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        MockBuilder mb = new MockBuilder();
        String[] greetings = {"1", "2", "3"};
        mb.spy().setClass(Simple1.class)
                .method("aXb").returnValue(20091124)
                .method("sayHello").methodParameter("").returnValues(greetings)
                .field("greeting", "hello:");
        SimpleWrapper sw = new SimpleWrapper();
        sw.sample = (Simple1) mb.build();
        assertEquals(sw.getAxB(), 20091124);
        String word = "" + System.nanoTime();
        assertEquals(sw.greeting(word), "hello: " + word);
        assertEquals(sw.greeting(word), "hello: " + word);
        assertEquals(sw.greeting(word), "hello: " + word);
        assertEquals(sw.greeting(word), "hello: " + word);
        assertEquals(sw.greeting(word), "hello: " + word);
    }
}
