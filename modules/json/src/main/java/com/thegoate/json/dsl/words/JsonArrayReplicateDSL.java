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

/**
 * Returns a json array with replicated values.
 * Created by gtque on 4/21/2017.
 */
@GoateDSL(word = "json array replicate")
@GoateDescription(description = "Returns a json array with the entries replicated the specified number of times.",
        parameters = {"The original json array","The number of times to replicate the fields."})
public class JsonArrayReplicateDSL extends DSL {
    public JsonArrayReplicateDSL(Object value) {
        super(value);
    }

    public static Object jsonArrayReplicate(String jsonString, int replicationCount){
        return jsonArrayReplicate(jsonString, replicationCount, new Goate());
    }

    public static Object jsonArrayReplicate(String jsonString, int replicationCount, Goate data){
        return new JsonArrayReplicateDSL("json array replicate::"+jsonString+","+replicationCount).evaluate(data);
    }
    @Override
    public Object evaluate(Goate data) {
        String def = ""+get(1,data);
        if(def.equals("null")||def.isEmpty()){
            def = "[]";
        }
        int replicate = Integer.parseInt("" + get(2,data));
        JSONArray arrayO = new JSONArray(def);
        JSONArray array = new JSONArray("[]");
        for(;replicate>0;replicate--){
            for(int i=0;i<arrayO.length();i++){
                array.put(arrayO.get(i));
            }
        }
        return array;
    }
}
