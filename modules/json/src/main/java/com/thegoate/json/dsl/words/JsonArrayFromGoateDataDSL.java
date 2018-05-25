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

package com.thegoate.json.dsl.words;

import com.thegoate.Goate;
import com.thegoate.annotations.GoateDescription;
import com.thegoate.dsl.DSL;
import com.thegoate.dsl.GoateDSL;
import org.json.JSONArray;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Returns the value of the referenced object.
 * Created by gtque on 4/21/2017.
 */
@GoateDSL(word = "json array from goate")
@GoateDescription(description = "Returns a json array built from the goate data",
        parameters = {"The filter to apply to the goate data, optional - if omitted will add everything from the goate collection."})
public class JsonArrayFromGoateDataDSL extends DSL {
    public JsonArrayFromGoateDataDSL(Object value) {
        super(value);
    }

    @Override
    public Object evaluate(Goate data) {
        String filter = ""+get(1,data);
        if(filter.equals("null")||filter.isEmpty()){
            filter = null;
        }
        Goate filtered = data;
        if(filter!=null){
            filtered = filtered.filter(filter);
        }
        JSONArray array = new JSONArray("[]");
        for(String key:filtered.keys()){
            array.put(filtered.get(key));
        }
        return array;
    }
}
