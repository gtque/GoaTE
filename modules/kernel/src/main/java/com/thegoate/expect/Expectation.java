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
import com.thegoate.dsl.Interpreter;
import com.thegoate.expect.builder.Value;
import com.thegoate.expect.extras.Extra;
import com.thegoate.expect.extras.IgnoreOrder;
import com.thegoate.expect.extras.OneOrMore;
import com.thegoate.expect.extras.ZeroOrMore;
import com.thegoate.expect.test.SkipExpectation;
import com.thegoate.expect.validate.Validate;
import com.thegoate.locate.Locate;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.reflection.Executioner;
import com.thegoate.staff.Employee;
import com.thegoate.utils.compare.CompareUtil;
import com.thegoate.utils.fill.serialize.DeSerializer;
import com.thegoate.utils.fill.serialize.DefaultSource;
import com.thegoate.utils.fill.serialize.Serializer;
import com.thegoate.utils.togoate.ToGoate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.thegoate.Goate.GOATE_VARIABLE_PREFIX;
import static com.thegoate.logging.volume.VolumeKnob.volume;

/**
 * Contains the information about what is expected.
 * The source should be defined and annotated as an Employee.<br>
 * Created by Eric Angeli on 5/8/2017.
 */
public class Expectation {

    public final static String EmployeeWorkResult = GOATE_VARIABLE_PREFIX + "Employee Work Result";

    public Validate getValidator() {
        return validator;
    }

    public Expectation validate(Validate validator) {
        return setValidator(validator);
    }

    public Expectation setValidator(Validate validator) {
        this.validator = validator;
        this.operator = validator.operator();
        simpleState += "i";
        checkState(false);
        return this;
    }

    public long getRetryTimeout() {
        return retryTimeout;
    }

    public Expectation retryTimeout(long retryTimeout) {
        this.retryTimeout = retryTimeout;
        return this;
    }

    public long getRetryPeriod() {
        return retryPeriod;
    }

    public Expectation retryPeriod(long retryPeriod) {
        this.retryPeriod = retryPeriod;
        return this;
    }

    enum EXTRAS {
        ZERO_OR_MORE("zeroOrMore", new ZeroOrMore()), ONE_OR_MORE("oneOrMore", new OneOrMore()), IGNORE_ORDER("ignoreOrder", new IgnoreOrder());
        String label;
        Extra extra;

        EXTRAS(String label, Extra extra) {
            this.label = label;
            this.extra = extra;
        }

        public EXTRAS process(Expectation expectation, Object value) {
            extra.processExtra(expectation, value);
            return this;
        }

        public static EXTRAS lookUp(String label) {
            EXTRAS extra = null;
            for (EXTRAS ext : values()) {
                if (ext.label.equalsIgnoreCase(label)) {
                    extra = ext;
                    break;
                }
            }
            return extra;
        }
    }

    protected final BleatBox LOG = BleatFactory.getLogger(getClass());
    String name = "";
    String expectedName = "";
    String id = "";
    Goate data = null;
    volatile Goate expect = new Goate();
    String simpleState = "";
    volatile Object actual;
    String operator = "";
    private Validate validator = null;
    volatile Object expected = null;
    long cachePeriod = 50L;
    Object from = null;
    Object fromExpected = null;
    Object source = null;
    boolean zeroOrMore = true;
    boolean ignoreOrder = false;
    String clearedState = null;
    StringBuilder failed = new StringBuilder("");
    String failureMessage = null;
    volatile List<Goate> fails = Collections.synchronizedList(new ArrayList<>());//new ArrayList<>();
    volatile List<Goate> passes = Collections.synchronizedList(new ArrayList<>());//new ArrayList<>();
    boolean includeExtras = false;
    private long retryTimeout = -42L;
    private long retryPeriod = -42L;
    private final Object synchro = new Object();

    public Expectation(Goate data) {
        this.data = data;
    }

    public static Expectation build() {
        return build(null);
    }

    public static Expectation build(Goate data) {
        if (data == null) {
            data = new Goate();
        }
        return new Expectation(data);
    }

    public String fullName() {
        return name + "#" + id + (expectedName.isEmpty() ? "" : "::" + expectedName) + (validator == null ? "" : "::" + validator.getName());
    }

    public String id() {
        return this.id;
    }

    public Expectation period(long period) {
        this.cachePeriod = period;
        return this;
    }

    public Expectation oneOrMore() {
        zeroOrMore = false;
        includeExtras = true;
        return this;
    }

    public Expectation zeroOrMore() {
        zeroOrMore = true;
        includeExtras = true;
        return this;
    }

    public Expectation ignoreOrder() {
        ignoreOrder = true;
        return this;
    }

