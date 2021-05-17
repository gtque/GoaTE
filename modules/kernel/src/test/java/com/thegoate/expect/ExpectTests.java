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

package com.thegoate.expect;

import com.thegoate.Goate;
import com.thegoate.data.GoateDLP;
import com.thegoate.data.GoateProvider;
import com.thegoate.data.StaticDL;
import com.thegoate.expect.builder.ContainsExpectationBuilder;
import com.thegoate.expect.builder.ModelIsPresentOptional;
import com.thegoate.expect.builder.Value;
import com.thegoate.expect.test.*;
import com.thegoate.expect.validate.Validate;
import com.thegoate.expect.validate.ValidateAbsence;
import com.thegoate.expect.validate.ValidateNotGoate;
import com.thegoate.json.utils.fill.serialize.to.JsonString;
import com.thegoate.json.utils.insert.InsertJson;
import com.thegoate.reflection.Executioner;
import com.thegoate.reflection.test.SkipThread;
import com.thegoate.testng.ExpectToFail;
import com.thegoate.testng.TestNGEngineAnnotatedDL;
import com.thegoate.testng.TestNGEngineMethodDL;
import com.thegoate.utils.fill.Fill;
import com.thegoate.utils.fill.serialize.DeSerializer;
import com.thegoate.utils.fill.serialize.DefaultSource;
import com.thegoate.utils.fill.serialize.Serializer;
import com.thegoate.utils.get.Get;
import com.thegoate.utils.togoate.ToGoate;

import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.*;

import static com.thegoate.dsl.words.LoadFile.fileAsAString;
import static com.thegoate.expect.ExpectLocator.start;
import static com.thegoate.expect.ExpectMatchWildcardIndexPath.matchWildcardIndex;
import static com.thegoate.expect.ExpectWildcardIndexPath.wildcardIndex;
import static com.thegoate.locate.Locate.path;
import static org.testng.Assert.*;

/**
 * Test expect framework.
 * Created by Eric Angeli on 5/10/2017.
 */
public class ExpectTests extends TestNGEngineMethodDL {

	//	final BleatBox LOG = BleatFactory.getLogger(getClass());
	String sample = "{\n" +
		"	\"empty\":[],\n" +
		"	\"there_but_missing\":[[{\"cheese\":\"burger\"}]],\n" +
		"	\"there_but_empty2\":[[{\"field\":\"cheezeburger\"}],[]],\n" +
		"	\"there_and_filled\":[[{\"field\":\"cheezeburger\"}],[{\"field\":\"cheezeburger\"}]],\n" +
		"    \"data\": [\n" +
		"        {\n" +
		"            \"a\": null,\n" +
		"            \"b\": null,\n" +
		"            \"id\": \"8ac2a3d05d37576c015d3c4b3135000e\"\n" +
		"        },\n" +
		"        {\n" +
		"            \"a\": null,\n" +
		"            \"c\": 42,\n" +
		"            \"id\": \"8ac2a3d05d37576c015d3c4b3135000f\"\n" +
		"        }\n" +
		"    ],\n" +
		"    \"mi\": [\n" +
		"        [\n" +
		"            {\n" +
		"                \"scooby\":\"doo\"\n" +
		"            }\n" +
		"        ]" +
		"    ],\n" +
		"    \"page\": null\n" +
		"}";

	@Test(groups = {"unit"})
	public void isEven() {
		Goate data = new Goate();
		data.put("check if even.value", 4)
			.put("check if even#1.value", 5);
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		Expectation e2 = new Expectation(data).from("check if even#1").actual("return").isNotEqualTo(true);//.is("!=").expected(true);
		etb.expect("check if even>return,==,boolean::true").expect(e2)
			.expect("check if even>return,!=,boolean::false");
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertTrue(result, ev.failed());
	}

	@Test(groups = {"unit"})
	public void isEvenEmployee() {
		expect(Expectation.build()
			.actual(Expectation.EmployeeWorkResult)
			.from(new CheckEven().check(84))
			.isEqualTo(true));
	}

	@Test(groups = {"unit"})
	public void isCustomObjectEmployee() {
		expect(Expectation.build()
			.actual(Expectation.EmployeeWorkResult)
			.from(new EmployeeReturnsAnObject().name("floyd").configured(true))
			.isEqualTo(new CustomObject().setName("floyd").setConfigured(true)));
	}

	@Test(groups = {"unit"})
	public void volumeCheck() {

		LOG.debug("volumeCheck", "checking the size of the first page.");
		Goate expected = new ToGoate("{'a':123,'b':true,'c':[{'x':'y'}]}").convert();
		String cheeseData = fileAsAString("data/big.json");
		Object firstPage = new Get("content").from(cheeseData);
		Goate cheese = new ToGoate(firstPage).convert();
		LOG.debug("volumeCheck", "checking the size of the first page.");
		int size = Integer.parseInt("" + new Get("size()").from(firstPage));//loans.filterStrict("[0-9]*").size();
		LOG.debug("volumeCheck", "size of the first page:" + size);
		for (int i = 0; i < size - 1; i++) {
			LOG.debug("volumeCheck", "adding new page:" + i);
			//            expect(Expectation.build()
			//                    .actual("" + i + ".loanNumber")
			//                    .from(loans)
			//                    .isLessThan("" + (i+1) + ".loanNumber")
			//                    .fromExpected(loans));
			expect(Expectation.build()
				.actual("cheeseNumber")
				.from(cheese.get("" + i))
				.isLessThan("cheeseNumber")
				.fromExpected(cheese.get("" + (i + 1))));
		}

		LOG.debug("expecting: " + etb.expectations());
	}

	@Test(groups = {"unit"})
	public void isEvenButReallyOdd() {
		Goate data = new Goate();
		data.put("check if even.value", 43);
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect("check if even>return,==,boolean::true");
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertFalse(result, ev.failed());
		//        LOG.debug("failed message:\n" + ev.failed());
	}

	@Test(groups = {"unit"})
	public void isEvenFail() {
		Goate data = new Goate();
		data.put("check if even.value", 42);
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect("check if even>return,==,boolean::true");
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		assertFalse(result, ev.failed());
		LOG.debug("failed message:\n" + ev.failed());
	}

	@Test(groups = {"unit"})
	public void checkJson() {
		Goate data = new Goate();
		String json = "{" +
			"'a':[" +
			"{'b':42}," +
			"{'b':43}," +
			"{'b':44}," +
			"{'b':45}]" +
			"}";
		data.put("check json.value", json);
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect("check json>a.*.b,>,int::41");
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		assertTrue(result, ev.failed());
		LOG.debug("failed message:\n" + ev.failed());
	}

	@Test(groups = {"unit"})
	public void checkJsonNested() {
		Goate data = new Goate();
		String json = "{" +
			"'a':[" +
			"[{'b':42}," +
			"{'b':43}," +
			"{'b':44}," +
			"{'b':45}]," +
			"[{'b':42}," +
			"{'b':43}," +
			"{'b':44}," +
			"{'b':45}]]," +
			"'z':[" +
			"[{'y':42}," +
			"{'y':43}," +
			"{'y':44}," +
			"{'y':45}]," +
			"[{'y':42}," +
			"{'y':43}," +
			"{'y':44}," +
			"{'y':45}]]" +
			"}";
		data.put("check json.value", json);
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect("check json>a.*.[*4].b,>,int::41");
		//        etb.expect(Expectation.build().actual("a.*.*.b").from(json).isEqualTo("z.%.\\%.y").fromExpected(json));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		StringBuilder ps = new StringBuilder();
		for (Goate p : ev.passes()) {
			ps.append(p.toString());
			ps.append("\n--------------------\n");
		}
		LOG.debug("passed:\n" + ps.toString());
		StringBuilder fs = new StringBuilder();
		for (Goate p : ev.fails()) {
			fs.append(p.toString());
			fs.append("\n--------------------\n");
		}
		LOG.debug("failed:\n" + fs.toString());
		assertTrue(result, ev.failed());
	}

