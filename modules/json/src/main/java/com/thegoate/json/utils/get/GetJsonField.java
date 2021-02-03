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
package com.thegoate.json.utils.get;

import com.thegoate.Goate;
import com.thegoate.json.JsonUtil;
import com.thegoate.json.utils.togoate.JSONToGoate;
import com.thegoate.utils.get.Get;
import com.thegoate.utils.get.GetUtil;
import com.thegoate.utils.get.GetUtility;
import com.thegoate.utils.get.NotFound;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Get the field from the given json.
 * Created by Eric Angeli on 5/19/2017.
 */
@GetUtil(type = JSONObject.class)
public class GetJsonField extends JsonUtil implements GetUtility {

    public GetJsonField(Object val) {
        super(val);
    }

    @Override
    protected Object processNested(Object subContainer) {
        Object result = subContainer;
        if(nested!=null){
            result = new Get(nested).from(subContainer);
        }
        return result;
    }

    @Override
    public Object from(Object container) {
        Object result = null;
        if(takeActionOn instanceof String && (takeActionOn.equals("size()")||takeActionOn.equals("length()"))){
            try {
                result = new JSONArray(container.toString()).length();
            }catch(Exception e){
                try {
                    result = new JSONObject(container.toString()).length();
                }catch(Exception e1) {
                    LOG.warn("The container was not something to get the size from.\n" + e1.getMessage(), e1);
                }
            }
        }else {
            Goate g = new JSONToGoate(container).convert();
            if (g != null) {
                if(g.keys().contains(""+takeActionOn)) {
                    result = g.get("" + takeActionOn);
                }else{
                    result = new NotFound(""+takeActionOn);
                }
            }
        }
        result = processNested(result);
        return result;
    }
}
