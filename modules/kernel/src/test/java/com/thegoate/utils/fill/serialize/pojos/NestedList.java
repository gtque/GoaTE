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

package com.thegoate.utils.fill.serialize.pojos;

import com.thegoate.utils.fill.serialize.GoatePojo;
import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.collections.ListType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric Angeli on 8/1/2019.
 */
@GoatePojo
public class NestedList {

	@ListType(type = SimpleInt.class)
	@GoateSource(source = RootSource.class, key = "values")
	private List<SimpleInt> theList;

	public List<SimpleInt> getTheList() {
		return theList;
	}

	public void setTheList(List<SimpleInt> theList) {
		this.theList = theList;
	}

	public NestedList addValue(SimpleInt value){
		if(theList == null){
			theList = new ArrayList<>();
		}
		theList.add(value);
		return this;
	}
}
