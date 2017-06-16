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
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.staff.Employee;
import com.thegoate.utils.compare.Compare;
import com.thegoate.utils.get.Get;
import com.thegoate.utils.get.NotFound;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the information about what is expected.
 * The source should be defined and annotated as an Employee.<br>
 * Created by Eric Angeli on 5/8/2017.
 */
public class Expectation {
    protected final BleatBox LOG = BleatFactory.getLogger(getClass());
    String name = "";
    String id = "";
    Goate data = null;
    Goate expect = new Goate();
    String simpleState = "";
    Object actual;
    String operator = "";
    Object expected = null;
    Employee from = null;
    StringBuilder failed = new StringBuilder("");
    List<Goate> fails = new ArrayList<>();
    List<Goate> passes = new ArrayList<>();
    boolean resultC = true;

    public Expectation(Goate data) {
        this.data = data;
    }

    public String fullName() {
        return name + "#" + id;
    }

    public String id() {
        return this.id;
    }

    public Expectation from(Object source) {
        if (source instanceof String) {
            this.from = buildSource("" + source);
        } else if (source instanceof Employee) {
            this.from = (Employee) source;
        }
        return this;
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
//            Map<String, Object> exp = (Map<String, Object>) ex.get(key);
            Goate exp = (Goate) ex.get(key);
            actual(exp.getStrict("actual")).is("" + exp.getStrict("operator")).expected(exp.getStrict("expected"));
        }
        return this;
    }

    public Expectation actual(Object actual) {
        this.actual = actual;
        simpleState += "a";
        checkState(false);
        return this;
    }

    public Expectation is(String operator) {
        this.operator = operator;
        simpleState += "i";
        checkState(false);
        return this;
    }

    public Expectation expected(Object expected) {
        this.expected = expected;
        simpleState += "c";
        checkState(false);
        return this;
    }

    protected void checkState(boolean force) {
        if ((simpleState.length() == 3 && simpleState.contains("a") && simpleState.contains("i") && simpleState.contains("c")) || force) {
            if (actual != null && operator != null && !operator.isEmpty()) {//must at least set actual and operator fields.
                String key = "" + actual + operator + expected;
//                Map<String, Object> exp = new ConcurrentHashMap<>();
                Goate exp = new Goate();
                exp.put("actual", actual);
                exp.put("operator", operator);
                exp.put("expected", expected);
                exp.put("from", fullName());
                expect.put(key, exp);
                actual = null;
                operator = "";
                expected = null;
                simpleState = "";
            }
        }
    }

    public boolean evaluate() {
        failed = new StringBuilder("");//only the last failure is preserved. if the expectation is executed more than once it resets the failure message.
        boolean result = true;//assume true, and if a failure is detected set to false.
        if (from != null) {
            try {
                Object rtrn = from.work();
                for (String key : expect.keys()) {
//                    Map<String, Object> exp = (Map<String, Object>) expect.get(key);
                    Goate exp = (Goate) expect.get(key);
                    try {
                        resultC = true;
                        boolean check = check(exp, key, rtrn);
                        if (!check) {
                            result = false;
                        }
                    } catch (Throwable t) {
                        if (key.contains("*")) {
                            if (!resultC) {
                                result = false;
                            }
                        } else {
                            result = false;
                        }
                    }
                }
            } catch (Throwable t) {
                result = false;
                failed.append("there was a problem executing the work: " + t.getMessage() + "\n");
            }
        } else {
            result = false;
            failed.append("the source of the data to check was not set.");
        }
        return result;
    }

    protected boolean check(Goate exp, String key, Object rtrn) {
        boolean result = true;
        Object val = null;
        String act = "" + exp.get("actual");
        if (act.contains("*")) {
            int index = 0;
            int star = act.indexOf("*");
            try {
                while (true) {
                    Goate expt = new Goate().merge(exp, true);
                    String act2 = act.substring(0, star) + index + act.substring(star + 1);
                    String keyt = key.replace(act, act2);
                    expt.put("actual", act2);
                    boolean check = check(expt, keyt, rtrn);
                    if (!check) {
                        resultC = false;
                        result = false;
                    }
                    index++;
                }
            } catch (Throwable t) {
                if (index == 0) {//escape back. This will leave the current result state
                    //that means if the first index is NotFound there is nothing to evaluate, if it should fail
                    //because a field should be present you will have to check for that some other way other than
                    //using the wild card, otherwise you could end up in an infinite recursive loop.
                    //you could, if checking elements inside an array, check that the size of the array is greater than 0.
                    throw t;
                }
            }
        } else {
            if (exp.get("actual") instanceof String && ((String) exp.get("actual")).equalsIgnoreCase("return")) {
                val = rtrn;
            } else {
                Get get = new Get(exp.get("actual"));//from.doWork());
                val = get.from(rtrn);
            }
            if (val instanceof NotFound) {
                if (!exp.get("operator").equals("doesNotExist")) {
                    LOG.info("" + exp.get("actual") + " was not found, but this does not necessarily indicate a failure.");
                    throw new RuntimeException("Did not find: " + exp.get("actual"));
                }
            } else {
                exp.put("actual_value", val);
                LOG.info("evaluating \"" + fullName() + "\": " + exp.get("actual") + "(" + val + ") " + exp.get("operator") + (exp.get("expected") == null ? "" : " " + exp.get("expected")));
                if (!(new Compare(val).to(exp.get("expected")).using(exp.get("operator")).evaluate())) {
                    result = false;
                    failed.append(fullName() + ">" + key + " evaluated to false.\n");
                    fails.add(exp);
                } else {
                    passes.add(exp);
                }
            }
        }
        return result;
    }

    public String failed() {
        return failed.append(from.getHrReport().printRecords()).toString();
    }

    public List<Goate> fails() {
        return fails;
    }

    public List<Goate> passes() {
        return passes;
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
            String source = "";
            if (expectation.contains(">")) {
                source = expectation.substring(0, expectation.indexOf(">"));
                expectation = expectation.substring(expectation.indexOf(">") + 1);

            }
            String[] parts = expectation.split(",");
            Interpreter i = new Interpreter(data);
            from(source);
            actual("" + i.translate(parts[0]));
            if (parts.length > 1) {
                is("" + i.translate(parts[1]));
            }
            if (parts.length > 2) {
                expected("" + i.translate(parts[2]));
            }
        }
        return this;
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
            this.name = sourceInfo[0];
            if (sourceInfo.length > 1) {
                this.id = sourceInfo[1];
            } else {
                this.id = "0";
            }
            worker = Employee.recruit(source, data);
        }
        return worker;
    }
}