	@Test
	public void regexSampler() {
		String pattern = "a.123.v.5.78950.z";
		String pattern2 = "a.%.v.\\%.\\\\%.z";
		String pattern3 = "a.\\%.v.\\\\%.%.5.z";
		String[] index = pattern.split("[0-9]+");
		Arrays.stream(index).forEach(i -> System.out.println(i));
		System.out.println(index.length);
		Validate e = new ValidateNotGoate(null, null, null, null, null, 0, new Goate());
		String p = "" + e.calculateKey(pattern2, pattern);
		String p3 = "" + e.calculateKey(pattern3, pattern);
		System.out.println(p);
		System.out.println(p3);
		assertEquals(p, pattern);
		assertEquals(p3, "a.5.v.78950.123.5.z");
	}

	@Test(groups = {"unit"})
	public void checkJsonFromFileNested() {
		Goate data = new Goate();
		String json = fileAsAString("data/simpleNested.json");
		data.put("check json.value", json);
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(
			Expectation.build()
				.actual("a.".concat(wildcardIndex()).concat(".").concat(wildcardIndex()).concat(".").concat(wildcardIndex()).concat(".x"))
				.from(json)
				.isEqualTo("b.".concat(matchWildcardIndex()).concat(".").concat(matchWildcardIndex(1)).concat(".").concat(matchWildcardIndex(2)).concat(".y"))
				.fromExpected(json));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		assertTrue(result, ev.failed());
		LOG.debug("failed message:\n" + ev.failed());
	}

	@Test(groups = {"unit"})
	public void checkJsonFromFileNestedExpectLocator() {
		Goate data = new Goate();
		String json = fileAsAString("data/simpleNested.json");
		data.put("check json.value", json);
		expect(
			Expectation.build()
				//      "a.".concat(wildcardIndex()).concat(".").concat(wildcardIndex()).concat(".").concat(wildcardIndex()).concat(".x")
				.actual(start("a").dot().wildcardIndex().dot().wildcardIndex().dot().wildcardIndex().dot().end("x"))
				.from(json)
				//         "b.".concat(matchWildcardIndex()).concat(".").concat(matchWildcardIndex(1)).concat(".").concat(matchWildcardIndex(2)).concat(".y")
				.isEqualTo(start("b").dot().matchIndex().dot().matchIndex(1).dot().matchIndex(2).dot().match("y"))
				.fromExpected(json));
	}

	@Test(groups = {"unit"})
	public void justPrintExpectations() {
		List<String> from = new ArrayList<String>();
		from.add("hello");
		from.add("hello");
		List<String> expected = new ArrayList<String>();
		expected.add("hello");
		expected.add("hello");
		//		Goate from = new Goate().put("1", true).put("2", false).put("3", true);
		expect(Expectation.build()
			.actual(from)
			.isEqualTo(expected));
	}

	@Test(groups = {"unit"})
	public void checkJsonNestedTooMany() {
		Goate data = new Goate();
		String json = "{" +
			"'a':[" +
			"[{'b':42}," +
			"{'b':43}," +
			"{'b':44}," +
			"{'b':45}]," +
			"[{'b':42}," +
			"{'b':43}," +
			"{'b':44}," +
			"{'b':45}]]" +
			"}";
		data.put("check json.value", json);
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect("check json>a.*.[*3].b,>,int::41");
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		assertFalse(result, ev.failed());
		LOG.debug("failed message:\n" + ev.failed());
	}

	@Test(groups = {"unit"})
	public void checkJsonNestedTooFew() {
		Goate data = new Goate();
		String json = "{" +
			"'a':[" +
			"[{'b':42}," +
			"{'b':43}," +
			"{'b':44}," +
			"{'b':45}]," +
			"[{'b':42}," +
			"{'b':43}," +
			"{'b':44}," +
			"{'b':45}]]" +
			"}";
		data.put("check json.value", json);
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect("check json>a.*.[*5].b,>,int::41");
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		assertFalse(result, ev.failed());
		LOG.debug("failed message:\n" + ev.failed());
	}

	@Test(groups = {"unit"})
	public void checkJsonNestedFailure() {
		Goate data = new Goate();
		String json = "{" +
			"'a':[" +
			"[{'b':42}," +
			"{'b':43}," +
			"{'b':44}," +
			"{'b':45}]," +
			"[{'b':42}," +
			"{'b':40}," +
			"{'b':44}," +
			"{'b':45}]]" +
			"}";
		data.put("check json.value", json);
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect("check json>a.*.*.b,>,int::41");
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		assertFalse(result, ev.failed());
		LOG.debug("failed message:\n" + ev.failed());
	}

	@Test(groups = {"unit"})
	public void checkJsonNotExist() {
		Goate data = new Goate();
		String json = "{" +
			"'a':[" +
			"{'b':42}," +
			"{'b':43}," +
			"{'b':44}," +
			"{'b':45}]" +
			"}";
		data.put("check json.value", json);
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect("check json>a.*.c,>,int::41");
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		assertTrue(result, ev.failed());
		LOG.debug("failed message:\n" + ev.failed());
	}

	@Test(groups = {"unit"})
	public void checkJsonBaseElementNotExist() {
		Goate data = new Goate();
		String json = "{" +
			"'a':[" +
			"{'b':42}," +
			"{'b':43}," +
			"{'b':44}," +
			"{'b':45}]" +
			"}";
		data.put("check json.value", json);
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect("check json>e.*.c,>,int::41");
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		assertTrue(result, ev.failed());
		LOG.debug("failed message:\n" + ev.failed());
	}

	@Test(groups = {"unit"})
	public void checkJsonNotJson() {
		Goate data = new Goate();
		String json = "" +
			"hello world!";
		data.put("check json.value", json);
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect("check json>e.*.c,>,int::41");
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		assertTrue(result, ev.failed());
		LOG.debug("failed message:\n" + ev.failed());
	}

	@Test(groups = {"unit"})
	public void checkJsonNullJson() {
		Goate data = new Goate();
		String json = null;
		data.put("check json.value", json);
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect("check json>e.*.c,>,int::41");
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		assertTrue(result, ev.failed());
		LOG.debug("failed message:\n" + ev.failed());
	}

	@Test(groups = {"unit"})
	public void selfDefined() {
		assertTrue(new Expectation(new Goate()).actual(42).is("==").expected(42).evaluate());
	}

	@Test(groups = {"unit"})
	public void selfDefinedExpectationBuilder() {
		Goate data = new Goate();
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(new Expectation(new Goate()).actual(42).is("==").expected(42))
			.expect(new Expectation(new Goate()).actual(42).is("==").expected("42"))
			.expect(new Expectation(new Goate()).actual(42).is("!=").expected("Fiddle Sticks"))
			.expect(new Expectation(new Goate()).actual("Hello").is("!=").expected("Howdy"))
		;
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		assertTrue(ev.evaluate());
	}

