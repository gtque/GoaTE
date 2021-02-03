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

import java.util.Map;

import com.thegoate.utils.fill.serialize.GoatePojo;
import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.collections.MapKeyType;
import com.thegoate.utils.fill.serialize.collections.MapType;

/**
 * Created by Eric Angeli on 6/26/2018.
 */
@GoatePojo(id = "mapped pojo")
public class MappedPojo {

    @MapType(type = NestedMap1.class)
    @MapKeyType(type = String.class)
    @GoateSource(source = SimpleSource.class, key = "theMap")
    private Map<String,NestedMap1> someMap;

    @MapType(type = String.class)
    @MapKeyType(type = String.class)
    @GoateSource(source = SimpleSource.class, key = "theStringMap")
    private Map<String, String> someMapOfStrings;

    public Map<String, NestedMap1> getSomeMap() {
        return someMap;
    }

    public void setSomeMap(Map<String, NestedMap1> someMap) {
        this.someMap = someMap;
    }

    public Map<String, String> getSomeMapOfStrings() {
        return someMapOfStrings;
    }

    public void setSomeMapOfStrings(Map<String, String> someMapOfStrings) {
        this.someMapOfStrings = someMapOfStrings;
    }
}
