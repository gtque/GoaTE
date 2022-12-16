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

package com.thegoate.json.utils.compare;

import com.thegoate.Goate;
import com.thegoate.data.StaticDL;
import com.thegoate.expect.ExpectEvaluator;
import com.thegoate.expect.ExpectationThreadBuilder;
import com.thegoate.testng.TestNGEngineMethodDL;
import com.thegoate.utils.fill.FillString;

import org.json.JSONObject;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Tests against CompareJson utilities.
 * Created by Eric Angeli on 10/5/2017.
 */
public class CompareJsonTests extends TestNGEngineMethodDL {

	public CompareJsonTests() {
		super();
	}

	@Factory(dataProvider = "dataLoader")
	public CompareJsonTests(Goate data) {
		super(data);
	}

	@Override
	public void defineDataLoaders() {
		runData
			.put("dl##", new StaticDL().add("Scenario", "equals returns true when equal")
				.add("json1", "{\"e\":\"y\",\"c\":\"x\",\"d\":\"z\",\"r\":[{\"a\":\"c\",\"g\":\"h\"},{\"a\":\"b\"},[\"a\"]]}")
				.add("json2", "{\"c\":\"x\",\"d\":\"z\",\"r\":[{\"a\":\"b\"},{\"a\":\"c\",\"g\":\"h\"},[\"a\"]],\"e\":\"y\"}")
				.add("operator", "==")
				.add("expected", true))
			.put("dl##", new StaticDL().add("Scenario", "equals returns false when not equal")
				.add("json1", "{\"e\":\"y\",\"c\":\"x\",\"d\":\"z\",\"r\":[{\"a\":\"c\"},{\"a\":\"b\"},[\"a\"]]}")
				.add("json2", "{\"frickle\":\"frackle\",\"c\":\"x\",\"d\":\"hello\",\"r\":[{\"a\":\"b\"},{\"a\":\"b\"},[\"a\"]],\"e\":\"y\"}")
				.add("operator", "==")
				.add("expected", false))
			.put("dl##", new StaticDL().add("Scenario", "Complex json")
				.add("json1", "file::complex.json")
				.add("json2", "file::complex.json")
				.add("operator", "==")
				.add("expected", true))
			.put("dl##", new StaticDL().add("Scenario", "isEmpty json array not empty fail")
				.add("json1", "['one':1]")
				.add("json2", true)
				.add("operator", "isEmpty")
				.add("expected", false))
			.put("dl##", new StaticDL().add("Scenario", "isEmpty json array not empty pass")
				.add("json1", "['one':1]")
				.add("json2", false)
				.add("operator", "isEmpty")
				.add("expected", true))
			.put("dl##", new StaticDL().add("Scenario", "isEmpty json array empty fails")
				.add("json1", "[]")
				.add("json2", false)
				.add("operator", "isEmpty")
				.add("expected", false))
			.put("dl##", new StaticDL().add("Scenario", "isEmpty json array")
				.add("json1", "[]")
				.add("json2", true)
				.add("operator", "isEmpty")
				.add("expected", true))
			.put("dl##", new StaticDL().add("Scenario", "isEmpty json null")
				.add("json1", JSONObject.NULL)
				.add("json2", true)
				.add("operator", "isEmpty")
				.add("expected", true))
		;
	}

	@Test(groups = {"unit"})
	public void compareJson() {
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		String check = new FillString("o::json1,${operator},o::json2").with(data).toString();
		etb.expect(check);
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertEquals(result, get("expected"), ev.failed());
		//        assertTrue(result, ev.failed());
		//        if (!result) {
		//            for (Goate f : ev.fails()) {
		//                LOG.fail(getTestName(), "FAILED: " + f.toString());
		//            }
		//        }
		//LOG.debug("failed message:\n" + ev.failed());
		//        assertEquals(new Compare(get("json1")).to(get("json2")).using("" + get("operator")).evaluate(), get("expected"));
	}


}
