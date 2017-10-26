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
package com.thegoate.mock.dsl;

import com.thegoate.Goate;
import com.thegoate.dsl.DSL;
import com.thegoate.dsl.GoateDSL;
import org.mockito.Mockito;

/**
 * Returns mockito anyClass
 * Created by Eric Angeli on 9/20/2017.
 */
@GoateDSL(word = "anyClass")
public class AnyClass extends DSL {

    public AnyClass(Object value) {
        super(value);
    }

    @Override
    public Object evaluate(Goate data) {
        Object o = get(1, data);
        Class c = null;
        if (o != null) {
            if (o instanceof String) {
                try {
                    c = Class.forName("" + o);
                } catch (ClassNotFoundException e) {
                    LOG.error("could not load: " + o + ": " + e.getMessage(), e);
                }
            } else {
                c = (Class) o;
            }
        }
        return c == null ? Mockito.any() : Mockito.any(c);
    }
}