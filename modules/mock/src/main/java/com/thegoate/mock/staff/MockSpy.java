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
package com.thegoate.mock.staff;

import com.thegoate.Goate;
import com.thegoate.mock.annotations.Mocker;
import com.thegoate.reflection.GoateReflection;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Uses mockito to "spy" on an actual object.
 * See mockito spy for more information.
 * Created by Eric Angeli on 9/20/2017.
 */
@Mocker(type = Spy.class)
public class MockSpy extends MockObject {

    public MockSpy(){
        spy = true;
    }

    @Override
    protected Object getObject(Class theClass){
        Object theSpy = null;
        try {
            Goate constructorData = sortParams(data.filter("constructor."));
            Object[] p = new Object[constructorData.size()];
            Class[] pc = new Class[constructorData.size()];
            buildParameters(p, pc, constructorData);
            Constructor constructor = new GoateReflection().findConstructor(theClass.getConstructors(), pc);
            theSpy = Mockito.spy(constructor.newInstance(p));
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            LOG.error("There was a problem building the spy: " + e.getMessage(), e);
        }
        return theSpy;
    }
}
