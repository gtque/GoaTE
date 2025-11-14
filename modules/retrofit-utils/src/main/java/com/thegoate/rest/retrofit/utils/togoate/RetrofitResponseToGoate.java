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
package com.thegoate.rest.retrofit.utils.togoate;

import com.thegoate.Goate;
import com.thegoate.annotations.IsDefault;
import com.thegoate.rest.retrofit.utils.get.GetRetrofitResponse;
import com.thegoate.utils.fill.serialize.Serializer;
import com.thegoate.utils.togoate.ToGoate;
import com.thegoate.utils.togoate.ToGoateUtil;
import com.thegoate.utils.togoate.ToGoateUtility;
import okhttp3.ResponseBody;
import retrofit2.Response;

import java.io.IOException;

/**
 * Converts from retrofit response to goate.
 * Created by Eric Angeli on 11/7/2025.
 */
@IsDefault(forType = true)
@ToGoateUtil(type = Response.class)
public class RetrofitResponseToGoate extends ToGoate implements ToGoateUtility {

    public RetrofitResponseToGoate(Object val) {
        super(val);
    }

    @Override
    public Goate convert() {
        Goate result = new Goate();
        result.put("_rest_response", original);
        Response r = (Response) original;
        result.put("status code", r.code());
//        result.put("body as a string", r.raw().body().toString()); //r.raw().body().string() consumes the body and closes it, so it is no longer available, but toString doesn't...
        result.put("body", r.body());

        if (r.body() instanceof ResponseBody) {
            try {
                result.put("body", ((ResponseBody) r.body()).string());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        long tx = r.raw().sentRequestAtMillis();
        long rx = r.raw().receivedResponseAtMillis();
        result.put("response time", rx - tx);
        result.put("session id", new GetRetrofitResponse("session id").from(r));
        result.put("status line", r.message());
//        result.put("json", r.jsonPath());
//        result.put("xml", r.xmlPath());
//        result.put("html", r.htmlPath());
        result.put("headers", r.headers());//r.header(selector.toString().substring("header".length()).trim());
        result.put("cookies",r.headers().values("Set-Cookie"));//selector.toString().substring("cookie".length()).trim());
        result.put("detailedCookies", new GetRetrofitResponse("detailedCookies").from(r));//selector.toString().substring("detailedCookie".length()).trim());
        Goate g = new Serializer(r.body(), r.body().getClass()).detailed(true).toGoate();
        result.merge(g, true);
//        try {
//            Goate g = null;
//            g = new ToGoate(result.get("body")).convert();
//            result.merge(g, true);
//        } catch (Exception e){
//            LOG.warn("Response ToGoate", "Failed to convert the body, there may not be a proper ToGoate Utility in the class path. "+e.getMessage(), e);
//        }
        return result;
    }

    @Override
    public boolean isType(Object check) {
        return check instanceof Response;
    }

}
