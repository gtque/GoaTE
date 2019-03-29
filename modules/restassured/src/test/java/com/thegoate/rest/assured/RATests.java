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
import com.thegoate.expect.Expectation;
import com.thegoate.expect.ExpectationThreadBuilder;
import com.thegoate.rest.Rest;
import com.thegoate.rest.RestCall;
import com.thegoate.rest.RestResult;
import com.thegoate.rest.staff.ApiGet;
import com.thegoate.simpleserver.SimpleServer;
import com.thegoate.spring.SpringTestEngine;
import com.thegoate.staff.Employee;
import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.testng.annotations.Test;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Simple rest tests.
 * this really should be a spring test that stands up a spring application for testing purposes.
 * or a mock server.
 * Created by Eric Angeli on 5/16/2017.
 */
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SimpleServer.class)
@TestExecutionListeners(inheritListeners = false, listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
public class RATests extends SpringTestEngine {

    @Test(groups = {"unit"})
    public void getURL() {
        Rest rest = new RABasicAuthHeader();
        Response response = (Response) rest.baseURL(baseURL()).get("bump/count");
        data.put("response", response);
        assertEquals(response.statusCode(), 200);
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect("api response>status code,==,200");
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        assertTrue(result, ev.failed());
    }

    @Test(groups = {"unit"})
    public void getURLRestWithExpectedWorker() {
        Response response = (Response) new RestCall()
                .baseURL(baseURL())
//                .get("/".concat(eut("endpoint.base","api")).concat("/payment-advice/{paymentAdviceId}"));
                .get("hello/world");
        data.put("response", response);

//        assertEquals(response.statusCode(), 200);
//        etb.expect("api response>status code,==,200");
        Employee callAnotherURL = new CallAnotherURL().baseURL(baseURL());
        expect(new Expectation(data).actual(response.statusCode()).is("==").expected(200));
        expect(Expectation.build()
                .actual(RestResult.statusCode)
                .from(response)
                .isEqualTo(200));
        expect(Expectation.build()
                .actual(RestResult.statusCode)
                .from(response)
                .isNotEqualTo(RestResult.statusCode)
                .fromExpected("{\"status code\": 201}"));
        expect(Expectation.build()
                .actual(RestResult.bodyAsAString)
                .fromClone(response.body().prettyPrint())
                .isNotEqualTo(RestResult.bodyAsAString)
                .fromExpected(callAnotherURL));
        expect(Expectation.build()
                .actual(RestResult.statusCode)
                .from(response)
                .isEqualTo(RestResult.statusCode)
                .fromExpected(callAnotherURL));
        expect(Expectation.build()
                .actual(200)
                .isEqualTo(RestResult.statusCode)
                .fromExpected(callAnotherURL));
    }

    @Test(groups = {"unit"})
    public void getURLByEmployee() {
        Goate d = new Goate().put("base url", baseURL()).put("end point", "hello/world");
        Employee e = new ApiGet().init(d);
        Response response = (Response) e.work();
        assertEquals(response.statusCode(), 200);
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(d);
        etb.expect("api response>status code,==,200");
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        assertTrue(result, ev.failed());
    }

    @Test(groups = {"unit"})
    public void testRestResultBodyAsAString() {
        String expected = "{\"greeting\":\"hello\"}";
        Object result = new RestCall().baseURL(baseURL())
                .get("hello/world");
        expect(Expectation.build()
                .actual(RestResult.statusCode)
                .from(result)
                .isEqualTo(200));
        expect(Expectation.build()
                .actual(RestResult.bodyAsAString)
                .from(result)
                .isEqualTo(expected));
        evaluate();
        assertEquals(getEv().passes().size(),2);
    }
}
