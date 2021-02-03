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

package com.thegoate.utils.get;

import com.thegoate.utils.UnknownUtilType;
import com.thegoate.utils.UtilCache;

/**
 * The generic get class.
 * This will attempt to look up the specific get utility for the type detected.
 * Created by Eric Angeli on 5/5/2017.
 */
@UtilCache(name = "get", clear = true, useCache = true)
public class Get extends UnknownUtilType implements GetUtility {

	GetUtility tool = null;
	Object find = null;

	public Get() {
		super();
	}

	public Get(Object o) {
		super();
		this.find = o;
		nameIsHash = true;
	}

	@Override
	public boolean isType(Object check) {
		return false;
	}

	@Override
	public Object from(Object container) {
		tool = (GetUtility) buildUtil(container, GetUtil.class, find);
		Object result = new NotFound("Get tool not found, make sure a proper get util is implemented or on the class path.");
		if (tool != null) {
			result = tool.from(container);
		}
		return result;
	}

	@Override
	public boolean checkType(Class tool, Class type) {
		GetUtil tu = (GetUtil) tool.getAnnotation(GetUtil.class);
		return tu.type() != null ? tu.equals(type) : type == null;
		//        return false;
	}
}
