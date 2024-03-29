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

package com.thegoate.utils.compare.tools.f;

import com.thegoate.reflection.GoateReflection;
import com.thegoate.utils.ParseDetector;
import com.thegoate.utils.compare.CompareTool;
import com.thegoate.utils.compare.tools.d.CompareDoubleEqualTo;

/**
 * Created by Eric Angeli on 7/14/2017.
 */
public abstract class CompareFloatTool extends CompareTool implements ParseDetector {

	public CompareFloatTool(Object actual) {
		super(actual);
	}

	@Override
	public boolean isType(Object check) {
		boolean istype = false;
		try {
			GoateReflection gr = new GoateReflection();
			if (gr.isFloatType(check.getClass())) {
				LOG.debug("Check Type", "detected primitive float");
				istype = true;
			} else {
				LOG.debug("Check Type", "not a primitive float.");
			}
		} catch (Throwable e) {
			//            LOG.debug(""+actual + " is not a float.");
		}
		return istype;
	}

	@Override
	public boolean parseDetector(Object object){
		boolean result = false;
		try{
			parseFloat(object);
			if(!new CompareDoubleEqualTo(actual).parseDetector(object)) {
				result = true;
			} else {
				LOG.debug("Parse Detector", "Detected a possible double, will treat it as such instead.");
			}
		} catch (Exception e){
			LOG.debug("Parse Detector", "Not a parsable float.");
		}
		return result;
	}

	protected float parseFloat(Object o){
		return new GoateReflection().parseFloat(o);
	}
}
