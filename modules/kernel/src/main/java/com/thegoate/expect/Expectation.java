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
import com.thegoate.dsl.GoateDSL;
import com.thegoate.dsl.Interpreter;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.staff.Employee;
import com.thegoate.utils.compare.Compare;
import com.thegoate.utils.compare.CompareUtil;
import com.thegoate.utils.compare.CompareUtility;
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
    String expectedName = "";
    String id = "";
    Goate data = null;
    Goate expect = new Goate();
    String simpleState = "";
    volatile Object actual;
    String operator = "";
    volatile Object expected = null;
    long period = 50L;
    Object from = null;
    Object fromExpected = null;
    Object fromExpectedResult = null;
    Object source = null;
    StringBuilder failed = new StringBuilder("");
    List<Goate> fails = new ArrayList<>();
    List<Goate> passes = new ArrayList<>();
    boolean resultC = true;

    public Expectation(Goate data) {
        this.data = data;
    }

    public static Expectation build(){
        return build(null);
    }

    public static Expectation build(Goate data){
        if(data == null){
            data = new Goate();
        }
        return new Expectation(data);
    }

    public String fullName() {
        return name + "#" + id + (expectedName.isEmpty()?"":"::" + expectedName);
    }

    public String id() {
        return this.id;
    }

    public Expectation period(long period){
        this.period = period;
        return this;
    }

    public Expectation from(Object source) {
        return from(source, true);
    }

    protected Expectation from(Object source, boolean setFrom){
        this.source = source;
        if (source instanceof String) {
            this.from = buildSource("" + source);
            if(this.from==null&&setFrom){
                this.from = source;
            }
        } else {
            if(source!=null&&!source.equals(actual)) {
                this.from = source;
            }
        }
        if (name == null || name.isEmpty() || name.equals(actual)) {
            name = "" + source;
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
            Goate exp = (Goate) ex.get(key);
            actual(exp.getStrict("actual")).is("" + exp.getStrict("operator")).expected(exp.getStrict("expected"));
        }
        return this;
    }

    public Expectation actual(Object actual) {
        this.actual = actual;
        if (this.source == null) {
            from(actual,false);
        }
        simpleState += "a";
        checkState(false);
        return this;
    }

    public Expectation isEqualTo(Object expected){
        return is("==").expected(expected);
    }

    public Expectation isNotEqualTo(Object expected){
        return is("!=").expected(expected);
    }

    public Expectation isGreaterThan(Object expected){
        return is(">").expected(expected);
    }

    public Expectation isGreaterThanOrEqualTo(Object expected){
        return is(">=").expected(expected);
    }

    public Expectation isLessThan(Object expected){
        return is("<").expected(expected);
    }

    public Expectation isLessThanOrEqualTo(Object expected){
        return is("<=").expected(expected);
    }

    public Expectation isPresent(Object expected){
        boolean isPresent = true;
        try{
            isPresent = Boolean.parseBoolean(""+expected);
        } catch (Exception e){
            LOG.debug("Expectation isPresent", "Failed to detect state of expected isPresent, defaulting to true.");
        }
        return isPresent?is("isPresent").expected(expected):is("doesNotExist").expected("true");
    }

    public Expectation isEmpty(Object expected){
        return is("isEmpty").expected(expected);
    }

    public Expectation isNull(Object expected){
        return is("isNull").expected(expected);
    }

    public Expectation is(String operator) {
        this.operator = operator;
        simpleState += "i";
        checkState(false);
        return this;
    }

    public Expectation is(Class operator){
        CompareUtil cu = (CompareUtil)operator.getAnnotation(CompareUtil.class);
        return is(cu.operator());
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
        fails = new ArrayList<>();
        passes = new ArrayList<>();
        boolean result = true;//assume true, and if a failure is detected set to false.
        if (from != null) {
            try {
                if(from instanceof Employee) {
                    source = ((Employee)from).defaultPeriod(period).syncWork();
                }else{
                    source = from;
                }
            } catch (Throwable e) {
                LOG.error("Expectation", "Problem get the source for comparison: " + e.getMessage(), e);
                Goate exp = new Goate();
                exp.put("from", fullName());
                exp.put("error", e.getMessage());
                LOG.error(e.getMessage(), e);
                failed.append("there was a problem executing the work: " + e.getMessage() + "\n");
                fails.add(exp);
                source = null;
            }
        }
        if (source != null) {
            try {
                LOG.debug("Expectation", "evaluating expectation: " + fullName());
                Object rtrn = source;//from.work();
                for (String key : expect.keys()) {
                    Goate exp = (Goate) expect.get(key);
                    if (from == null) {
                        exp.put("actual", "return");
                    }
                    try {
                        resultC = true;
                        boolean check = check(exp, key, rtrn);
                        if (!check) {
                            result = false;
                            failed.append(fullName() + ">" + key + " evaluated to false.\n");
                            fails.add(exp);
                        }
                    } catch (Throwable t) {
                        if (key.contains("*")) {
                            if (!resultC) {
                                result = false;
                                failed.append(fullName() + ">" + key + " evaluated to false.\n");
                                fails.add(exp);
                            }
                        } else {
                            result = false;
                            failed.append(fullName() + ">" + key + " evaluated to false.\n");
                            fails.add(exp);
                        }
                    }
                }
            } catch (Throwable t) {
                result = false;
                Goate exp = new Goate();
                exp.put("from", fullName());
                exp.put("error", t.getMessage());
                LOG.error(t.getMessage(), t);
                failed.append("there was a problem executing the work: " + t.getMessage() + "\n");
                fails.add(exp);
            }
        } else {
            result = false;
            failed.append("the source of the data to check was not set.");
        }
        fromExpectedResult = null;
        return result;
    }

    protected boolean check(Goate exp, String key, Object rtrn) {
        boolean result = true;
        Object val = null;
        String act = "" + exp.get("actual");
        String actP = act;
        if (act.contains("*")) {
            int index = 0;
            int star = act.indexOf("*")-1;
            String start = null;
            String expectedSize = null;
            int size = -42;
            if(star>=0){
                start = ""+act.charAt(star);
                if(start.equals("[")){
                    expectedSize = act.substring(star+2,act.indexOf("]"));
                    actP = expectedSize;
                    try{
                        if(expectedSize.contains(",")){
                            String[] es = expectedSize.split(",");
                            index = Integer.parseInt(es[0]);
                            expectedSize = es.length>1?es[1]:"";
                        }
                        size = Integer.parseInt(expectedSize)+index;
                    } catch(NumberFormatException nfe){
                        LOG.debug("Expectation", "Detected wildcard with expected, but seems it is malformed, defaulting to any number.", nfe);
                    }
                }
            }
            try {
                boolean running = true;
                String act1 = size>0||index>0?act.replaceFirst("\\[","").replaceFirst(""+actP+"]",""):act;
                while (running) {
                    Goate expt = new Goate().merge(exp, true);
                    String act2 = act1.replaceFirst("\\*",""+index);//act.substring(0, star) + index + act.substring(star + 1);
                    String keyt = key.replace(act, act2);
                    expt.put("actual", act2);
                    if(expt.get("expected") instanceof String){
                        expt.put("expected",expt.get("expected","",String.class).replaceAll("\\\\%","_mwi_").replaceAll("\\%",""+index));
                    }
                    boolean check = check(expt, keyt, rtrn);
                    if (!check) {
                        resultC = false;
                        result = false;
                    }
                    index++;
                    if(size>0&&index>size){
                        running = false;
                        //if there are more than expected this should fail.
                        resultC = false;
                        result = false;
                    }
                }
            } catch (Throwable t) {
                if(size>0&&(index==0||index!=size)){
                    //this should cause a failure if the expected size is not reached.
                    //use [*###] where ### is the expected size, ie: [*84]
                    resultC = false;
                    result = false;
                }
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
                    LOG.info("" + exp.get("actual") + " was not found, but this does not necessarily indicate a failure, to check if something is not present use 'doesNotExist'.");
                    exp.put("actual_value", "_NOT_FOUND_");
                    throw new RuntimeException("Did not find: " + exp.get("actual"));
                } else {
                    exp.put("actual_value", val);
                    passes.add(exp);
                }
            } else {
                exp.put("actual_value", val);
                Object expV = getExpectedValue(exp.get("expected"));
                exp.put("expected_value", expV);
                LOG.info("evaluating \"" + fullName() + "\": " + exp.get("actual") + "(" + val + ") " + exp.get("operator") + (exp.get("expected") == null ? "" : " " + exp.get("expected")));
                CompareUtility compare = new Compare(val).to(expV).using(exp.get("operator"));
                compare.setData(data);
                if (!(compare.evaluate())) {
                    result = false;
//                    failed.append(fullName()).append(">").append(key).append(" evaluated to false.\n");
                    Goate health = compare.healthCheck();
                    if(health.size()>0){
                        exp.put("failed", health);
                    }
//                    fails.add(exp);
                } else {
                    passes.add(exp);
                }
            }
        }
        return result;
    }

    public String failed() {
        if(from instanceof Employee) {
            failed.append(((Employee)from).getHrReport().printRecords());
        }
        return failed.toString();
    }

    public List<Goate> fails() {
        return fails;
    }

    public List<Goate> passes() {
        return passes;
    }

    protected Object getExpectedValue(Object exp){
        Object result = exp;
        if(fromExpected!=null){
            if(fromExpectedResult==null){
                if(fromExpected instanceof Employee) {
                    fromExpectedResult = ((Employee)fromExpected).defaultPeriod(period).syncWork();
                } else {
                    fromExpectedResult = fromExpected;
                }
            }
            result = new Get(exp).from(fromExpectedResult);
        }
        return result;
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
            if(expectation.startsWith(">>")){
                expectation = ""+data.getStrict(expectation.substring(2));
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
        }
        return this;
    }

    public Expectation fromExpected(Object from){
        this.fromExpected =  from;
        expectedName = "" + from;
        checkState(false);
        return this;
    }

    public Expectation setData(Goate data){
        this.data = data;
        initFrom(data, from);
        initFrom(data, fromExpected);
        return this;
    }

    protected void initFrom(Goate data, Object init){
        if(init!=null&&init instanceof Employee){
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
