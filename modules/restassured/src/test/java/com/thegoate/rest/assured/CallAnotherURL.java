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
package com.thegoate.rest.assured;

import com.thegoate.rest.RestCall;
import com.thegoate.staff.Employee;

import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.response.Response;

/**
 * Created by Eric Angeli on 11/26/2018.
 */
public class CallAnotherURL extends Employee<Response> {
    String baseURL;

    @Override
    public String[] detailedScrub() {
        return new String[0];
    }

    @Override
    protected Employee<Response> init() {
        return this;
    }

    public CallAnotherURL baseURL(String baseURL){
        this.baseURL = baseURL;
        return this;
    }
    @Override
    protected Response doWork() {
//        RestCall<Response> rest = new RestCall();//.baseURL(baseURL);
        return new RestCall<Response>().baseURL(baseURL).patch("bump");
    }
}
