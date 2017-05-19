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

package com.thegoate.rest.assured.utils.get;

import com.thegoate.utils.get.Get;
import com.thegoate.utils.get.GetTool;
import com.thegoate.utils.get.GetUtil;
import io.restassured.response.Response;

/**
 * Loads the file specified in from into a string and retuns it.
 * Created by Eric Angeli on 5/18/2017.
 */
@GetUtil
public class GetRAResponse extends GetTool{

    public GetRAResponse(){
        super(null);
    }

    public GetRAResponse(Object selector) {
        super(selector);
    }

    @Override
    public boolean isType(Object check) {
        return check instanceof Response;
    }

    @Override
    public Object from(Object container) {
        Object result = null;
        if(container!=null){
            Response r = (Response)container;
            if(selector.equals("status code")){
                result = r.statusCode();
            }else if(selector.equals("body")){
                result = r.body();
            }else if(selector.equals("body as a string")){
                result = r.body().prettyPrint();
            }else if(selector.equals("response time")){
                result = r.time();
            }else if(selector.equals("session id")){
                result = r.sessionId();
            }else if(selector.equals("status line")){
                result = r.statusLine();
            }else if(selector.equals("json")){
                result = r.jsonPath();
            }else if(selector.equals("xml")){
                result = r.xmlPath();
            }else if(selector.equals("html")){
                result = r.htmlPath();
            }else if(selector.toString().startsWith("header")){
                result = r.header(selector.toString().substring("header".length()).trim());
            }else if(selector.toString().startsWith("cookie")){
                result = r.cookie(selector.toString().substring("cookie".length()).trim());
            }else if(selector.toString().startsWith("detailedCookie")){
                result = r.detailedCookie(selector.toString().substring("detailedCookie".length()).trim());
            }else{
                result = new Get(selector).from(r.body().prettyPrint());
            }

        }
        return result;
    }
}