    public Expectation fromClone(Object source) {
        Object clone = null;
        if (source != null) {
            Goate dna = new Serializer<>(source, DefaultSource.class).toGoate();
            clone = new DeSerializer().data(dna).build(source.getClass());
        }
        return from(clone);
    }

    public Expectation from(Object source) {
        return from(source, true);
    }

    protected Expectation from(Object source, boolean setFrom) {
        this.source = source;
        if (source instanceof String) {
            this.from = buildSource("" + source);
            if (this.from == null && setFrom) {
                this.from = source;
            }
        } else {
            if (source != null && !source.equals(actual)) {
                this.from = source;
            }
        }
        if (name == null || name.isEmpty() || name.equals(actual)) {
            if (source != null) {
                String prename = "actual:";
                if(setFrom){
                    prename = "from:";
                }
                name = prename + volume(source);
            }
        }
        return this;
    }

    public Object getFrom() {
        return this.from;
    }

    public Object getActual() {
        return actual;
    }

    public String getOperator() {
        return operator;
    }

    public Object getExpected() {
        return expected;
    }

    public Object getFromExpected() {
        return this.fromExpected;
    }

    public String getFailureMessage() {
        return this.failureMessage;
    }

    /**
     * When adding a new expectation to an existing one you must set actual, is, and expected, (even if expected is null)
     * Except for the last one added.
     *
     * @return Self for syntactic sugar.
     */
    public Expectation addNewExpectation() {
        checkState(true);
        simpleState = "";
        actual = null;
        operator = "";
        expected = null;
        return this;
    }

    public Goate getExpectations() {
        return expect;
    }

    public Expectation add(Expectation expectation) {
        Goate ex = expectation.getExpectations();
        for (String key : ex.keys()) {
            Goate exp = (Goate) ex.get(key);
            actual(exp.getStrict("actual")).is("" + exp.getStrict("operator")).expected(exp.getStrict("expected")).failureMessage("" + exp.getStrict("failure message"));
        }
        return this;
    }

    public Expectation actualValue(Value value) {
        if (value != null) {
            actual(value.getLocator());
            if (value.getContainer() != null) {
                from(value.getContainer());
            }
        }
        return this;
    }

    public Expectation actual(Object actual) {
        if (actual instanceof Value) {
            return actualValue((Value) actual);
        }
        if (actual instanceof Locate) {
            actual = ((Locate) actual).toPath();
        }
        if (actual == null) {
            actual = "null::";
        }
        this.actual = actual;
        if (this.source == null) {
            from(actual, false);
        }
        simpleState += "a";
        checkState(false);
        return this;
    }

    public Expectation isEqualTo(Object expected) {
        return is("==").expected(expected);
    }

    public Expectation isEqualToIgnoreOrder(Object expected) {
        return is("~==").expected(expected);
    }

    public Expectation isEqualToIgnoreCase(Object expected) {
        return is("~=").expected(expected);
    }

    public Expectation isNotEqualTo(Object expected) {
        return is("!=").expected(expected);
    }

    public Expectation isGreaterThan(Object expected) {
        return is(">").expected(expected);
    }

    public Expectation isGreaterThanOrEqualTo(Object expected) {
        return is(">=").expected(expected);
    }

    public Expectation isLessThan(Object expected) {
        return is("<").expected(expected);
    }

    public Expectation isLessThanOrEqualTo(Object expected) {
        return is("<=").expected(expected);
    }

    public Expectation isPresent(Object expected) {
        return is("isPresent").expected(expected);
    }

    public Expectation isEmpty(Object expected) {
        return is("isEmpty").expected(expected);
    }

    public Expectation isNull(Object expected) {
        return is("isNull").expected(expected);
    }

    public Expectation isIn(Object expected) {
        return is("contains").expected(expected);
    }

    public Expectation contains(Object expected) {
        return is("contains").expected(expected);
    }

    public Expectation is(String operator) {
        this.operator = operator;
        this.validator = null;
        simpleState += "i";
        checkState(false);
        return this;
    }

    public Expectation is(Class operator) {
        CompareUtil cu = (CompareUtil) operator.getAnnotation(CompareUtil.class);
        return is(cu.operator());
    }

    public Expectation is(ExpectedBuilder expected) {
        is(expected.getClass());
        expected(expected.generateExpected());
        fromExpected(expected.fromExpected());
        return this;
    }

    public Expectation expectedValue(Value value) {
        if (value != null) {
            expected(value.getLocator());
            if (value.getContainer() != null) {
                fromExpected(value.getContainer());
            }
        }
        return this;
    }

