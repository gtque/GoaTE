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

package com.thegoate.utils.compare;

import com.thegoate.Goate;
import com.thegoate.data.GoateDLP;
import com.thegoate.data.GoateProvider;
import com.thegoate.data.StaticDL;
import com.thegoate.expect.Expectation;
import com.thegoate.testng.ExpectToFail;
import com.thegoate.testng.TestNGEngineMethodDL;
import com.thegoate.utils.compare.tools.integer.*;
import com.thegoate.utils.compare.tools.l.CompareLongEqualTo;
import com.thegoate.utils.compare.tools.l.CompareLongGreaterThanEqualTo;
import com.thegoate.utils.get.Get;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static com.thegoate.utils.type.GoateNullCheck.isNull;
import static org.testng.Assert.*;

/**
 * Tests compare utilities.
 * Created by Eric Angeli on 5/9/2017.
 */
public class CompareTests extends TestNGEngineMethodDL {

    public CompareTests() {
        super();
    }


    @Test(groups = {"unit"})
    public void intEqualTo() {
        assertTrue(new CompareIntEqualTo(42).to(42).using("==").evaluate());
    }

    @Test(groups = {"unit"})
    public void intEqualToFail() {
        assertFalse(new CompareIntEqualTo(43).to(42).using("==").evaluate());
    }

    @Test(groups = {"unit"})
    public void intNotEqualTo() {
        assertTrue(new CompareIntNotEqualTo(42).to(43).using("!=").evaluate());
    }

    @Test(groups = {"unit"})
    public void intNotEqualToFail() {
        assertFalse(new CompareIntNotEqualTo(42).to(42).using("!=").evaluate());
    }

    @Test(groups = {"unit"})
    public void intLessEqualTo() {
        assertTrue(new CompareIntLessThanEqualTo(42).to(42).using("<=").evaluate());
    }

    @Test(groups = {"unit"})
    public void intLessEqualToLess() {
        assertTrue(new CompareIntLessThanEqualTo(41).to(42).using("<=").evaluate());
    }

    @Test(groups = {"unit"})
    public void intLessEqualToFail() {
        assertFalse(new CompareIntLessThanEqualTo(42).to(41).using("<=").evaluate());
    }

    @Test(groups = {"unit"})
    public void intGreaterEqualTo() {
        assertTrue(new CompareIntGreaterThanEqualTo(42).to(42).using(">=").evaluate());
    }

    @Test(groups = {"unit"})
    public void intGreaterEqualToGreater() {
        assertTrue(new CompareIntGreaterThanEqualTo(43).to(42).using(">=").evaluate());
    }

    @Test(groups = {"unit"})
    public void intGreaterEqualToFail() {
        assertFalse(new CompareIntGreaterThanEqualTo(42).to(43).using(">=").evaluate());
    }

    @Test(groups = {"unit"})
    public void intGreater() {
        assertTrue(new CompareIntGreaterThan(43).to(42).using(">").evaluate());
    }

    @Test(groups = {"unit"})
    public void intGreaterEqualFail() {
        assertFalse(new CompareIntGreaterThan(42).to(42).using(">").evaluate());
    }

    @Test(groups = {"unit"})
    public void intGreaterFail() {
        assertFalse(new CompareIntGreaterThan(42).to(43).using(">").evaluate());
    }

    @Test(groups = {"unit"})
    public void intLesser() {
        assertTrue(new CompareIntLessThan(42).to(43).using("<").evaluate());
    }

    @Test(groups = {"unit"})
    public void intLesserEqualFail() {
        assertFalse(new CompareIntLessThan(42).to(42).using("<").evaluate());
    }

