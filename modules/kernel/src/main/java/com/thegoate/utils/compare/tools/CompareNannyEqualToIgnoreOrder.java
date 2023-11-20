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
import com.thegoate.utils.fill.serialize.Nanny;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.thegoate.logging.volume.VolumeKnob.volume;

/**
 * Compares two Nanny based pojos (or Kid since Kid extends Nanny) and ignores list order.
 * Created by Eric Angeli on 5/9/2017.
 */
@CompareUtil(operator = "~==", type = Nanny.class)
@IsDefault(forType = true)
public class CompareNannyEqualToIgnoreOrder extends CompareObject {

	public CompareNannyEqualToIgnoreOrder(Object actual) {
		super(actual);
	}

	@Override
	public boolean isType(Object check) {
		return false;
	}

	@Override
	public boolean evaluate() {
		boolean result = false;

			result = actual != null ? ((Nanny)actual).equalityCheck("~==").equals(expected) : (expected == null || expected.equals("null::"));
//		}
		if (actual instanceof HealthMonitor) {
			health = (((HealthMonitor) actual).healthCheck());
		} else {
			if (!result&&actual!=null) {
				GoateReflection gr = new GoateReflection();
				Method m = gr.findMethod(actual, "healthCheck");
				if(m != null){
					try {
						health = (Goate)m.invoke(actual);
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
}
