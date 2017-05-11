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

package com.thegoate.annotations;

import com.thegoate.staff.GoateTask;
import com.thegoate.staff.GoateTaskContainer;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Some simple tests of the AnnotationFactory
 * Created by gtque on 4/24/2017.
 */
public class AnnotationTests {

    @Test(groups = {"unit"})
    public void simpleAnnotatedTask() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        AnnotationFactory af = new AnnotationFactory()
                .annotatedWith(GoateTaskContainer.class)
                .findByMethod("say hello")
                .methodAnnotatedWith(GoateTask.class)
                .buildDirectory();
        Object o = af.build();
        Method m = af.getMethod();
        assertNotNull(o);
        assertNotNull(m);
        assertEquals(m.invoke(o),"hello");
        o = null;
        af.clear()
                .annotatedWith(GoateTaskContainer.class)
                .findByMethod("say goodbye")
                .methodAnnotatedWith(GoateTask.class);
        o = af.build();
        m = af.getMethod();
        assertNotNull(o);
        assertNotNull(m);
        assertEquals(m.invoke(o),"goodbye");
    }
}
