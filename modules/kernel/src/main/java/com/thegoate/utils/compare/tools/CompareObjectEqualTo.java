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

package com.thegoate.utils.compare.tools;

import com.thegoate.Goate;
import com.thegoate.HealthMonitor;
import com.thegoate.annotations.IsDefault;
import com.thegoate.reflection.GoateReflection;
import com.thegoate.utils.compare.CompareUtil;
import com.thegoate.utils.compare.tools.b.CompareByteEqualTo;
import com.thegoate.utils.compare.tools.bool.CompareBooleanEqualTo;
import com.thegoate.utils.compare.tools.c.CompareCharEqualTo;
import com.thegoate.utils.compare.tools.d.CompareDoubleEqualTo;
import com.thegoate.utils.compare.tools.f.CompareFloatEqualTo;
import com.thegoate.utils.compare.tools.integer.CompareIntEqualTo;
import com.thegoate.utils.compare.tools.l.CompareLongEqualTo;
import com.thegoate.utils.compare.tools.s.CompareShortEqualTo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import static com.thegoate.logging.volume.VolumeKnob.volume;

/**
 * Compares two booleans for equality.
 * Created by Eric Angeli on 5/9/2017.
 */
@CompareUtil(operator = "==")
@IsDefault
public class CompareObjectEqualTo extends CompareObject {

    public CompareObjectEqualTo(Object actual) {
        super(actual);
    }

    @Override
    public boolean isType(Object check) {
        return false;
    }

    @Override
    public boolean evaluate() {
        boolean result = false;
//		GoateReflection gr = new GoateReflection();
//		if (expected != null && (gr.isPrimitive(expected.getClass()) || expected instanceof Number)) {
//			LOG.debug("isEqualTo", "Detected a primitive, comparing as a formatted string.");
//			Compare comp = new Compare(expected);
//			result = comp.compareNumeric(expected instanceof Number).to(actual).using("==").evaluate();
//			health = comp.healthCheck();
//		} else {
        Class klass = actual != null ? new GoateReflection().primitiveType(actual) : null;
        if (klass == null) {
            result = actual != null ? actual.equals(expected) : (expected == null || expected.equals("null::"));
        } else {
            result = comparePrimitive(klass);
        }
//		}
        if (actual instanceof HealthMonitor) {
            health = (((HealthMonitor) actual).healthCheck());
        } else {
            if (!result && actual != null) {
                GoateReflection gr = new GoateReflection();
                Method m = gr.findMethod(actual, "healthCheck");
                if (m != null) {
                    try {
                        health = (Goate) m.invoke(actual);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } else {
                    health.put("value", "" + volume(actual) + "!=" + volume(expected));
                }
            }
        }
        return result;
    }

    private boolean comparePrimitive(Class klass) {
        if (klass == Integer.class) {
            return new CompareIntEqualTo(actual).triedOnce(triedOnce).to(expected).evaluate();
        } else if (klass == Long.class) {
            return new CompareLongEqualTo(actual).triedOnce(triedOnce).to(expected).evaluate();
        } else if (klass == Short.class) {
            return new CompareShortEqualTo(actual).triedOnce(triedOnce).to(expected).evaluate();
        } else if (klass == Byte.class) {
            return new CompareByteEqualTo(actual).triedOnce(triedOnce).to(expected).evaluate();
        } else if (klass == Double.class) {
            return new CompareDoubleEqualTo(actual).triedOnce(triedOnce).to(expected).evaluate();
        } else if (klass == Float.class) {
            return new CompareFloatEqualTo(actual).triedOnce(triedOnce).to(expected).evaluate();
        } else if (klass == Character.class) {
            return new CompareCharEqualTo(actual).triedOnce(triedOnce).to(expected).evaluate();
        } else if (klass == Boolean.class) {
            return new CompareBooleanEqualTo(actual).triedOnce(triedOnce).to(expected).evaluate();
        } else {
            return Objects.equals(actual, expected);
        }
    }
}
