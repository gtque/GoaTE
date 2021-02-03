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
package com.thegoate.json.utils.tolist;

import com.thegoate.Goate;
import com.thegoate.json.JsonUtil;
import com.thegoate.utils.fill.serialize.DeSerializer;
import com.thegoate.utils.togoate.ToGoate;
import com.thegoate.utils.tolist.ToListUtil;
import com.thegoate.utils.tolist.ToListUtility;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Converts from json array to list.
 * Created by Eric Angeli on 5/19/2017.
 */
@ToListUtil
public class JSONToList extends JsonUtil implements ToListUtility {

    public JSONToList(Object val) {
        super(val);
        takeActionOn = val;
        nested = null;
    }

    @Override
    protected void init(Object val){
        processNested = false;
        super.init(val);
    }

    @Override
    protected Object processNested(Object subContainer) {
        return subContainer;
    }

    @Override
    public List list() {
        JSONObject jo = new JSONObject();
        jo.put("theList", (takeActionOn instanceof JSONArray?(JSONArray)takeActionOn: new JSONArray(""+takeActionOn)));
        Goate g = new ToGoate(jo).convert();
        JSONList data = new DeSerializer().data(g).build(JSONList.class);
        return data.getTheList();
    }
}
