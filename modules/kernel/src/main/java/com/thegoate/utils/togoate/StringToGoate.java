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
package com.thegoate.utils.togoate;

import com.thegoate.Goate;
import com.thegoate.annotations.IsDefault;
import com.thegoate.utils.Utility;

import java.util.Arrays;

/**
 * Converts the given string to a goate collection.
 * If a string, assumes it is in a semicolon separated list of key=value pairs.
 */
@ToGoateUtil(type = String.class)
@IsDefault(forType = true)
public class StringToGoate implements ToGoateUtility {
	Object takeActionOn;

	public StringToGoate(Object val) {
		takeActionOn = val;
	}

	@Override
	public Goate healthCheck() {
		return null;
	}

	@Override
	public Utility setData(Goate data) {
		return null;
	}

	@Override
	public boolean isType(Object check) {
		return check instanceof String && ((""+check).contains("="));
	}

	@Override
	public ToGoateUtility autoIncrement(boolean increment) {
		return this;
	}

	@Override
	public Goate convert() {
		Goate goate = new Goate();

		if(takeActionOn != null){
			if(takeActionOn instanceof String) {
				goate = processString(goate, ""+takeActionOn);
			} else {
//				goate = new Serializer(takeActionOn, takeActionOn.getClass()).toGoate();
			}
		}
		return goate;
	}

	private Goate processString(Goate goate, String value){
		String[] elements = value.split(";");
		Arrays.stream(elements).forEach(element -> addToGoate(element, goate));
		return goate;
	}

	private void addToGoate(String element, Goate goate){
		String[] keyValue = element.split("=");
		if(keyValue.length == 2){
			goate.put(keyValue[0], keyValue[1]);
		}
	}

}