    public Expectation expected(Object expected) {
        if (expected instanceof Value) {
            return expectedValue((Value) expected);
        }

        if (expected instanceof Locate) {
            expected = ((Locate) expected).toPath();
        }
        this.expected = expected;
        simpleState += "c";
        checkState(false);
        return this;
    }

    public Expectation includeExtras(){
        includeExtras = true;
        return this;
    }

    protected void checkState(boolean force) {
        if ((simpleState.length() == 3 && simpleState.contains("a") && simpleState.contains("i") && simpleState.contains("c")) || force) {
            if (actual != null && operator != null && !operator.isEmpty()) {//must at least set actual and operator fields.
                String key = "" + actual + operator + expected;
                Goate exp = new Goate();
                exp.put("actual", actual);
                exp.put("operator", operator);
                exp.put("expected", expected);
                exp.put("from", from);
                exp.put("failure message", failureMessage);
                if (includeExtras) {
                    Goate extras = new Goate();
                    extras.put("zeroOrMore", zeroOrMore);
                    extras.put("ignoreOrder", ignoreOrder);
                    exp.put("extras", extras);
                }
                expect.put(key, exp);
                actual = null;
                operator = "";
                expected = null;
                simpleState = "";
                failureMessage = null;
                clearedState = key;
            }
        } else {
            clearedState = null;
        }
    }

    public boolean evaluate() {
        synchronized (synchro) {
            failed = new StringBuilder("");//only the last failure is preserved. if the expectation is executed more than once it resets the failure message.
            fails = new ArrayList<>();
            passes = new ArrayList<>();
            boolean result = true;//assume true, and if a failure is detected set to false.
            boolean forceFrom = false;
            if (from == null) {
                forceFrom = true;
                from = new Goate().put("actual", source);
            }
            try {
                if (from instanceof Employee) {
                    source = ((Employee) from).expectation(this).defaultPeriod(cachePeriod).syncWork();
                } else {
                    source = from;
                }
            } catch (Throwable e) {
                LOG.error("Expectation", "Problem getting the source for comparison: " + e.getMessage(), e);
                logFail(e);
                source = null;
            }

            if (source != null) {
                if (source instanceof SkipExpectation) {
                    LOG.info("Expectation", "Specifically skipped an expectation.");
                } else {
                    List<Validate> checkers = new ArrayList<>();
                    try {
                        LOG.debug("Expectation", "evaluating expectations.");
                        Object rtrn = new ToGoate(source).convert();//source;//from.work();
                        if (rtrn == null) {
                            rtrn = new Goate().get("source", source);
                        }
                        if (rtrn instanceof Goate) {
                            Set<String> keys = ((Goate) rtrn).keys();
                            if (keys.size() == 1 && keys.contains("_original_")) {
                                rtrn = source;
                            }
                        }
                        if (expect.size() == 0) {
                            logFail(new Exception("Expection not defined properly."), new Goate()
                                    .put("actual", getActual())
                                    .put("from", getFrom())
                                    .put("operator", getOperator())
                                    .put("expected", getExpected())
                                    .put("fromExpected", getFromExpected())
                                    .put("failure message", getFailureMessage()));
                            result = false;
                        } else {
                            for (String key : expect.keys()) {
                                Goate exp = (Goate) expect.get(key);
                                if (forceFrom) {
                                    exp.put("actual", "actual");
                                    exp.put("from", from);
                                }
//                        String act = exp.get("actual", null, String.class);
//                        if(act != null && act.equals(EmployeeWorkResult)){
//                            exp.put("actual", "actual");
//                            exp.put("from", source);
//                        }
                                Validate checker = buildValidator(exp, key, rtrn);//new Checker(exp, key, rtrn);
//                        checker.start();
                                checkers.add(checker);
                            }
                        }
                        result = playCheckers(checkers);
                    } catch (Throwable t) {
                        result = false;
                        logFail(t);
                    } finally {
                        for (Validate checker : checkers) {
                            passes.addAll(checker.getPasses());
                            if (checker.getFails().size() > 0) {
                                LOG.warn("Expectation Checker", "detected failed expectations.");
                            }
                            fails.addAll(checker.getFails());
                        }
                    }
                }
            } else {
                result = false;
                logFail(new Exception("The source of the data to check was not set"), expect);
//            failed.append("the source of the data to check was not set.");
//            fails.add(expect);
            }
            return result;
        }
    }

    private boolean playCheckers(List<Validate> checkers) {
        boolean result = new Executioner<Validate>().process(checkers);
        for (Validate checker : checkers) {
            if (!checker.result()) {
                result = false;
            }
        }
        return result;
    }

    private void logFail(Throwable t) {
        Goate exp = new Goate();
        exp.put("from", fullName());
        exp.put("error", t.getMessage());
        logFail(t, exp);
    }

