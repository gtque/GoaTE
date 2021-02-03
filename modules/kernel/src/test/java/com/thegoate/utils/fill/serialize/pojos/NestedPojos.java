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
import com.thegoate.utils.fill.serialize.collections.MapKeyType;
import com.thegoate.utils.fill.serialize.collections.MapType;

import java.util.List;
import java.util.Map;

/**
 * Created by Eric Angeli on 6/27/2018.
 */
@GoatePojo(id = "nested pojo")
public class NestedPojos {
    private ComplexPojo[] cp;
    private List<Object> list;
    @GoateSource(source = Cheese.class, key="flumbers")
    private int[] numbers;
    private int[] numbers2;
    @MapType(type = Boolean.class)
    @MapKeyType(type = String.class)
    private Map<String, Boolean> map;
    @MapType(type = Object.class)
    @MapKeyType(type = String.class)
    private Map<String, Object> map2;

    public ComplexPojo[] getCp() {
        return cp;
    }

    public void setCp(ComplexPojo[] cp) {
        this.cp = cp;
    }

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    public int[] getNumbers() {
        return numbers;
    }

    public void setNumbers(int[] numbers) {
        this.numbers = numbers;
    }

    public Map<String, Boolean> getMap() {
        return map;
    }

    public void setMap(Map<String, Boolean> map) {
        this.map = map;
    }

    public int[] getNumbers2() {
        return numbers2;
    }

    public void setNumbers2(int[] numbers2) {
        this.numbers2 = numbers2;
    }

    public Map<String, Object> getMap2() {
        return map2;
    }

    public void setMap2(Map<String, Object> map2) {
        this.map2 = map2;
    }
}
