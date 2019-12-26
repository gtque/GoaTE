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
package com.thegoate.rest.assured.utils.togoate;

import com.thegoate.Goate;
import com.thegoate.annotations.IsDefault;
import com.thegoate.utils.togoate.ToGoate;
import com.thegoate.utils.togoate.ToGoateUtil;
import com.thegoate.utils.togoate.ToGoateUtility;
import io.restassured.response.Response;

/**
 * Converts from restassured response to goate.
 * Created by Eric Angeli on 3/1/2019.
 */
@IsDefault(forType = true)
@ToGoateUtil
public class RAResponseToGoate extends ToGoate implements ToGoateUtility {

    public RAResponseToGoate(Object val) {
        super(val);
    }

    @Override
    public Goate convert() {
        Goate result = new Goate();
        result.put("_rest_response", original);
        Response r = (Response) original;
        result.put("status code", r.statusCode());
        result.put("body", r.body());
        result.put("body as a string", r.body().asString());
        result.put("response time", r.time());
        result.put("session id", r.sessionId());
        result.put("status line", r.statusLine());
//        result.put("json", r.jsonPath());
//        result.put("xml", r.xmlPath());
//        result.put("html", r.htmlPath());
        result.put("headers", r.headers());//r.header(selector.toString().substring("header".length()).trim());
        result.put("cookies", r.cookies());//selector.toString().substring("cookie".length()).trim());
        result.put("detailedCookies", r.detailedCookies());//selector.toString().substring("detailedCookie".length()).trim());
        try {
            Goate g = null;
            g = new ToGoate(r.body().asString()).convert();
            result.merge(g, true);
        } catch (Exception e){
            LOG.warn("Response ToGoate", "Failed to convert the body, there may not be a proper ToGoate Utility in the class path. "+e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean isType(Object check) {
        return check instanceof Response;
    }

}
