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

import com.thegoate.Goate;
import com.thegoate.expect.ExpectEvaluator;
import com.thegoate.expect.ExpectationThreadBuilder;
import com.thegoate.rest.Rest;
import com.thegoate.rest.staff.ApiGet;
import com.thegoate.rest.staff.ApiPost;
import com.thegoate.staff.Employee;
import com.thegoate.testng.TestNGEngineAnnotatedDL;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Simple rest tests.
 * this really should be a spring test that stands up a spring application for testing purposes.
 * or a mock server.
 * Created by Eric Angeli on 5/16/2017.
 */
public class RATests extends TestNGEngineAnnotatedDL {

    @Test(groups = {"unit"})
    public void getGoogle() {
        Rest rest = new RABasicAuthHeader();
        Response response = (Response) rest.baseURL("https://www.google.com").get("");
        data.put("response", response);
        assertEquals(response.statusCode(), 200);
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect("api response>status code,==,200");
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        assertTrue(result, ev.failed());
    }

    @Test(groups = {"unit"})
    public void getGoogleByEmployee() {
        Goate d = new Goate().put("base url", "https://www.google.com");
        Employee e = new ApiGet().init(d);
        Response response = (Response) e.work();
        assertEquals(response.statusCode(), 200);
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(d);
        etb.expect("api response>status code,==,200");
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        assertTrue(result, ev.failed());
    }
}
