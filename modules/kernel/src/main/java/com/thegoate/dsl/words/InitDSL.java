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

package com.thegoate.dsl.words;

import com.thegoate.Goate;
import com.thegoate.annotations.GoateDescription;
import com.thegoate.dsl.DSL;
import com.thegoate.dsl.GoateDSL;

/**
 * Returns the value of the referenced object and sets it to the field so the same value is returned every time it is accessed.
 * Created by gtque on 4/21/2017.
 */
@GoateDSL(word = "init")
@GoateDescription(description = "initializes the field to the evaluated value and returns that value." +
        "\n subsequent calls will return the same value.", parameters = {"the value or method for determining the value."})
public class InitDSL extends DSL {
    public InitDSL(Object value) {
        super(value);
    }

    public static Object init(String key){
        return init(key, new Goate());
    }

    public static Object init(String key, Goate data){
        return new InitDSL("init::"+key).evaluate(data);
    }

    @Override
    public Object evaluate(Goate data) {
        data.put(key, get(1,data));
        return data.get(key);
    }
}