    private void logFail(Throwable t, Goate exp) {
        LOG.error(t.getMessage(), t);
        failed.append("there was a problem executing the work: " + t.getMessage() + "\n");
        fails.add(exp);
    }

    public String failed() {
        synchronized (synchro) {
            if (from instanceof Employee) {
                failed.append(((Employee) from).getHrReport().printRecords());
            }
            return failed.toString();
        }
    }

    public List<Goate> fails() {
        synchronized (synchro) {
            return fails;
        }
    }

    public List<Goate> passes() {
        synchronized (synchro) {
            return passes;
        }
    }

    /**
     * This can be used to build the expectation by parsing a string that defines the expectation.<br>
     * There are four parts to the definition<br>
     * the first part is the source followed by "{@literal >}" <br>
     * then the next three parts define how to validate the expectation, they should be separated using ","<br>
     * If the value you are expecting needs to include a "," you will need to abstract it using o::expected_value<br>
     * and make sure expected_value contains the actual value.<br>
     * example: source1{@literal >}field_1,==,int::42 <br>
     * this means "get field_1 from the source and validate that it is equal to the int value 42"
     *
     * @param expectation The string defining the expectation
     * @return Instance of itself, syntactic sugar.
     */
    public Expectation define(String expectation) {
        if (data == null) {
            data = new Goate();
        }
        if (expectation != null && !expectation.isEmpty()) {
            String source = null;
            if (expectation.startsWith(">>")) {
                expectation = "" + data.getStrict(expectation.substring(2));
            }
            if (expectation.contains(">")) {
                source = expectation.substring(0, expectation.indexOf(">"));
                expectation = expectation.substring(expectation.indexOf(">") + 1);
            }
            String[] parts = expectation.split(",");
            Interpreter i = new Interpreter(data);
            from(source);
            Object act = i.translate(parts[0]);
            actual(act);
            if (parts.length > 1) {
                is("" + i.translate(parts[1]));
            }
            if (parts.length > 2) {
                expected(i.translate(parts[2]));
            }
            if (parts.length > 3) {
                processExtras(parts[3].split("&"));
            }
            if (parts.length > 4) {
                failureMessage(parts[4]);
            }
        }
        return this;
    }

    private void processExtras(String[] extras) {
        Interpreter i = new Interpreter(data);
        for (String extra : extras) {
            String[] pieces = extra.split(":=");
            String extraKey = "" + i.translate(pieces[0]);
            Object extraValue = null;
            if (pieces.length > 1) {
                extraValue = i.translate(pieces[1]);
            }
            EXTRAS.lookUp(extraKey).process(this, extraValue);
        }
    }

    public Expectation failureMessage(String failureMessage) {
        if (clearedState != null) {
            ((Goate) expect.get(clearedState)).put("failure message", failureMessage);
        } else {
            this.failureMessage = failureMessage;
        }
        return this;
    }

    public Expectation fromExpected(Object from) {
        this.fromExpected = from;
        expectedName = "" + from;
        checkState(false);
        return this;
    }

    public Expectation setData(Goate data) {
        this.data = data;
        initFrom(data, from);
        initFrom(data, fromExpected);
        return this;
    }

    protected void initFrom(Goate data, Object init) {
        if (init != null && init instanceof Employee) {
            ((Employee) init).mergeData(data);
        }
    }

    protected Employee buildSource(String source) {
        Employee worker = null;
        if (data == null) {
            data = new Goate();
        }
        Interpreter i = new Interpreter(data);
        Object workerId = i.translate(source);
        if (workerId instanceof String) {
            source = "" + workerId;
            String[] sourceInfo = source.split("#");//if the source does not define an id number, assume 0.
//            this.name = volume(sourceInfo[0]);
            if (sourceInfo.length > 1) {
                this.id = sourceInfo[1];
            } else {
                this.id = "0";
            }
            worker = Employee.recruit(source, data);
        }
        return worker;
    }

    protected Validate buildValidator(Goate exp, String key, Object rtrn) {
        Validate vlad;
        if (getValidator() != null) {
            vlad = getValidator()
                    .setExp(exp)
                    .setKey(key)
                    .setRtrn(rtrn)
                    .setPeriod(cachePeriod)
                    .setData(data);
        } else {
            vlad = Validate.lookUpGeneric(exp, key, from, fromExpected, rtrn, cachePeriod, data);
//            if (rtrn instanceof Goate) {
//                vlad = new ValidateGoate(exp, key, from, fromExpected, rtrn, cachePeriod, data);
//            } else {
//                vlad = new ValidateNotGoate(exp, key, from, fromExpected, rtrn, cachePeriod, data);
//            }
        }
        return vlad;
    }
}