	@Test(groups = {"unit"})
	public void selfDefinedDefinition() {
		Goate data = new Goate();
		data.put("id", "42a");
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(new Expectation(data).define("o::id,==,42a"));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		assertTrue(ev.evaluate());
	}

	@Test(groups = {"unit"})
	public void isEqualNullFail() {
		Goate data = new Goate();
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual(null)
			.isEqualTo("hello"));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertFalse(result);
	}

	@Test(groups = {"unit"})
	public void isEqualNull() {
		Goate data = new Goate();
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual(null)
			.isEqualTo(null));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertTrue(result);
	}

	@Test(groups = {"unit"})
	public void isNullNull() {
		Goate data = new Goate();
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual(null)
			.isNull(true));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertTrue(result);
	}

	@Test(groups = {"unit"})
	public void isNullNullFalse() {
		Goate data = new Goate();
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual(null)
			.isNull(false));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertFalse(result);
	}

	@Test(groups = {"unit"})
	public void isNullValue() {
		Goate data = new Goate();
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual(42)
			.isNull(true));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertFalse(result);
	}

	@Test(groups = {"unit"})
	public void isNullValueFalse() {
		Goate data = new Goate();
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual("cheese")
			.isNull(false));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertTrue(result);
	}

	@Test(groups = {"unit"})
	public void isPresent() {
		Goate data = new Goate();
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual("data.+.id").from(sample)
			.isPresent(true));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertTrue(result);
	}

	@Test(groups = {"unit"})
	public void isPresentFalse() {
		Goate data = new Goate();
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual("data.*.vitaliy").from(sample)
			.isPresent(false));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertTrue(result);
	}

	@Test(groups = {"unit"})
	public void isPresentFalseButIsPresent() {
		Goate data = new Goate();
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual("data.*.id").from(sample)
			.isPresent(false));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertFalse(result);
	}

	@Test(groups = {"unit"})
	public void isJsonNullTrueOneOrMore() {
		Goate data = new Goate();
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual("data.+.a").from(sample)
			.isNull(true));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertTrue(result);
	}

	@Test(groups = {"unit"})
	public void goateNullIsPresentTrueOneOrMore() {
		Goate data = new Goate();
		data.get("data.0.a", null);
		data.get("data.1.a", null);
		data.get("data.2.a", null);
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual("data.+.a").from(data)
			.isPresent(true));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertTrue(result);
	}

	@Test(groups = {"unit"})
	public void isJsonNullPresentTrueOneOrMore() {
		Goate data = new Goate();
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual("data.+.a").from(sample)
			.isPresent(true));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertTrue(result);
	}

	@Test(groups = {"unit"})
	public void isPresentZeroOrMoreNotPresentButShouldBe() {
		Goate data = new Goate();
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual("data.+.z").from(sample)
			.isNull(true));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertFalse(result);
	}

	@Test(groups = {"unit"})
	public void isJsonNullZeroOrMore() {
		Goate data = new Goate();
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual("data.*.b").from(sample)
			.isNull(true));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertTrue(result);
	}

	@Test(groups = {"unit"})
	public void isJsonNullFalse() {
		Goate data = new Goate();
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual("data.+.id").from(sample)
			.isNull(false));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertTrue(result);
	}

	@Test(groups = {"unit"})
	public void isJsonNullFalseOneOrMoreFail() {
		Goate data = new Goate();

		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual("data.+.c").from(sample)
			.isNull(false));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertFalse(result);
	}

	@Test(groups = {"unit"})
	public void notExecuted() {
		Goate data = new Goate();
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual(null)
			.is("throwNullPointer").expected(42));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertFalse(result);
	}

	@Test(groups = {"unit"})
	public void notExecutedNotFound() {
		Goate data = new Goate();
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual("42")
			.is("tricky_bits_are_tricky").expected(42));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertFalse(result);
	}

	@Test(groups = {"unit"})
	public void notExecutedFrom() {
		Goate data = new Goate();
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual("data.*.id").from(sample)
			.is("throwNullPointer").expected(42));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertFalse(result);
	}

	String response = "{\n" +
		"    \"data\": [\n" +
		"        {\n" +
		"            \"id\": \"8ac2a3d05d37576c015d3c4b3135000e\",\n" +
		"            \"b\": true" +
		"        },\n" +
		"        {\n" +
		"            \"id\": \"8ac2a3d05d37576c015d3c4b3135000f\"\n" +
		"        }\n" +
		"    ],\n" +
		"    \"page\": null\n" +
		"}";

	@Test(groups = {"unit"})
	public void validatePresenceNotFoundFalseOneOrMoreFails() {
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual(path().match("data").dot().matchOneOrMore("[0-9]").dot().match("b")).from(response)
			.validate(ValidateAbsence.using(IsNotFound.class)).expected(false));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertFalse(result);
	}

	@Test(groups = {"unit"})
	public void validatePresenceNotFoundFalseZeroOrMorePasses() {
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual(path().match("data").dot().matchZeroOrMore("[0-9]").dot().match("b")).from(response)
			.validate(ValidateAbsence.using(IsNotFound.class)).expected(false));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertTrue(result);
	}

	@Test(groups = {"unit"})
	public void validatePresenceNotFoundFalseOneOrMoreNoneFails() {
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual(path().match("data").dot().matchOneOrMore("[0-9]").dot().match("c")).from(response)
			.validate(ValidateAbsence.using(IsNotFound.class)).expected(false));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertFalse(result);
	}

	@Test(groups = {"unit"})
	public void validatePresenceNotFoundTrueOneOrMoreFails() {
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual(path().match("data").dot().anyNumberOneOrMore().dot().match("b")).from(response)
			.validate(ValidateAbsence.using(IsNotFound.class)).expected(true));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertFalse(result);
	}

	@Test(groups = {"unit"})
	public void validatePresenceNotFoundFalseOneOrMorePasses() {
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual(path().match("data").dot().matchOneOrMore("[0-9]").dot().match("id")).from(response)
			.validate(ValidateAbsence.using(IsNotFound.class)).expected(false));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertTrue(result);
	}

	@Test(groups = {"unit"})
	public void validatePresenceNotFoundTrueOneOrMorePasses() {
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual(path().match("data").dot().anyNumberOneOrMore().dot().match("c")).from(response)
			.validate(ValidateAbsence.using(IsNotFound.class)).expected(true));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertTrue(result);
	}

	@Test(groups = {"unit"})
	public void validatePresenceNotFoundFalseZeroOrMoreStillPasses() {
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual(path().match("data").dot().anyNumberZeroOrMore().dot().match("c"))
			.from(response)
			.validate(ValidateAbsence.using(IsNotFound.class))
			.expected(false));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertTrue(result);
	}

	String fullModel = "{\n" +
		"    \"data\": [\n" +
		"        {\n" +
		"            \"id\": \"8ac2a3d05d37576c015d3c4b3135000e\",\n" +
		"            \"b\": true," +
		"            \"c\": {" +
		"               \"z\":42" +
		"            }" +
		"        }\n" +
		"    ],\n" +
		"    \"page\": null\n" +
		"}";
	String requiredModel = "{\n" +
		"    \"data\": [\n" +
		"        {\n" +
		"            \"id\": \"8ac2a3d05d37576c015d3c4b3135000e\"\n" +
		"        }\n" +
		"    ],\n" +
		"    \"page\": null\n" +
		"}";
	String optionalModel = "{\n" +
		"    \"data\": [\n" +
		"        {\n" +
		"            \"b\": true" +
		"        }\n" +
		"    ]\n" +
		"}";

	@Test(groups = {"unit"})
	public void conditionalModelIsPresent() {
		expect(new ModelIsPresentOptional()
			.addOptionalField(path().match("data").dot().anyNumberZeroOrMore().dot().match("b"))
			.addOptionalField(path().match("data").dot().anyNumberZeroOrMore().dot().match("c"))
			.setActual(response)
			.setExpected(fullModel));
	}

	String jaoSample = "{\"list\":["
		+ "{\"field1\":\"a\"},"
		+ "{\"field1\":\"a\"},"
		+ "{\"field1\":\"a\"},"
		+ "{\"field1\":\"a\"}"
		+ "]}";
	String jaoFullModel = "{\"list\":["
		+ "{\"field1\":\"a\"}"
		+ "]}";
	String jaoPartialModel = "{\"field1\":\"a\"}";

	@Test(groups = {"unit"})
	public void jsonArrayOfObjects() {
		//I am using the one or more wild card operator, +,
		//this expects at least one element in the json array.
		//You can use the zero or more operator, *,
		//but this will still pass if there is nothing in the json array, so use as appropriate.

		//check if the json array has something in it, regardless of what that something is.
		expect(Expectation.build()
			.actual("list.+")
			.from(jaoSample)
			.isPresent(true));

		//check if a field is in an object in a json array, regardless of value
		expect(Expectation.build()
			.actual("list.+.field1")
			.from(jaoSample)
			.isPresent(true));

		//check if value of a field in an object in the json array equals the expecte value.
		expect(Expectation.build()
			.actual("list.+.field1")
			.from(jaoSample)
			.isEqualTo("a"));

		//using a Conditional builder, this is not covered by tutorials, at least not yet.
		//it allows you to define a base model for the expected json and then check that the response matches the model.
		//checking the entire expected json response
		expect(new ModelIsPresentOptional()
			.setActual(jaoSample)
			.setExpected(jaoFullModel));

		//using a Conditional builder, this is not covered by tutorials, at least not yet.
		//it allows you to define a base model for the expected json and then check that the response matches the model.
		//checking just the expected json object(s) in the json array in the response
		expect(new ModelIsPresentOptional()
			.setActual("list.+")
			.setFrom(jaoSample)
			.setExpected(jaoPartialModel));
	}

	String responseWithC = "{\n" +
		"    \"data\": [\n" +
		"        {\n" +
		"            \"id\": \"8ac2a3d05d37576c015d3c4b3135000e\",\n" +
		"            \"b\": true" +
		"        },\n" +
		"        {\n" +
		"            \"id\": \"8ac2a3d05d37576c015d3c4b3135000f\",\n" +
		"            \"c\": {\"z\": 3.1459}\n" +
		"        }\n" +
		"    ],\n" +
		"    \"page\": null\n" +
		"}";

	@Test(groups = {"unit"})
	public void conditionalModelIsPresentCIsPresent() {
		expect(new ModelIsPresentOptional()
			.addOptionalField(path().match("data").dot().anyNumberZeroOrMore().dot().match("b"))
			.addOptionalField(path().match("data").dot().anyNumberZeroOrMore().dot().match("c"))
			.setActual(responseWithC)
			.setExpected(fullModel));
	}

	String responseShouldFailOnMissingZ = "{\n" +
		"    \"data\": [\n" +
		"        {\n" +
		"            \"id\": \"8ac2a3d05d37576c015d3c4b3135000e\",\n" +
		"            \"b\": true," +
		"            \"c\": {}" +
		"        },\n" +
		"        {\n" +
		"            \"id\": \"8ac2a3d05d37576c015d3c4b3135000f\"\n" +
		"        }\n" +
		"    ],\n" +
		"    \"page\": null\n" +
		"}";

	@Test(groups = {"unit"})
	public void conditionalModelIsPresentZFail() {
		expect(new ModelIsPresentOptional()
			.addOptionalField(path().match("data").dot().anyNumberZeroOrMore().dot().match("b"))
			.addOptionalField(path().match("data").dot().anyNumberZeroOrMore().dot().match("c"))
			.setActual(responseShouldFailOnMissingZ)
			.setExpected(fullModel));
		boolean failed = true;
		try {
			evaluate();
			failed = false;
		} catch (Throwable t) {
			LOG.debug("Test", "Failed as expected.");
		}
		assertTrue(failed);
	}

	String responseShouldFailOnMissingId = "{\n" +
		"    \"data\": [\n" +
		"        {\n" +
		"            \"id\": \"8ac2a3d05d37576c015d3c4b3135000e\",\n" +
		"            \"b\": true" +
		"        },\n" +
		"        {\n" +
		"            \"b\": \"8ac2a3d05d37576c015d3c4b3135000f\"\n" +
		"        }\n" +
		"    ],\n" +
		"    \"page\": null\n" +
		"}";

	@Test(groups = {"unit"})
	public void conditionalModelIsPresentRequiredFieldMissing() {
		expect(new ModelIsPresentOptional()
			.addOptionalField(path().match("data").dot().anyNumberZeroOrMore().dot().match("b"))
			.addOptionalField(path().match("data").dot().anyNumberZeroOrMore().dot().match("c"))
			.setActual(responseShouldFailOnMissingId)
			.setExpected(fullModel));
		boolean failed = true;
		try {
			muteFrom = true;
			evaluate();
			failed = false;
		} catch (Throwable t) {
			LOG.debug("Test", "Failed as expected.");
		}
		assertTrue(failed);
	}

	@Test(groups = {"unit"})
	public void NotNullToNull() {
		expect(Expectation.build()
			.actual("3229.55")
			.isEqualTo(null));
		boolean failed = true;
		try {
			evaluate();
			failed = false;
		} catch (Throwable t) {
			LOG.debug("Test", "Failed as expected.");
		}
		assertTrue(failed);
	}

	@Test(groups = {"unit"})
	public void evalSkipAndFail() {
		boolean skipped = true;
		try {
			expect(Expectation.build()
				.actual("tracy.*.athan")
				.from(sample)
				.isEqualTo(42));
			expect(Expectation.build()
				.actual("data.+.c")
				.from(sample)
				.isEqualTo(42));
			expect(Expectation.build()
				.actual("tracy")
				.from(sample)
				.isEqualTo(42));
			evaluate(true);
			skipped = false;
		} catch (Throwable shouldBeThrown) {
			LOG.info("Skip And Fail", "Success!!!");
		}
		assertEquals(ev.fails().size() + ev.skipped().size(), 2);
		assertTrue(skipped, "Should have failed and thrown an error.");
	}

	@Test(groups = {"unit"})
	public void evalSkipAndPass() {
		boolean skipped = false;
		try {
			expect(Expectation.build()
				.actual("tracy.*.athan")
				.from(sample)
				.isEqualTo(42));
			evaluate();
			skipped = true;
		} catch (Throwable shouldBeThrown) {
			LOG.info("Skip And Fail", "FAIL!!!");
		}
		assertTrue(skipped, "Should have passed and not thrown an error.");
	}

	@Test(groups = {"unit"})
	public void evalNoSkipZeroOrMoreAndPass() {
		boolean skipped = false;
		try {
			expect(Expectation.build()
				.actual("data.*.c")
				.from(sample)
				.isEqualTo(42));
			evaluate();
			skipped = true;
		} catch (Throwable shouldBeThrown) {
			LOG.info("No Skip And Fail", "FAIL!!!");
		}
		assertTrue(skipped, "Should have passed and not thrown an error.");
	}

	@Test(groups = {"unit"})
	public void evalNoSkipOneOrMoreAndPass() {
		boolean skipped = false;
		try {
			expect(Expectation.build()
				.actual("data.+.a")
				.from(sample)
				.isEqualTo(null));
			evaluate();
			skipped = true;
		} catch (Throwable shouldBeThrown) {
			LOG.info("No Skip And Fail", "FAIL!!!");
		}
		assertTrue(skipped, "Should have passed and not thrown an error.");
	}

	@Test(groups = {"unit"})
	public void oneOrMoreButActuallyEmpty() {
		boolean skipped = true;
		try {
			expect(Expectation.build()
				.actual("empty.+.field")
				.from(sample)
				.isEqualTo("cheezeburger"));
			evaluate();
			skipped = false;
		} catch (Throwable shouldBeThrown) {
			LOG.info("One Or More Wildcard Index", "Failed, like it should have.");
		}
		assertTrue(skipped, "Should have failed and thrown an error.");
	}

	@Test(groups = {"unit"})
	public void zeroOrMoreThenOneOrMoreButActuallyEmpty() {
		boolean skipped = false;
		try {
			expect(Expectation.build()
				.actual("empty.*.+.field")
				.from(sample)
				.isEqualTo("cheezeburger"));
			evaluate();
			skipped = true;
		} catch (Throwable shouldBeThrown) {
			LOG.info("One Or More Wildcard Index", "Failed, but it shouldn't have.");
		}
		assertTrue(skipped, "Should have passed.");
	}

	@Test(groups = {"unit"})
	public void checkForOneOrMoreIfAnyPresent() {
		boolean skipped = true;
		try {
			expect(Expectation.build()
				.actual("there_but_missing.*.+.field")
				.from(sample)
				.isEqualTo("cheezeburger"));
			evaluate();
			skipped = false;
		} catch (Throwable shouldBeThrown) {
			LOG.info("One Or More Wildcard Index", "Failed, like it should have.");
		}
		assertTrue(skipped, "Should have failed and thrown an error.");
	}

	@Test(groups = {"unit"})
	public void checkForOneOrMoreNestedNotPresent() {
		boolean skipped = true;
		try {
			expect(Expectation.build()
				.actual("there_but_empty2.+.+.field")
				.from(sample)
				.isEqualTo("cheezeburger"));
			evaluate();
			skipped = false;
		} catch (Throwable shouldBeThrown) {
			LOG.info("One Or More Wildcard Index", "Failed, like it should have.");
		}
		assertTrue(skipped, "Should have failed and thrown an error.");
	}

	@Test(groups = {"unit"})
	public void checkForOneOrMoreNestedIsPresent() {
		boolean skipped = false;
		try {
			expect(Expectation.build()
				.actual("there_and_filled.+.+.field")
				.from(sample)
				.isEqualTo("cheezeburger"));
			evaluate();
			skipped = true;
		} catch (Throwable shouldBeThrown) {
			LOG.info("One Or More Wildcard Index", "Failed, but it shouldn't have.");
			logStatuses(ev);
		}
		assertTrue(skipped, "Should have passed.");
	}

	@Test(groups = {"unit"})
	public void evalMatchIndexNotFound() {
		boolean skipped = false;
		try {
			String val = "" + new Get("mi.0.0.scooby").from(sample);
			expect(Expectation.build()
				.actual("mi.*.*.scooby")
				.from(sample)
				.isEqualTo("mi.%.\\%.scooby")
				.fromExpected(sample));
			evaluate();
			skipped = true;
		} catch (Throwable shouldBeThrown) {
			LOG.info("Match Index", "FAIL!!!");
		}
		assertTrue(skipped, "Should have passed and not thrown an error.");
	}

	@Test(groups = {"unit"})
	public void wildCardInExpectedValue() {
		String value = "hello%world";
		expect(Expectation.build()
			.actual(value)
			.isEqualTo(value));
	}

	@Test(groups = {"unit"})
	public void wildcardListIndex() {
		List<Goate> list = new ArrayList<>();
		Goate one = new Goate().put("a", true);
		Goate two = new Goate().put("a", true);
		list.add(one);
		list.add(two);
		//        list.stream().forEach(goate ->
		//        Goate gl = new ToGoate(list).convert();
		expect(Expectation.build()
			.actual("*.a")
			.from(list)
			.isEqualTo(true));
	}

	@Test(groups = {"unit"})
	public void wildcardListIndexExpected() {
		List<Goate> list = new ArrayList<>();
		Goate one = new Goate().put("a", true);
		Goate two = new Goate().put("a", true);
		list.add(one);
		list.add(two);
		//        list.stream().forEach(goate ->
		//        Goate gl = new ToGoate(list).convert();
		expect(Expectation.build()
			.actual("*.a")
			.from(list)
			.isEqualTo("%.a")
			.fromExpected(list));
	}

	@Test(groups = {"unit"})
	public void wildcardPercentCharacterInValue() {
		//        list.stream().forEach(goate ->
		//        Goate gl = new ToGoate(list).convert();
		expect(Expectation.build()
			.actual("%.a")
			.isEqualTo("%.a"));
	}

	@Test(groups = {"unit"})
	public void wildcardZeroOrMoreCharacterInValue() {
		//        list.stream().forEach(goate ->
		//        Goate gl = new ToGoate(list).convert();
		expect(Expectation.build()
			.actual("*.a")
			.isEqualTo("*.a"));
	}

	@Test(groups = {"unit"})
	public void wildcardOneOrMoreCharacterInValue() {
		//        list.stream().forEach(goate ->
		//        Goate gl = new ToGoate(list).convert();
		expect(Expectation.build()
			.actual("+.a")
			.isEqualTo("+.a"));
	}

	@Test(groups = {"unit"})
	public void wildcardNestedListIndex() {
		List<List> list = new ArrayList<>();
		Goate one = new Goate().put("a", true);
		Goate two = new Goate().put("a", true);
		List<Goate> oneL = new ArrayList<>();
		oneL.add(one);
		oneL.add(two);
		List<Goate> twoL = new ArrayList<>();
		twoL.add(one);
		list.add(oneL);
		list.add(twoL);
		//        list.stream().forEach(goate ->
		//        Goate gl = new ToGoate(list).convert();
		expect(Expectation.build()
			.actual("*.*.a")
			.from(list)
			.isEqualTo(true));
	}

	@Test(groups = {"unit"})
	public void wildcardNestedNestedListIndex() {
		List<List> list = new ArrayList<>();
		List<List> l1 = new ArrayList<>();
		List<List> l2 = new ArrayList<>();
		Goate one = new Goate().put("a", true);
		Goate two = new Goate().put("a", true);
		List<Goate> oneL = new ArrayList<>();
		oneL.add(one);
		oneL.add(two);
		List<Goate> twoL = new ArrayList<>();
		twoL.add(one);
		l1.add(oneL);
		l1.add(twoL);
		l2.add(oneL);
		list.add(l1);
		list.add(l2);
		//        list.stream().forEach(goate ->
		//        Goate gl = new ToGoate(list).convert();
		expect(Expectation.build()
			.actual("*.*.*.a")
			.from(list)
			.isEqualTo(true));
	}

	@Test(groups = {"unit"})
	public void wildcardOneOrMoreNestedNestedListIndex() {
		List<List> list = new ArrayList<>();
		List<List> l1 = new ArrayList<>();
		List<List> l2 = new ArrayList<>();
		Goate one = new Goate().put("a", true);
		Goate two = new Goate().put("a", true);
		List<Goate> oneL = new ArrayList<>();
		oneL.add(one);
		oneL.add(two);
		List<Goate> twoL = new ArrayList<>();
		twoL.add(one);
		l1.add(oneL);
		l1.add(twoL);
		l2.add(oneL);
		list.add(l1);
		list.add(l2);
		//        list.stream().forEach(goate ->
		//        Goate gl = new ToGoate(list).convert();
		expect(Expectation.build()
			.actual("+.+.+.a")
			.from(list)
			.isEqualTo(true));
	}

	@Test(groups = {"unit"})
	public void wildcardExpectFailureListIndex() {
		List<List> list = new ArrayList<>();
		List<List> l1 = new ArrayList<>();
		List<List> l2 = new ArrayList<>();
		Goate one = new Goate().put("a", true);
		Goate two = new Goate().put("b", true);
		List<Goate> oneL = new ArrayList<>();
		oneL.add(one);
		oneL.add(two);
		List<Goate> twoL = new ArrayList<>();
		twoL.add(one);
		l1.add(oneL);
		l1.add(twoL);
		l2.add(oneL);
		list.add(l1);
		list.add(l2);
		//        list.stream().forEach(goate ->
		//        Goate gl = new ToGoate(list).convert();
		expect(Expectation.build()
			.actual("+.+.+.a")
			.from(list)
			.isEqualTo(true));
		boolean fail = true;
		try {
			evaluate();
			fail = false;
		} catch (Throwable t) {
			LOG.info("nested more than one expected failed as expected.");
		}
		assertTrue(fail);
	}

	@Test(groups = {"unit"})
	public void jsonFail() {
		String a = "{" +
			"\"a\":42" +
			"}";
		String b = "{" +
			"\"a\":43" +
			"}";
		//        list.stream().forEach(goate ->
		//        Goate gl = new ToGoate(list).convert();
		expect(Expectation.build()
			.actual(a)
			.isEqualTo(b));
		boolean fail = true;
		try {
			evaluate();
			fail = false;
		} catch (Throwable t) {
			LOG.info("nested more than one expected failed as expected.");
		}
		assertTrue(fail);
	}

	@Test(groups = {"unit"})
	public void listToGoate() {
		List<String> fred = new ArrayList<>();
		fred.add("rogers");
		fred.add("astaire");
		fred.add("kruger");
		//        list.stream().forEach(goate ->
		//        Goate gl = new ToGoate(list).convert();
		Goate fredGoate = new ToGoate(fred).convert();
		LOG.info("Goate Conversion", fredGoate.toString());
		expect(Expectation.build()
			.actual("+")
			.from(fred)
			.isEmpty(false));
	}

	@Test(groups = {"unit"})
	public void fromExpectedIndex() throws Exception {
		String body = "{\"content\":[{\"id\":4},{\"id\":5},{\"id\":6}]}";
		int size = Integer.parseInt("" + new Get("content>size()").from(body));
		Object last = new Get("content." + (size - 1)).from(body);
		String nextBody = "" + new Fill(body).with(new Goate().put("content.0", "drop field::"));
		nextBody = "" + new InsertJson("content", last)
			.in("content")
			.into(nextBody)
			.insert();
		expect(Expectation.build()
			.actual("content.+.id")
			.from(body)
			.isLessThanOrEqualTo("content.%.id")
			.fromExpected(nextBody));
	}

	@Test(groups = {"unit"})
	public void fromExpectedIndexLoop() {
		String body = "{\"content\":[{\"id\":4},{\"id\":5},{\"id\":6}]}";
		int size = Integer.parseInt("" + new Get("content>size()").from(body));
		for (int i = 0; i < size - 1; i++) {
			expect(Expectation.build()
				.actual("content." + i + ".id")
				.from(body)
				.isLessThanOrEqualTo("content." + (i + 1) + ".id")
				.fromExpected(body));
		}
	}

	@Test(groups = {"unit"})
	public void isEqualObjectsWithHealthCheck() {
		Goate data = new Goate();
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual(new SimpleObject().setA("h").setB(true).addC("a").addC("b"))
			.isEqualTo(new SimpleObject().setA("h").setB(true).addC("a").addC("b")));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		boolean result = ev.evaluate();
		logStatuses(ev);
		assertTrue(result);
	}

	@Test(groups = {"unit"})
	public void isEqualObjectsWithHealthCheckFail() {
		Goate data = new Goate();
		Map<String, Object> map1 = new HashMap<>();
		map1.put("fred", new Goate().put("field1", true));
		Map<String, Object> map2 = new HashMap<>();
		map2.put("fred", new Goate().put("field1", false));
		List<SimpleObject2> list1 = new ArrayList<>();
		list1.add(new SimpleObject2().setBd(new BigDecimal(3)));
		List<SimpleObject2> list2 = new ArrayList<>();
		list2.add(new SimpleObject2().setBd(new BigDecimal(3)));
		ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
		etb.expect(Expectation.build()
			.actual(new SimpleObject().setSo2(list1).setA("h").setB(true).addC("a").addC("b").setNested(new NestedObject().setNo("hello").setMap(map1)))
			.isEqualTo(new SimpleObject().setSo2(list2).setA("h").setB(false).addC("c").addC("b").setNested(new NestedObject().setNo("world").setMap(map2))));
		ExpectEvaluator ev = new ExpectEvaluator(etb);
		//        assertEquals(42, 42, "these were not equal");
		boolean result = ev.evaluate();
		logStatuses(ev, true);
		assertFalse(result);
	}

	@Test(groups = {"unit"})
	public void isInString() {
		String act = "hello World!!!";
		String exp = "llo Wo";
		expect(Expectation.build()
			.actual(exp)
			.isIn(act));
	}

	@Test(groups = {"unit"})
	public void isIn() {
		String json = "[" +
			"{'id':'1st'}," +
			"{'id':'4th'}," +
			"{'id':'1st'}," +
			"{'id':'2nd'}," +
			"{'id':'3rd'}" +
			"]";
		List<String> expected = new ArrayList<>();
		expected.add("1st");
		expected.add("2nd");
		expected.add("3rd");
		expected.add("4th");

		expect(Expectation.build()
			.actual("+.id")
			.from(json)
			.isIn(expected));
	}

	@Test(groups = {"unit"})
	public void containsListInList() {
		String json = "'content':[" +
			"{'id':'1st'}," +
			"{'id':'4th'}," +
			"{'id':'1st'}," +
			"{'id':'2nd'}," +
			"{'id':'3rd'}" +
			"{'id':'3rd'}" +
			"]";
		List<String> expected = new ArrayList<>();
		expected.add("1st");
		expected.add("2nd");
		expected.add("3rd");
		expected.add("4th");

		expect(new ContainsExpectationBuilder()
			.lookFor(new Value("+.id").in(json))
			.lookIn(new Value(expected)));
	}

	@Test(groups = {"unit"})
	public void compareUnsorted() {
		String api1 = "{" +
			"\"content\":[" +
			"{\"type\":\"foo\", \"some other key\": false}," +
			"{\"type\":\"bar\", \"some other key\": false}," +
			//                "{\"type\":\"noo\", \"some other key\": false}," +
			"{\"type\":\"roo\", \"some other key\": true}" +
			"]" +
			"}";
		String api2 = "{" +
			"\"content\":[" +
			"{\"udfFieldType\":{\"code\":\"bar\"}}," +
			"{\"udfFieldType\":{\"code\":\"roo\"}}," +
			"{\"udfFieldType\":{\"code\":\"foo\"}}" +
			"]" +
			"}";
		ContentPojoT<TypePojo> content = new DeSerializer().data(new ToGoate(api1).convert()).T(TypePojo.class).build(ContentPojoT.class);
		String api1FormattedPT = "" + new Serializer<>(new DeSerializer().data(new ToGoate(api1).convert()).T(TypePojo.class).build(ContentPojoT.class), DefaultSource.class).to(new JsonString());
		String api1FormattedP = "" + new Serializer<>(new DeSerializer().data(new ToGoate(api1).convert()).build(ContentPojo.class), DefaultSource.class).to(new JsonString());
		String api2FormattedP = "" + new Serializer<>(new DeSerializer().data(new ToGoate(api2).convert()).from(UDFSource.class).build(ContentPojo.class), DefaultSource.class).to(new JsonString());
		expect(Expectation.build()
			.actual("content")
			.from(api1FormattedP)
			.isEqualTo("content")
			.fromExpected(api2FormattedP));
		expect(Expectation.build()
			.actual(api1FormattedPT)
			.isEqualTo(api2FormattedP));
	}

	@Test(groups = {"unit"})
	public void unsortedList() {
		ListPojo a = new ListPojo();
		a.getTheList().add("a");
		a.getTheList().add("b");
		a.getTheList().add("c");
		a.getTheList().add("d");

		ListPojo b = new ListPojo();
		b.getTheList().add("c");
		b.getTheList().add("a");
		b.getTheList().add("d");
		b.getTheList().add("b");

		expect(Expectation.build()
			.actual(a).isEqualToIgnoreOrder(b));
	}

	@Test(groups = {"unit"})
	public void unsortedListDoubleNested() {
		ListPojo a = new ListPojo();
		List<String> io1i1 = new ArrayList<>();
		io1i1.add("hello");
		List<String> io1i2 = new ArrayList<>();
		io1i1.add("world");
		List<InsideObject> io1 = new ArrayList<>();
		io1.add(new InsideObject().setTheList(io1i1));
		io1.add(new InsideObject().setTheList(io1i2));
		List<String> io2i1 = new ArrayList<>();
		io1i1.add("hello");
		List<String> io2i2 = new ArrayList<>();
		io1i1.add("world");
		List<InsideObject> io2 = new ArrayList<>();
		io2.add(new InsideObject().setTheList(io2i2));
		io2.add(new InsideObject().setTheList(io2i1));
		a.getTheInsideList().add(new NestedObject().setAnotherList(io1));
		a.getTheInsideList().add(new NestedObject().setAnotherList(io2));

		ListPojo b = new ListPojo();
		List<String> bio1i1 = new ArrayList<>();
		bio1i1.add("hello");
		List<String> bio1i2 = new ArrayList<>();
		bio1i1.add("world");
		List<InsideObject> bio1 = new ArrayList<>();
		bio1.add(new InsideObject().setTheList(bio1i1));
		bio1.add(new InsideObject().setTheList(bio1i2));
		List<String> bio2i1 = new ArrayList<>();
		bio1i1.add("hello");
		List<String> bio2i2 = new ArrayList<>();
		bio1i1.add("world");
		List<InsideObject> bio2 = new ArrayList<>();
		bio2.add(new InsideObject().setTheList(bio2i1));
		bio2.add(new InsideObject().setTheList(bio2i2));
		b.getTheInsideList().add(new NestedObject().setAnotherList(bio2));
		b.getTheInsideList().add(new NestedObject().setAnotherList(bio1));

		expect(Expectation.build()
			.actual(a).isEqualToIgnoreOrder(b));
	}

	@ExpectToFail
	@Test(groups = {"unit"})
	public void unsortedListDoubleNestedEqualToFail() {
		ListPojo a = new ListPojo();
		List<String> io1i1 = new ArrayList<>();
		io1i1.add("hello");
		List<String> io1i2 = new ArrayList<>();
		io1i1.add("world");
		List<InsideObject> io1 = new ArrayList<>();
		io1.add(new InsideObject().setTheList(io1i1));
		io1.add(new InsideObject().setTheList(io1i2));
		List<String> io2i1 = new ArrayList<>();
		io1i1.add("hello");
		List<String> io2i2 = new ArrayList<>();
		io1i1.add("world");
		List<InsideObject> io2 = new ArrayList<>();
		io2.add(new InsideObject().setTheList(io2i2));
		io2.add(new InsideObject().setTheList(io2i1));
		a.getTheInsideList().add(new NestedObject().setAnotherList(io1));
		a.getTheInsideList().add(new NestedObject().setAnotherList(io2));

		ListPojo b = new ListPojo();
		List<String> bio1i1 = new ArrayList<>();
		bio1i1.add("hello");
		List<String> bio1i2 = new ArrayList<>();
		bio1i1.add("world");
		List<InsideObject> bio1 = new ArrayList<>();
		bio1.add(new InsideObject().setTheList(bio1i1));
		bio1.add(new InsideObject().setTheList(bio1i2));
		List<String> bio2i1 = new ArrayList<>();
		bio1i1.add("hello");
		List<String> bio2i2 = new ArrayList<>();
		bio1i1.add("world");
		List<InsideObject> bio2 = new ArrayList<>();
		bio2.add(new InsideObject().setTheList(bio2i1));
		bio2.add(new InsideObject().setTheList(bio2i2));
		b.getTheInsideList().add(new NestedObject().setAnotherList(bio2));
		b.getTheInsideList().add(new NestedObject().setAnotherList(bio1));

		expect(Expectation.build()
			.actual(a).isEqualTo(b));
	}

	@Test(groups = {"unit"})
	public void genericTypeInPojo() {
		String j = "{\"field\":\"some string value\", \"field2\":42}";

		SimplePojoT<String, BigDecimal> pojoT = new DeSerializer().data(new ToGoate(j).convert()).T(String.class).T(BigDecimal.class).build(SimplePojoT.class);

		expect(Expectation.build()
			.actual(pojoT.getField())
			.isEqualTo("some string value"));
		expect(Expectation.build()
			.actual(pojoT.getField2())
			.isEqualTo(new BigDecimal(42)));
	}

	@Test(groups = {"unit"})
	public void isNotEqualToNull() {
		expect(Expectation.build()
			.actual("hello, world!")
			.isNotEqualTo(null));
	}

	@Test(groups = {"unit"})
	public void listContains() {
		List<String> headers = new ArrayList<>();
		headers.add("A");
		headers.add("B");
		headers.add("C");
		headers.add("D");
		headers.add("E");
		headers.add("F");
		String expectedString = "c";
		expect(new ContainsExpectationBuilder()
			.lookFor(new Value(expectedString.toUpperCase()))
			.lookIn(new Value(headers)));
	}

	@Test(groups = {"unit"})
	public void goateValueTrue() {
		String message = "has value";
		expect(Expectation.build()
			.actual(message)
			.from(new Goate().put(message, true))
			.isEqualTo(true));
		expect(Expectation.build()
			.actual(message)
			.from(new Goate().put(message, true))
			.isEqualTo(true));
	}

	@Test(groups = {"unit"})
	public void skippedThread() {
		List<SkipThread> threads = new ArrayList<>();
		threads.add(new SkipThread());
		expect(Expectation.build()
			.actual(new Executioner<SkipThread>().process(threads))
			.isEqualTo(false)
			.failureMessage("the number of threads to be process and the number of threads processed should not be equal, but they were."));
	}

	@GoateDLP(name = "gw")
	public Goate[] gw() {
		Goate[] runs = new Goate[2];
		runs[0] = new Goate();
		for (int i = 0; i < 2; i++) {
			runs[0].put("run ##", new StaticDL().add("Scenario", "run: " + i));
		}
		return runs;
	}

	@GoateProvider(name = "gw")
	@Test(groups = {"unit"}, dataProvider = "methodLoader")
	public void stringToDecimalComparisonUsingGoateWrapper(Goate testData) {
		Object expected = new Get("val").from("{\"val\":98800.0}");
		expectValue("has button", true, true);
		expectValue("button 1", "98,800.00", expected);
		expectValue("button 2", "98,800.00", expected);
	}

	private void expectValue(String message, Object actual, Object expected) {
		expect(Expectation.build()
			.actual(message)
			.from(new Goate().put(message, actual))
			.isEqualTo(expected));
	}

	@Test(groups = {"unit"})
	public void expectWildcardIndex() {
		String json = "[" +
			"{'id':'1st'}," +
			"{'id':'4th'}," +
			"{'id':'1st'}," +
			"{'id':'2nd'}," +
			"{'id':'3rd'}" +
			"]";
		List<String> expected = new ArrayList<>();
		expected.add("1st");
		expected.add("4th");
		expected.add("1st");
		expected.add("2nd");
		expected.add("3rd");

		expect(Expectation.build()
			.actual("+.id")
			.from(json)
			.isEqualTo("%")
			.fromExpected(expected));
	}

	@Test(groups = {"unit"})
	public void listContainsList() {
		List<String> expected = new ArrayList<>();
		expected.add("1st");
		expected.add("4th");
		expected.add("1st");
		expected.add("2nd");
		expected.add("3rd");

		List<String> actual = new ArrayList<>();
		actual.add("3rd");
		actual.add("2nd");
		expect(new ContainsExpectationBuilder()
			.lookFor(new Value("+").in(actual))
			.lookIn(expected));
	}

	@Test(groups = {"unit"})
	public void listContainsArray() {
		List<String> expected = new ArrayList<>();
		expected.add("1st");
		expected.add("4th");
		expected.add("1st");
		expected.add("2nd");
		expected.add("3rd");

		String[] actual = new String[2];
		actual[0] = "3rd";
		actual[1] = "2nd";
		expect(new ContainsExpectationBuilder()
			.lookFor(new Value("+").in(Arrays.asList(actual)))
			.lookIn(expected));
	}

	@Test(groups = {"unit"})
	public void expectWildcardIndexUsingValue() {
		String json = "[" +
			"{'id':'1st'}," +
			"{'id':'4th'}," +
			"{'id':'1st'}," +
			"{'id':'2nd'}," +
			"{'id':'3rd'}" +
			"]";
		List<String> expected = new ArrayList<>();
		expected.add("1st");
		expected.add("4th");
		expected.add("1st");
		expected.add("2nd");
		expected.add("3rd");

		expect(Expectation.build()
			.actual(new Value("+.id").in(json))
			.isEqualTo(new Value("%").in(expected)));
	}

	@Test(groups = {"unit"})
	public void containsWildCardActual() {
		String json = "[" +
			"{'id':'hello world'}," +
			"{'id':'hello world'}," +
			"{'id':'ho wo'}," +
			"{'id':'halo wants'}," +
			"{'id':'hello world!'}" +
			"]";

		expect(new ContainsExpectationBuilder()
			.lookFor("o w")
			.lookIn(new Value("+.id").in(json)));
	}

	@Test(groups = {"unit"})
	public void containsBuilderString() {
		expect(new ContainsExpectationBuilder()
			.lookFor(new Value("o w"))
			.lookIn(new Value("Hello world!!!")));
	}

	@Test(groups = {"unit"})
	public void containsString() {
		expect(Expectation.build()
			.actual("o w")
			.isIn("Hello world!!!"));
	}

	@Test(groups = {"unit"})
	public void nullToJsonNull() {
		expect(Expectation.build().actual(null)
			.isEqualTo("a")
			.fromExpected("{\"a\":null}"));
	}

	@Test(groups = {"unit"})
	public void skippedTestsFail() {
		expect(Expectation.build().actual("b")
			.isEqualTo("a")
			.from("{\"a\":null}"));
		boolean failed = true;
		//        try {
		//            evaluate();
		//            failed = false;
		//        } catch (Throwable t) {
		//            LOG.info("I threw the error I was expecting.");
		//        }
		expect(Expectation.build().actual(false).isEqualTo(true));
		try {
			evaluate(true);
			failed = false;
		} catch (Throwable t) {
			LOG.info("nuthin");
		}
		expect(Expectation.build().actual(failed).isEqualTo(true));
		expect(Expectation.build().actual("b.*.a")
			.isEqualTo("a")
			.from("{\"a\":null}"));
	}

	@Test(groups = {"unit"})
	public void skippedTestsPassZeroOrMore() {
		expect(Expectation.build().actual("b.*.a")
			.isEqualTo("a")
			.from("{\"a\":null}"));
		boolean failed = false;
		try {
			evaluate();
			failed = true;
		} catch (Throwable t) {
			LOG.info("I threw the error I was NOT expecting.");
		}
		expect(Expectation.build().actual(failed).isEqualTo(true));
	}

	@Test(groups = {"unit"})
	public void doSkipTestOnPurpose() {
		expect(Expectation.build().actual("noop").from(new SkipExpectation()).isNull(false));
		boolean skipped = false;
		try {
			evaluate();
			LOG.debug("Skip Test", "If the test passed, I shouldn't see this message in the logs.");
		} catch (Throwable t) {
			skipped = true;
		}
		expect(Expectation.build()
			.actual(skipped)
			.isEqualTo(true));
	}

	@Test(groups = {"unit"})
	public void jsonListWithIntToDecimal() {
		String j1 = "{\"a\":[{\"b\":42.00}, {\"b\":314}]}";
		String j2 = "{\"a\":[{\"b\":314.00}, {\"b\":42}]}";
		expect(Expectation.build()
			.actual(j1)
			.isEqualTo(j2));
	}

	@Test
	public void errorLog() {
		LOG.error("error", new Exception("something witty and inspirational."));
	}
}
