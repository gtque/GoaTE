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
import com.thegoate.dsl.Interpreter;
import com.thegoate.dsl.PrimitiveDSL;
import com.thegoate.mock.annotations.Mocker;
import com.thegoate.reflection.GoateReflection;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Returns a mocked object using Mockito.
 * Created by Eric Angeli on 9/20/2017.
 */
@Mocker(type = "object")
public class MockObject extends Mock {

    boolean spy = false;

    @Override
    public Object build() {
        Object mock = null;
        try {
            Class theClass = Class.forName("" + data.get("class", "java.lang.Object"));
            mock = getObject(theClass);
            if (mock != null) {
                doMockFields(theClass, mock);
                doMockMethods(theClass, mock);
            }
        } catch (ClassNotFoundException e) {
            LOG.error("Problem setting up the mock: " + e.getMessage(), e);
        }
        return mock;
    }

    protected Object getObject(Class theClass) {
        return Mockito.mock(theClass);
    }

    protected void doMockFields(Class theClass, Object mock) {
        Goate fields = data.filter("fields.");
        if (fields.size() > 0) {
            Map<String, Field> fieldMap = new ConcurrentHashMap<>();
            new GoateReflection().findFields(theClass, fieldMap);
            for (String field : fields.keys()) {
                String fieldName = "" + field.substring(field.lastIndexOf(".") + 1);
                Object value = data.get(field);
                if (fieldMap.containsKey(fieldName)) {
                    Field fi = fieldMap.get(fieldName);
                    //cheating a bit by forcefully making the field accessible,
                    //but its actual state is reset afterwards.
                    boolean accessible = fi.isAccessible();
                    fi.setAccessible(true);
                    try {
                        fi.set(mock, value);
                    } catch (IllegalAccessException e) {
                        LOG.error("Problem accessing field: " + e, e);
                    }
                    fi.setAccessible(accessible);
                }
            }
        }
    }

    protected String isName(String check) {
        String result = check;
        try {
            int i = Integer.parseInt(check);
            result = null;
        } catch (Exception e) {
            LOG.debug("assuming " + check + " is the name of the method.");
        }
        return result;
    }

    public void doMockMethods(Class theClass, Object mock) {
        Goate methods = data.filter("methods.");
        for (String meth : methods.keys()) {
            if (!meth.contains("parameters") && !meth.contains("return") && !meth.contains("throws")) {
                String checkName = meth.substring(meth.lastIndexOf(".") + 1);
                String methodName = isName(checkName);
                if (methodName != null) {
                    Goate methodData = methods.filter(meth + ".");
                    Goate params = sortParams(methodData.filter(meth+".parameters."));
                    Object[] p = new Object[params.size()];
                    Class[] pc = new Class[params.size()];
                    if (p.length > 0) {
                        buildParameters(p, pc, params);
                    }
                    Object[] r = null;
                    Object r0 = null;
                    boolean notVoid = false;
                    boolean returnReal = Boolean.parseBoolean("" + data.get(meth + ".return.returnReal", "false"));
                    if (methodData.filter(meth + ".return").size() > 0) {
                        Object value = data.get(meth + ".return.value");
                        String check = "" + value;
                        if (!check.equals("void")) {
                            notVoid = true;
                            if (!returnReal) {
                                Goate returns = methodData.filter(meth+".*value.");
                                if (returns.size() > 0) {
                                    r = new Object[returns.size() - 1];
                                    String baseReturn = returns.keys().iterator().next();
                                    baseReturn = baseReturn.substring(0, baseReturn.lastIndexOf("."));
                                    for (int i = 0; i < returns.size(); i++) {
                                        String returnKey = baseReturn + "." + i;
                                        if (i == 0) {
                                            r0 = data.get(returnKey);
                                        } else {
                                            r[i - 1] = data.get(returnKey);
                                        }
                                    }
                                } else {
                                    r = null;
                                    r0 = value;
                                }
                            }
                        }
                    }
                    try {
                        Method fn = new GoateReflection().findMut(theClass, methodName, pc);//theClass.getMethod(method, pc);
                        fn.setAccessible(true);
                        Object theThrows = data.get(meth + ".throws", null);
                        if (theThrows == null) {
                            if (notVoid) {
                                try {
                                    if (!returnReal) {
                                        if (r != null) {
                                            if (!spy) {
                                                Mockito.when(fn.invoke(mock, p)).thenReturn(r0, r);
                                            } else {
                                                fn.invoke(Mockito.doReturn(r0, r).when(mock), p);
                                            }
                                        } else {
                                            if (!spy) {
                                                Mockito.when(fn.invoke(mock, p)).thenReturn(r0);
                                            } else {
                                                fn.invoke(Mockito.doReturn(r0).when(mock), p);
                                            }
                                        }
                                    } else {
                                        Mockito.when(fn.invoke(mock, p)).thenCallRealMethod();
                                    }
                                } catch (Exception e) {
                                    LOG.error("Exception defining method mocks: " + e.getMessage(), e);
                                }
                            }
                        } else {
                            try {
                                Class exception = Class.forName("" + theThrows);
                                Mockito.when(fn.invoke(mock, p)).thenThrow(exception);
                            } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
                                LOG.error("Exception setting up the throw for the mock: " + e.getMessage(), e);
                            }
                        }
                    } catch (NoSuchMethodException e) {
                        LOG.error("failed to find the method: " + methodName + ": " + e.getMessage(), e);
                    }
                }
            }
        }
    }

    protected void buildParameters(Object[] p, Class[] pc, Goate theParameters) {
        for (String index : theParameters.keys()) {
            int i = Integer.parseInt(index);
            Goate param = (Goate) theParameters.get(index);
            p[i] = param.get("value");
            pc[i] = p[i] == null ? Object.class : p[i].getClass();
            if (param.get("type", null) != null) {
                try {
                    pc[i] = Class.forName("" + param.get("type"));
                } catch (ClassNotFoundException e) {
                    LOG.error("unable to find the type for the parameter: " + param.get("type"));
                }
            } else {
                String check = "" + param.getStrict("strict");
                if (check.contains("::")) {
                    Object b = new Interpreter(data).build(check, p[i]);
                    if (PrimitiveDSL.isPrimitive(b)) {
                        PrimitiveDSL prim = (PrimitiveDSL) b;
                        pc[i] = prim.classType();
                    }
                }
            }
        }
    }

    protected Goate sortParams(Goate params) {
        Goate sorted = new Goate();
        int i = 0;
        for (String pv : params.filter(".*value").keys()) {
            String base = pv.substring(0, pv.lastIndexOf("."));
            Goate param = new Goate();
            int index = Integer.parseInt("" + (data.get(base + ".index", i)));
            while (sorted.keys().contains(index)) {
                index++;
            }
            param.put("value", data.get(pv));
            param.put("strict", data.getStrict(pv));
            param.put("type", data.get(base + ".type"));
            sorted.put("" + index, param);
            i++;
        }
        return sorted;
    }
}
