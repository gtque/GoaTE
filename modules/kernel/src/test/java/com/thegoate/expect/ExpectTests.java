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

import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.thegoate.dsl.words.LoadFile.fileAsAString;
import static com.thegoate.expect.ExpectMatchWildcardIndexPath.matchWildcardIndex;
import static com.thegoate.expect.ExpectWildcardIndexPath.wildcardIndex;
import static org.testng.Assert.*;

/**
 * Test expect framework.
 * Created by Eric Angeli on 5/10/2017.
 */
public class ExpectTests {
    final BleatBox LOG = BleatFactory.getLogger(getClass());

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
        assertTrue(result, ev.failed());
    }

    @Test(groups = {"unit"})
    public void isEvenButReallyOdd() {
        Goate data = new Goate();
        data.put("check if even.value", 43);
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect("check if even>return,==,boolean::true");
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        assertFalse(result, ev.failed());
        LOG.debug("failed message:\n" + ev.failed());
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
        for(Goate p:ev.passes()){
            ps.append(p.toString());
            ps.append("\n--------------------\n");
        }
        LOG.debug("passed:\n" + ps.toString());
        StringBuilder fs = new StringBuilder();
        for(Goate p:ev.fails()){
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
        Expectation e = new Expectation(new Goate());
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
}
