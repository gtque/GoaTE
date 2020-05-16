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

package com.thegoate.utils.compare.tools.c;

import com.thegoate.reflection.GoateReflection;
import com.thegoate.utils.ParseDetector;
import com.thegoate.utils.compare.CompareTool;

/**
 * Created by Eric Angeli on 7/14/2017.
 */
public abstract class CompareCharTool extends CompareTool implements ParseDetector {

	public CompareCharTool(Object actual) {
		super(actual);
	}

	@Override
	public boolean isType(Object check) {
		boolean istype = false;
		try {
			GoateReflection gr = new GoateReflection();
			if (gr.isCharacterType(check.getClass())) {
				LOG.debug("Check Type", "detected primitive char");
				istype = true;
			} else {
				LOG.debug("Check Type", "not a primitive char.");
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
			parseChar(object);
			result = true;
		} catch (Exception e){
			LOG.debug("Parse Detector", "Not a parsable char.");
		}
		return result;
	}

	protected char parseChar(Object value){
		String v = ""+value;
		if(v.length()>1){
			throw new ClassCastException("Problem: "+value+" is not a valid char.");
		}
		return v.charAt(0);
	}
}
