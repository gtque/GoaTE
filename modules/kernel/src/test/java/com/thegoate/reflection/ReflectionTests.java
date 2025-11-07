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

package com.thegoate.reflection;

import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.reflection.test.SampleChild;
import com.thegoate.reflection.test.TestChild;
import com.thegoate.reflection.test.TestConstructors;
import org.testng.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Simple tests to validate reflection wrappers and helpers.
 * Created by gtque on 4/25/2017.
 */
public class ReflectionTests {
    protected final BleatBox LOG = BleatFactory.getLogger(getClass());

    @Test(groups = {"unit"})
    public void getAllMethods(){
        List<Method> methods = new GoateReflection().getAllMethods(TestChild.class, null);
        for(Method m:methods) {
            LOG.debug(m.getName());
        }
        assertEquals(methods.size(), 19);
    }

    @Test(groups = {"unit"})
    public void constructorStringString() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object[] args = {"Hello", "world!"};
        Constructor c = new GoateReflection().findConstructor(TestConstructors.class.getConstructors(), args);
        TestConstructors tc = (TestConstructors)c.newInstance(args);
        assertEquals(tc.v1, "Hello");
        assertEquals(tc.v2, "world!");
        assertEquals(tc.v3, 0);
    }

    @Test(groups = {"unit"})
    public void constructorStringInt() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object[] args = {"Hello", 42};
        Constructor c = new GoateReflection().findConstructor(TestConstructors.class.getConstructors(), args);
        TestConstructors tc = (TestConstructors)c.newInstance(args);
        assertEquals(tc.v1, "Hello");
        assertEquals(tc.v2, "");
        assertEquals(tc.v3, 42);
    }

    @Test(groups = {"unit"})
    public void constructorByte() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object[] args = {(byte)2};
        Constructor c = new GoateReflection().findConstructor(TestConstructors.class.getConstructors(), args);
        TestConstructors tc = (TestConstructors)c.newInstance(args);
        assertEquals(tc.b, (byte)2);
    }

    @Test(groups = {"unit"})
    public void constructorLong() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object[] args = {2L};
        Constructor c = new GoateReflection().findConstructor(TestConstructors.class.getConstructors(), args);
        TestConstructors tc = (TestConstructors)c.newInstance(args);
        assertEquals(tc.l, 2L);
    }

    @Test(groups = {"unit"})
    public void constructorDouble() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object[] args = {2d};
        Constructor c = new GoateReflection().findConstructor(TestConstructors.class.getConstructors(), args);
        TestConstructors tc = (TestConstructors)c.newInstance(args);
        assertEquals(tc.d, 2d);
    }

    @Test(groups = {"unit"})
    public void constructorFloat() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object[] args = {2f};
        Constructor c = new GoateReflection().findConstructor(TestConstructors.class.getConstructors(), args);
        TestConstructors tc = (TestConstructors)c.newInstance(args);
        assertEquals(tc.f, 2f);
    }

    @Test(groups = {"unit"})
    public void constructorBoolean() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object[] args = {true};
        Constructor c = new GoateReflection().findConstructor(TestConstructors.class.getConstructors(), args);
        TestConstructors tc = (TestConstructors)c.newInstance(args);
        assertEquals(tc.t, true);
    }

    @Test(groups = {"unit"})
    public void constructorChar() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object[] args = {'z'};
        Constructor c = new GoateReflection().findConstructor(TestConstructors.class.getConstructors(), args);
        TestConstructors tc = (TestConstructors)c.newInstance(args);
        assertEquals(tc.c, 'z');
    }

    @Test(groups = {"unit"})
    public void findMethod() {
        Object o = new SampleChild();
        assertNotNull(new GoateReflection().findMethod(o, "healthCheck"));
    }
}
