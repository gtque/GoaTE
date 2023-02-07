/*
 * Copyright (c) 2023. Eric Angeli
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
package com.thegoate.eut.properties;

import com.thegoate.eut.Eut;
import com.thegoate.eut.EutConfig;
import com.thegoate.utils.fill.serialize.GoateSource;

@GoateSource(key = "something", flatten = true)
@GoateSource(key = "first", priority = 1)
@GoateSource(key = "third", priority = 3)
@GoateSource(key = "second", priority = 2)
@GoateSource(key = "")
@GoateSource(key = "threeB", priority = 3)
@EutConfig(location = "config", defaultFile = "config/simple.properties", pattern = "simple-${testProfile}")
public class SimpleEut extends Eut<SimpleEut> {

    public static final SimpleEut eut = load(SimpleEut.class);

    @GoateSource(key = "field.a", priority = 1)
    @GoateSource(key = "just_a_name", flatten = true, priority = 3)
    @GoateSource(key = "some.field.a", priority = 2)
    @GoateSource(key = "theField", priority = 1)
    public String FIELD_A = "Fuzzy Wuzzy was a bear";

    @Override
    public SimpleEut eut() {
        return eut;
    }
}
