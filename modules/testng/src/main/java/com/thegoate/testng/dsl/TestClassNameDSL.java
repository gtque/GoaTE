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

package com.thegoate.testng.dsl;

import com.thegoate.Goate;
import com.thegoate.annotations.GoateDescription;
import com.thegoate.dsl.DSL;
import com.thegoate.dsl.GoateDSL;

/**
 * returns the class name of the calling class.
 * Created by gtque on 4/21/2017.
 */
@GoateDSL(word = "test.class.name")
@GoateDescription(description = "Returns the (canonical) name of the class this was invoked by.",
        parameters = {})
public class TestClassNameDSL extends DSL {
    public TestClassNameDSL(Object value) {
        super(value);
    }

    @Override
    public Object evaluate(Goate data) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        int count = 0;
        String name = "Not Found";
        StackTraceElement next = null;
        for(StackTraceElement element:stack){
            count++;
            if(element.getClassName().equals("com.thegoate.testng.TestNGEngine")){
                if(count<stack.length) {
                    next = stack[count];
                    break;
                }
            }
        }
        if(next!=null){
            name = next.getClassName();
        }
        return name;
    }
}