    @Test(groups = {"unit"})
    public void intLesserFail() {
        assertFalse(new CompareIntLessThan(43).to(42).using("<").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatEqualTo() {
        assertTrue(new Compare(42.0).to(42.0).using("==").evaluate());
    }

    @Test(groups = {"unit"})
    public void intFloatEqualTo() {
        assertTrue(new CompareIntEqualTo(42).to(42.0).using("==").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatIntEqualTo() {
        assertTrue(new Compare(42.0).to(42).using("==").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatEqualToFail() {
        assertFalse(new Compare(42.1).to(42.0).using("==").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatNotEqualTo() {
        assertTrue(new Compare(42.1).to(42.0).using("!=").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatNotEqualToFail() {
        assertFalse(new Compare(42.0).to(42.0).using("!=").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatLessEqualTo() {
        assertTrue(new Compare(42.0).to(42.0).using("<=").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatLessEqualToLess() {
        assertTrue(new Compare(42.0).to(42.1).using("<=").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatLessEqualToFail() {
        assertFalse(new Compare(42.0).to(41.0).using("<=").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatGreaterEqualTo() {
        assertTrue(new Compare(42.0).to(42.0).using(">=").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatGreaterEqualToGreater() {
        assertTrue(new Compare(43.0).to(42.0).using(">=").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatGreaterEqualToFail() {
        assertFalse(new Compare(42.0).to(43.0).using(">=").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatGreater() {
        assertTrue(new Compare(43.0).to(42.0).using(">").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatGreaterEqualFail() {
        assertFalse(new Compare(42.0).to(42.0).using(">").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatGreaterFail() {
        assertFalse(new Compare(42.0).to(43.0).using(">").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatLesser() {
        assertTrue(new Compare(42.0).to(43.0).using("<").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatLesserEqualFail() {
        assertFalse(new Compare(42.0).to(42.0).using("<").evaluate());
    }

    @Test(groups = {"unit"})
    public void floatLesserFail() {
        assertFalse(new Compare(43.0).to(42.0).using("<").evaluate());
    }

    @Test(groups = {"unit"})
    public void longEqualTo() {
        assertTrue(new CompareLongEqualTo(42L).to(42L).using("==").evaluate());
    }

    @Test(groups = {"unit"})
    public void longEqualToFail() {
        assertFalse(new Compare(43L).to(42L).using("==").evaluate());
    }

    @Test(groups = {"unit"})
    public void longNotEqualTo() {
        assertTrue(new Compare(42L).to(43L).using("!=").evaluate());
    }

    @Test(groups = {"unit"})
    public void longNotEqualToFail() {
        assertFalse(new Compare(42L).to(42L).using("!=").evaluate());
    }

    @Test(groups = {"unit"})
    public void longLessEqualTo() {
        assertTrue(new Compare(42).to(42L).using("<=").evaluate());
    }

    @Test(groups = {"unit"})
    public void longLessEqualToLess() {
        assertTrue(new Compare(41L).to(42L).using("<=").evaluate());
    }

    @Test(groups = {"unit"})
    public void longLessEqualToFail() {
        assertFalse(new Compare(42L).to(41L).using("<=").evaluate());
    }

    @Test(groups = {"unit"})
    public void longGreaterEqualTo() {
        assertTrue(new CompareLongGreaterThanEqualTo(42L).to(42L).using(">=").evaluate());
    }

    @Test(groups = {"unit"})
    public void longGreaterEqualToGreater() {
        assertTrue(new CompareLongGreaterThanEqualTo(43L).to(42L).using(">=").evaluate());
    }

    @Test(groups = {"unit"})
    public void longGreaterEqualToFail() {
        assertFalse(new Compare(42L).to(43L).using(">=").evaluate());
    }

    @Test(groups = {"unit"})
    public void longGreater() {
        assertTrue(new Compare(43L).to(42L).using(">").evaluate());
    }

    @Test(groups = {"unit"})
    public void longGreaterEqualFail() {
        assertFalse(new Compare(42L).to(42L).using(">").evaluate());
    }

    @Test(groups = {"unit"})
    public void longGreaterFail() {
        assertFalse(new Compare(42L).to(43L).using(">").evaluate());
    }

    @Test(groups = {"unit"})
    public void longLesser() {
        assertTrue(new Compare(42L).to(43L).using("<").evaluate());
    }

    @Test(groups = {"unit"})
    public void longLesserEqualFail() {
        assertFalse(new Compare(42L).to(42L).using("<").evaluate());
    }

    @Test(groups = {"unit"})
    public void longLesserFail() {
        assertFalse(new Compare(43L).to(42L).using("<").evaluate());
    }

    @Test(groups = {"unit"})
    public void booleanEqualTo() {
        assertTrue(new Compare(true).to(true).using("==").evaluate());
    }

    @Test(groups = {"unit"})
    public void booleanEqualToFail() {
        assertFalse(new Compare(false).to(true).using("==").evaluate());
    }

    @Test(groups = {"unit"})
    public void booleanNotEqualTo() {
        assertTrue(new Compare(true).to(false).using("!=").evaluate());
    }

    @Test(groups = {"unit"})
    public void booleanNotEqualToFail() {
        assertFalse(new Compare(false).to(false).using("!=").evaluate());
    }

    @Test(groups = {"unit"})
    public void stringIsEmpty() {
        assertTrue(new Compare("").to(true).using("isEmpty").evaluate(), "The String was apparently not empty.");
    }

    @Test(groups = {"unit"})
    public void objectStringEqualTo() {
        assertTrue(new Compare("hello").to("hello").using("==").evaluate());
    }

    @Test(groups = {"unit"})
    public void dateIsPattern() {
        assertTrue(new Compare("2009-11-24").to("yyyy-MM-dd").using("dateIsPattern").evaluate(), "The date did not match the pattern.");
    }

    @Test(groups = {"unit"})
    public void dateIsPatternDoesNotMatch() {
        assertFalse(new Compare("2009/11/24").to("yyyy/MMM/dd").using("dateIsPattern").evaluate(), "The date matched the pattern.");
    }

    @Test(groups = {"unit"})
    public void isNullTrue() {
        assertTrue(new Compare(null).to(true).using("isNull").evaluate(), "They where not null?");
    }

    @Test(groups = {"unit"})
    public void isNullFalse() {
        assertFalse(new Compare(null).to(false).using("isNull").evaluate(), "They where not null?");
    }

    @Test(groups = {"unit"})
    public void isNullFalseTrue() {
        assertTrue(new Compare("mustard").to(false).using("isNull").evaluate(), "They where not null?");
    }

    @Test(groups = {"unit"})
    public void simpleDateLessThan() {
        assertTrue(new Compare("2017-06-11T04:00:00.000+0000").using("<").to("2017-06-30").evaluate(), "The date was not less than apparently.");
    }

    @GoateDLP(name = "string comparisons")
    public Goate[] sdlp() {
        Goate[] d = new Goate[2];
        d[0] = new Goate()
                .put("dl##", new StaticDL().add("Scenario", "abcd<=abcd is true").add("actual", "actual").add("from", new Goate().put("actual", "abcd")).add("expected", "abcd").add("operator", "<=").add("pass", true))
                .put("dl##", new StaticDL().add("Scenario", "abcd<z is true").add("actual", "actual").add("from", new Goate().put("actual", "abcd")).add("expected", "z").add("operator", "<").add("pass", true))
                .put("dl##", new StaticDL().add("Scenario", "abcd<=abc is false").add("actual", "actual").add("from", new Goate().put("actual", "abcd")).add("expected", "abc").add("operator", "<=").add("pass", false))
                .put("dl##", new StaticDL().add("Scenario", "abcde>=abcd is true").add("actual", "actual").add("from", new Goate().put("actual", "abcde")).add("expected", "abcd").add("operator", ">=").add("pass", true))
                .put("dl##", new StaticDL().add("Scenario", "c>abcd is true").add("actual", "actual").add("from", new Goate().put("actual", "c")).add("expected", "abcd").add("operator", ">").add("pass", true))
                .put("dl##", new StaticDL().add("Scenario", "a>abcd is false").add("actual", "actual").add("from", new Goate().put("actual", "a")).add("expected", "abcd").add("operator", ">").add("pass", false))
                .put("dl##", new StaticDL().add("Scenario", "json null<=abcd is false").add("actual", "actual").add("from", "{\"actual\":null}").add("expected", "abcd").add("operator", "<=").add("pass", false))
                .put("dl##", new StaticDL().add("Scenario", "null<=abcd is false").add("actual", "actual").add("from", new Goate().put("actual", "null::")).add("expected", "abcd").add("operator", "<=").add("pass", false))
                .put("dl##", new StaticDL().add("Scenario", "json null>=abcd is true").add("actual", "actual").add("from", "{\"actual\":null}").add("expected", "abcd").add("operator", ">=").add("pass", true))
                .put("dl##", new StaticDL().add("Scenario", "null>=abcd is true").add("actual", "actual").add("from", new Goate().put("actual", "null::")).add("expected", "abcd").add("operator", ">=").add("pass", true))
                .put("dl##", new StaticDL().add("Scenario", "json null<abcd is false").add("actual", "actual").add("from", "{\"actual\":null}").add("expected", "abcd").add("operator", "<").add("pass", false))
                .put("dl##", new StaticDL().add("Scenario", "null<abcd is false").add("actual", "actual").add("from", new Goate().put("actual", "null::")).add("expected", "abcd").add("operator", "<").add("pass", false))
                .put("dl##", new StaticDL().add("Scenario", "json null>abcd is true").add("actual", "actual").add("from", "{\"actual\":null}").add("expected", "abcd").add("operator", ">").add("pass", true))
                .put("dl##", new StaticDL().add("Scenario", "null>abcd is true").add("actual", "actual").add("from", new Goate().put("actual", "null::")).add("expected", "abcd").add("operator", ">").add("pass", true))
                .put("dl##", new StaticDL().add("Scenario", "json null<=null is false").add("actual", "actual").add("from", "{\"actual\":null}").add("expected", "abcd").add("operator", "<=").add("pass", false))
                .put("dl##", new StaticDL().add("Scenario", "null<=null is true").add("actual", "actual").add("from", new Goate().put("actual", "null::")).add("expected", "null::").add("operator", "<=").add("pass", true))
                .put("dl##", new StaticDL().add("Scenario", "json null>null is false").add("actual", "actual").add("from", "{\"actual\":null}").add("expected", "abcd").add("operator", "<=").add("pass", false))
                .put("dl##", new StaticDL().add("Scenario", "null<null is false").add("actual", "actual").add("from", new Goate().put("actual", "null::")).add("expected", "abcd").add("operator", "<=").add("pass", false))
        ;
        return d;
    }

    @GoateProvider(name = "string comparisons")
    @Test(groups = {"unit"}, dataProvider = "methodLoader")
    public void compareStrings(Goate testdata) {
        LOG.info("comparing strings", "" + new Get(get("actual", "actual", String.class)).from(get("from")) + get("operator", "", String.class) + get("expected"));
        boolean result = false;
        try {
            expect(Expectation.build()
                    .actual(get("actual"))
                    .from(get("from"))
                    .is(get("operator", "==", String.class))
                    .expected(get("expected")));
            evaluate();
            result = true;
        } catch (Throwable t) {
            LOG.debug("exception was thrown, which means the comparison failed.");
        }
        assertEquals(result, (boolean) get("pass", false, Boolean.class));
    }

    @GoateDLP(name = "compare primitives")
    public Goate[] dlp() {
        Goate[] d = new Goate[2];
        d[0] = new Goate()
                .put("dl##", new StaticDL().add("actual", "0").add("operator", "==").add("expected", "0.0").add("Scenario", "string 0 == string 0.0"))
                .put("dl##", new StaticDL().add("actual", "1000").add("operator", "==").add("expected", 1000.00D).add("Scenario", "string int == double"))
                .put("dl##", new StaticDL().add("actual", Short.parseShort("4")).add("operator", "==").add("expected", "4").add("Scenario", "short == string short"))
                .put("dl##", new StaticDL().add("actual", Short.parseShort("4")).add("operator", "!=").add("expected", "5").add("Scenario", "short != string short"))
                .put("dl##", new StaticDL().add("actual", Short.parseShort("4")).add("operator", "<").add("expected", "5").add("Scenario", "short < string short"))
                .put("dl##", new StaticDL().add("actual", Short.parseShort("4")).add("operator", "<=").add("expected", "4").add("Scenario", "short <= string short"))
                .put("dl##", new StaticDL().add("actual", Short.parseShort("4")).add("operator", ">").add("expected", "3").add("Scenario", "short > string short"))
                .put("dl##", new StaticDL().add("actual", Short.parseShort("4")).add("operator", ">=").add("expected", "2").add("Scenario", "short >= string short"))
                .put("dl##", new StaticDL().add("actual", Byte.parseByte("4")).add("operator", "==").add("expected", "4").add("Scenario", "byte == string byte"))
                .put("dl##", new StaticDL().add("actual", Byte.parseByte("4")).add("operator", "!=").add("expected", "5").add("Scenario", "byte != string byte"))
                .put("dl##", new StaticDL().add("actual", Byte.parseByte("4")).add("operator", "<").add("expected", "5").add("Scenario", "byte < string byte"))
                .put("dl##", new StaticDL().add("actual", Byte.parseByte("4")).add("operator", "<=").add("expected", "4").add("Scenario", "byte <= string byte"))
                .put("dl##", new StaticDL().add("actual", Byte.parseByte("4")).add("operator", ">").add("expected", "3").add("Scenario", "byte > string byte"))
                .put("dl##", new StaticDL().add("actual", Byte.parseByte("4")).add("operator", ">=").add("expected", "2").add("Scenario", "byte >= string byte"))
                .put("dl##", new StaticDL().add("actual", 3.14159).add("operator", "==").add("expected", "3.14159").add("Scenario", "double == string double"))
                .put("dl##", new StaticDL().add("actual", Double.parseDouble("3.14159")).add("operator", "!=").add("expected", "5").add("Scenario", "double != string double"))
                .put("dl##", new StaticDL().add("actual", Double.parseDouble("3.14159")).add("operator", "<").add("expected", "5").add("Scenario", "double < string double"))
                .put("dl##", new StaticDL().add("actual", Double.parseDouble("3.14159")).add("operator", "<=").add("expected", "3.14159").add("Scenario", "double <= string double"))
                .put("dl##", new StaticDL().add("actual", Double.parseDouble("3.14159")).add("operator", ">").add("expected", "3").add("Scenario", "double > string double"))
                .put("dl##", new StaticDL().add("actual", Double.parseDouble("3.14159")).add("operator", ">=").add("expected", "2").add("Scenario", "double >= string double"))
                .put("dl##", new StaticDL().add("actual", '4').add("operator", "==").add("expected", "4").add("Scenario", "char == string char"))
                .put("dl##", new StaticDL().add("actual", '4').add("operator", "!=").add("expected", "5").add("Scenario", "char != string char"))
                .put("dl##", new StaticDL().add("actual", '4').add("operator", "<").add("expected", "5").add("Scenario", "char < string char"))
                .put("dl##", new StaticDL().add("actual", '4').add("operator", "<=").add("expected", "4").add("Scenario", "char <= string char"))
                .put("dl##", new StaticDL().add("actual", '4').add("operator", ">").add("expected", "3").add("Scenario", "char > string char"))
                .put("dl##", new StaticDL().add("actual", '4').add("operator", ">=").add("expected", "2").add("Scenario", "char >= string char"))
                .put("dl##", new StaticDL().add("actual", "3.14159").add("operator", "==").add("expected", 3.14159).add("Scenario", "double string == double"))
                .put("dl##", new StaticDL().add("actual", "3.14159").add("operator", ">=").add("expected", "2").add("Scenario", "string double >= string double"))
                .put("dl##", new StaticDL().add("actual", "3.14159").add("operator", "<").add("expected", "3.142").add("Scenario", "string double < string double"))
                .put("dl##", new StaticDL().add("actual", "3").add("operator", ">").add("expected", "2").add("Scenario", "string int > string int"))
                .put("dl##", new StaticDL().add("actual", "3").add("operator", "<=").add("expected", "4").add("Scenario", "string int <= string int"))
                .put("dl##", new StaticDL().add("actual", "3.14159").add("operator", "==").add("expected", new BigDecimal("3.14159")).add("Scenario", "double string == bigdecimal"))
        ;
        return d;
    }


    @GoateProvider(name = "compare primitives")
    @Test(groups = {"unit"}, dataProvider = "methodLoader")
    public void compare(Goate testdata) {
        LOG.info("comparing primitives", "" + get("actual") + get("operator", "", String.class) + get("expected"));
        expect(Expectation.build()
                .actual("actual")
                .from(data)
                .is(get("operator", "==", String.class))
                .expected(get("expected")));
        evaluate();
        LOG.info("directly called evaluate and it passed.");
    }

    @Test(groups = {"unit"})
    public void checkNull() {
        assertTrue(isNull(null));
        assertTrue(isNull(JSONObject.NULL));
        assertFalse(isNull(new JSONObject()));
    }

    @Test(groups = {"unit"})
    public void checkArray() {
        int[] a1 = {42, 84, 168};
        int[] a2 = {42, 84, 168};
        expect(Expectation.build()
                .actual(a1)
                .isEqualTo(a2));
    }

    @ExpectToFail
    @Test(groups = {"unit"})
    public void zeroNotEqual() {
        expect(Expectation.build()
                .setId("isequalto")
                .actual(0)
                .isEqualTo(0)
                .failureMessage("this should pass, because they are equal."));
        expect(Expectation.build()
                .setId("isgreaterthan")
                .actual(0)
                .isGreaterThan(0)
                .failureMessage("this should fail, because they arent equal."));
    }

    @Test(groups = {"unit"})
    public void checkArrayIgnoreOrder() {
        int[] a1 = {42, 84, 168};
        int[] a2 = {84, 168, 42};
        expect(Expectation.build()
                .actual(a1)
                .isEqualToIgnoreOrder(a2));
    }

    @Test(groups = {"unit"})
    public void dateIsNotNull() {
        expect(Expectation.build()
                .actual("2019-01-01")
                .isNotEqualTo(null));
    }

    @GoateDLP(name = "compare longs")
    public Goate[] dlpForLongComparison() {
        Goate[] d = new Goate[2];
        d[0] = new Goate()
                .put("dl##", new StaticDL().add("Scenario", "string, string").add("actual", "0").add("expected", "0"))
                .put("dl##", new StaticDL().add("Scenario", "string, int").add("actual", "0").add("expected", 0))
                .put("dl##", new StaticDL().add("Scenario", "int, string").add("actual", 0).add("expected", "0"))
                .put("dl##", new StaticDL().add("Scenario", "int, int").add("actual", 0).add("expected", 0))
                .put("dl##", new StaticDL().add("Scenario", "int, long").add("actual", 0).add("expected", 0L))
                .put("dl##", new StaticDL().add("Scenario", "long, int").add("actual", 0L).add("expected", 0))
                .put("dl##", new StaticDL().add("Scenario", "long, long").add("actual", 0L).add("expected", 0L))
        ;
        return d;
    }

    @GoateProvider(name = "compare longs")
    @Test(groups = {"unit"}, dataProvider = "methodLoader")
    public void compareLongsFrom(Goate testdata) {
        expect(Expectation.build()
                .actual("actual")
                .from(testdata)
                .isEqualTo("expected")
                .fromExpected(testdata));
    }

    @GoateProvider(name = "compare longs")
    @Test(groups = {"unit"}, dataProvider = "methodLoader")
    public void compareLongsDirect(Goate testdata) {
        expect(Expectation.build()
                .actual(get("actual"))
                .isEqualTo(get("expected")));
    }
}
