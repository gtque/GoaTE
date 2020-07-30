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

package com.thegoate.expect.test;

import com.thegoate.utils.compare.Compare;
import com.thegoate.utils.compare.CompareTool;
import com.thegoate.utils.compare.CompareUtil;
import com.thegoate.utils.compare.CompareUtility;

/**
 * Compares two booleans for equality.
 * Created by Eric Angeli on 5/9/2017.
 */
@CompareUtil(operator = "==", type = SimpleObjectHealth.class)
public class CompareSimpleObjectEqualTo extends CompareTool {

	public CompareSimpleObjectEqualTo(Object actual) {
		super(actual);
	}

	@Override
	public boolean isType(Object check) {
		return check instanceof SimpleObjectHealth;
	}

	@Override
	public boolean evaluate() {
		boolean result = true;
		SimpleObjectHealth compare = (SimpleObjectHealth)expected;
		SimpleObjectHealth act = (SimpleObjectHealth)actual;
		if(compare instanceof SimpleObjectHealth){
			SimpleObjectHealth so = (SimpleObjectHealth)compare;
			if(!act.getA().equals(so.getA())){
				result = false;
				health.put("field a", act.getA() + " != " + so.getA());
			}
			if(act.isB() != so.isB()){
				result = false;
				health.put("field b", act.isB() + " != " + so.isB());
			}
			CompareUtility c = new Compare(act.getC()).using("==").to(so.getC());
			if(!c.evaluate()){
				result = false;
				health.put("field c", c.healthCheck());
			}
		} else {
			result = false;
			health.put("object check", "not the same type");
		}
		return result;
	}
}
