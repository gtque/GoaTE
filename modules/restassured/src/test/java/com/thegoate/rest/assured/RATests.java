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
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

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
	public void putURL() {
		Rest rest = new RABasicAuthHeader();
		Response response = (Response) rest.baseURL(baseURL()).put("hello/world");
		data.put("response", response);
		assertEquals(response.statusCode(), 200);
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect("api response>status code,==,200");
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		assertTrue(result, ev.failed());
	}

	@Test(groups = {"unit"})
	public void deleteURL() {
		Rest rest = new RABasicAuthHeader();
		//        LOG.debug("Start", "Calling delete end point");
		Response response = (Response) rest.baseURL(baseURL()).delete("hello/world");
		data.put("response", response);
		//        LOG.debug("Called", "Endpoint returned: " + response.getStatusCode());
		assertEquals(response.statusCode(), 200);
		//        response.getBody().prettyPrint();
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect("api response>status code,==,200");
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev, result);
		assertTrue(result, ev.failed());
	}

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
	public void premal() {
		Rest rest = new RestAssured();
		Response response = (Response) rest.baseURL(baseURL()).get("breakingthings");
		data.put("response", response);
		data.put("expected", "{\n" +
			"    \"title\": \"Unprocessable Entity\",\n" +
			"    \"status\": 422,\n" +
			"    \"detail\": \"Input data was invalid. See exceptions for details.\",\n" +
			"    \"exceptions\": [\n" +
			"        {\n" +
			"            \"code\": \"API_VALIDATION_ERROR\",\n" +
			"            \"message\": \"numeric value out of bounds (<18 digits>.<2 digits> expected)\",\n" +
			"            \"field\": \"amount\"\n" +
			"        }\n" +
			"    ]\n" +
			"}");
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect("api response>body as a string,isEqualIgnoreFields,o::expected");
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		assertTrue(result, ev.failed());
	}

	@Test(groups = {"unit"})
	public void debugExtraLines() {
		Response response = (Response) new RestCall()
			.baseURL(baseURL())
			//                .get("/".concat(eut("endpoint.base","api")).concat("/payment-advice/{paymentAdviceId}"));
			.get("hello/world");
		data.put("response", response);

		//        assertEquals(response.statusCode(), 200);
		//        etb.expect("api response>status code,==,200");
		//        Employee callAnotherURL = new CallAnotherURL().baseURL(baseURL());
		//        expect(new Expectation(data).actual(response.statusCode()).is("==").expected(200));
		expect(Expectation.build()
			.actual(RestResult.statusCode)
			.from(response)
			.isEqualTo(200));
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
			.from(response)
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
		assertEquals(getEv().passes().size(), 2);
	}

	@Test(groups = {"unit"})
	public void testGetRestBasicAuthHeader() {
		Object result = new RABasicAuth().user("fred").password("rogers").baseURL(baseURL())
			.get("hello/auth");
		expect(Expectation.build()
			.actual("user")
			.from(result)
			.isEqualTo("fred"));
		expect(Expectation.build()
			.actual("password")
			.from(result)
			.isEqualTo("rogers"));
	}

	@Test(groups = {"unit"})
	public void testDeleteRestBasicAuthHeader() {
		Object result = new RABasicAuth().user("fred").password("rogers").baseURL(baseURL())
			.delete("hello/auth");
		expect(Expectation.build()
			.actual("user")
			.from(result)
			.isEqualTo("fred"));
		expect(Expectation.build()
			.actual("password")
			.from(result)
			.isEqualTo("rogers"));
	}

	@Test(groups = {"unit"})
	public void testListString() {
		List<String> simpleList = new ArrayList<>();
		simpleList.add("a");
		simpleList.add("b");
		simpleList.add("c");
		Object result = new RestCall().queryParam("list", "a,b,c").baseURL(baseURL())
			.get("hello/list");
		expect(Expectation.build()
			.actual("size")
			.from(result)
			.isEqualTo(simpleList.size()));
	}

	@Test(groups = {"unit"})
	public void testListObject() {
		List<String> simpleList = new ArrayList<>();
		simpleList.add("a");
		simpleList.add("b");
		simpleList.add("c");
		RestCall<Response> rest = new RestCall().queryParam("list", simpleList).baseURL(baseURL());
		Response result = rest
			.get("hello/list");
		expect(Expectation.build()
			.actual("size")
			.from(result)
			.isEqualTo(simpleList.size()));
	}


	@Test(groups = {"unit"})
	public void testPutRestBasicAuthHeader() {
		Object result = new RABasicAuth().user("fred").password("rogers").baseURL(baseURL())
			.put("hello/auth");
		expect(Expectation.build()
			.actual("user")
			.from(result)
			.isEqualTo("fred"));
		expect(Expectation.build()
			.actual("password")
			.from(result)
			.isEqualTo("rogers"));
	}

	@Test(groups = {"unit"})
	public void mpFileUploadTestNoType() {
		Object rest = new RestCall()
			.multipart("simpleF", "fileIO::mp_upload.json")
			.baseURL(baseURL())
			.post("hello/mpfile");
		expect(Expectation.build()
			.actual("field")
			.from(rest)
			.isEqualTo("Jeefff"));
		expect(Expectation.build()
			.actual("type")
			.from(rest)
			.isEqualTo("application/octet-stream"));
	}

	@Test(groups = {"unit"})
	public void mpFileUploadTestWithType() {
		Object rest = new RestCall()
			.multipart("simpleF", "fileIO::mp_upload.json", "application/json")
			.baseURL(baseURL())
			.post("hello/mpfile");
		expect(Expectation.build()
			.actual("field")
			.from(rest)
			.isEqualTo("Jeefff"));
		expect(Expectation.build()
			.actual("type")
			.from(rest)
			.isEqualTo("application/json"));
	}

	@Test(groups = {"unit"})
	public void isPresentScalabilityTesting() {
		Object body = new RestCall()
			.baseURL(baseURL())
			.get("hello/big");
		//		int size = Integer.parseInt(""+new Get("content>size()").from(body));
		//		for(int i = 0; i<size-1; i++) {
//		muteFrom = true;
		expect(Expectation.build()
			.actual("content.*.id")
			.from(body)
			.isPresent(true));
		expect(Expectation.build()
			.actual("content.*.a")
			.from(body)
			.isPresent(true));
		expect(Expectation.build()
			.actual("content.*.b")
			.from(body)
			.isPresent(true));
		expect(Expectation.build()
			.actual("content.*.c")
			.from(body)
			.isPresent(true));
		expect(Expectation.build()
			.actual("content.*.d")
			.from(body)
			.isPresent(true));
		expect(Expectation.build()
			.actual("content.*.e")
			.from(body)
			.isPresent(true));
		expect(Expectation.build()
			.actual("content.*.f")
			.from(body)
			.isPresent(true));
		expect(Expectation.build()
			.actual("content.*.g")
			.from(body)
			.isPresent(true));
		expect(Expectation.build()
			.actual("content.*.h")
			.from(body)
			.isPresent(true));
		expect(Expectation.build()
			.actual("content.*.i")
			.from(body)
			.isPresent(true));
		expect(Expectation.build()
			.actual("content.*.j")
			.from(body)
			.isPresent(true));
		expect(Expectation.build()
			.actual("content.*.k")
			.from(body)
			.isPresent(true));
		expect(Expectation.build()
			.actual("content.*.l")
			.from(body)
			.isPresent(true));
		expect(Expectation.build()
			.actual("content.*.m")
			.from(body)
			.isPresent(true));
		expect(Expectation.build()
			.actual("content.*.n")
			.from(body)
			.isPresent(true));
		expect(Expectation.build()
			.actual("content.*.o")
			.from(body)
			.isPresent(true));
		expect(Expectation.build()
			.actual("content.*.p")
			.from(body)
			.isPresent(true));
		//	}
	}

    @Test(groups = {"unit"})
    public void pathParametersTestHello() {
        Object rest = new RestCall<>()
                .baseURL(baseURL())
				.urlEncode(false)
                .get("test//hello");
//        expect(Expectation.build()
//                .actual("error")
//                .from(rest)
//                .isEqualTo("nemo"));
    }

	@Test(groups = {"unit"})
	public void pathParametersTestHey() {
		Object rest = new RestCall<>()
				.baseURL(baseURL())
				.urlEncode(false)
				.pathParam("name", "empty::")
				.post("test/{name}/hey");
//		expect(Expectation.build()
//				.actual("error")
//				.from(rest)
//				.isEqualTo("nemo"));
	}
}
