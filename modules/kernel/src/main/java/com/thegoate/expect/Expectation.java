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
import com.thegoate.expect.extras.Extra;
import com.thegoate.expect.extras.OneOrMore;
import com.thegoate.expect.extras.ZeroOrMore;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.staff.Employee;
import com.thegoate.utils.compare.Compare;
import com.thegoate.utils.compare.CompareUtil;
import com.thegoate.utils.compare.CompareUtility;
import com.thegoate.utils.get.Get;
import com.thegoate.utils.get.NotFound;
import com.thegoate.utils.togoate.ToGoate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains the information about what is expected.
 * The source should be defined and annotated as an Employee.<br>
 * Created by Eric Angeli on 5/8/2017.
 */
public class Expectation {

    enum EXTRAS {
        ZERO_OR_MORE("zeroOrMore", new ZeroOrMore()), ONE_OR_MORE("oneOrMore", new OneOrMore());
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
    String starReplacement = "_s#T$a%R_";
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
    boolean zeroOrMore = true;
    StringBuilder failed = new StringBuilder("");
    volatile List<Goate> fails = Collections.synchronizedList(new ArrayList<>());//new ArrayList<>();
    volatile List<Goate> passes = Collections.synchronizedList(new ArrayList<>());//new ArrayList<>();
    boolean resultC = true;
    boolean includeExtras = false;
//    long flushCount = 0;
//    long flushAfter = 1000;

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
        return name + "#" + id + (expectedName.isEmpty() ? "" : "::" + expectedName);
    }

    public String id() {
        return this.id;
    }

    public Expectation period(long period) {
        this.period = period;
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
            from(actual, false);
        }
        simpleState += "a";
        checkState(false);
        return this;
    }

    public Expectation isEqualTo(Object expected) {
        return is("==").expected(expected);
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
        boolean isPresent = true;
        try {
            isPresent = Boolean.parseBoolean("" + expected);
        } catch (Exception e) {
            LOG.debug("Expectation isPresent", "Failed to detect state of expected isPresent, defaulting to true.");
        }
        return isPresent ? is("isPresent").expected(expected) : is("doesNotExist").expected("true");
    }

    public Expectation isEmpty(Object expected) {
        return is("isEmpty").expected(expected);
    }

    public Expectation isNull(Object expected) {
        return is("isNull").expected(expected);
    }

    public Expectation is(String operator) {
        this.operator = operator;
        simpleState += "i";
        checkState(false);
        return this;
    }

    public Expectation is(Class operator) {
        CompareUtil cu = (CompareUtil) operator.getAnnotation(CompareUtil.class);
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
                if(includeExtras) {
                    Goate extras = new Goate();
                    extras.put("zeroOrMore", zeroOrMore);
                    exp.put("extras", extras);
                }
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
                if (from instanceof Employee) {
                    source = ((Employee) from).defaultPeriod(period).syncWork();
                } else {
                    source = from;
                }
            } catch (Throwable e) {
                LOG.error("Expectation", "Problem get the source for comparison: " + e.getMessage(), e);
                logFail(e);
                source = null;
            }
        }
        if (source != null) {
            try {
                LOG.debug("Expectation", "evaluating expectations.");
                Object rtrn = new ToGoate(source).convert();//source;//from.work();
                if(rtrn==null){
                    rtrn = source;
                }
                List<Checker> checkers = new ArrayList<>();
                for (String key : expect.keys()) {
                    Goate exp = (Goate) expect.get(key);
                    if (from == null) {
                        exp.put("actual", "return");
                    }
                    Checker checker = new Checker(exp, key, rtrn);
                    checker.start();
                    checkers.add(checker);
                }
                boolean notFinished = true;
                while (notFinished) {
                    try {
                        Thread.sleep(50L);
                    } catch (Exception e) {
                        LOG.debug("Expectation", "Problem sleeping while evaluating: " + e.getMessage(), e);
                    }
                    notFinished = false;
                    for (Checker checker : checkers) {
                        if (checker.running()) {
                            notFinished = true;
                        } else {
                            if (!checker.result()) {
                                result = false;
                            }
                        }
                    }
                }
            } catch (Throwable t) {
                result = false;
                logFail(t);
            }
        } else {
            result = false;
            failed.append("the source of the data to check was not set.");
        }
        fromExpectedResult = null;
        return result;
    }

    private void logFail(Throwable t) {
        Goate exp = new Goate();
        exp.put("from", fullName());
        exp.put("error", t.getMessage());
        LOG.error(t.getMessage(), t);
        failed.append("there was a problem executing the work: " + t.getMessage() + "\n");
        fails.add(exp);
    }

    public String failed() {
        if (from instanceof Employee) {
            failed.append(((Employee) from).getHrReport().printRecords());
        }
        return failed.toString();
    }

    public List<Goate> fails() {
        return fails;
    }

    public List<Goate> passes() {
        return passes;
    }

    private String encodeOffset(int offset, String c) {
        StringBuilder os = new StringBuilder();
        offset++;
        while (offset > 0) {
            os.append(c);
            offset--;
        }
        return os.toString();
    }

    private int calculateOffset(String offset, int current) {
        return offset.contains(";") ? current : offset.length() - 1;
    }

    public Object calculateKey(Object exp, String act1) {
        String pattern = "" + exp;
        List<String> grouping = new ArrayList<>();
        int groupOffset = 0;
        while (pattern.contains("%")) {
            pattern = pattern.replace("\\%", "_p*e*r*C*e*n*t_");
            pattern = pattern.replace("%", "_open-" + encodeOffset(groupOffset, ":") + "-close_");
            pattern = pattern.replace("_p*e*r*C*e*n*t_", "%");
            groupOffset++;
        }
        Pattern p = Pattern.compile("([0-9]+)");
        Matcher m = p.matcher("" + act1);
        while (m.find()) {
            LOG.debug("Expected Wildcard Index", "Number of groups found: " + m.groupCount());
            LOG.debug("Expected Wildcard Index", "Group: " + m.group(0));
            grouping.add(m.group(0));
            LOG.debug("Expected Wildcard Index", "grouping: " + grouping.get(grouping.size() - 1));
            act1 = m.replaceFirst("_found-dnuof_");
            LOG.debug("Expected Wildcard Index", "Remainder: " + act1);
            m = p.matcher("" + act1);
        }
        int current = 0;
        Pattern p2 = Pattern.compile("(_open-(:*;*)-close_)");
        m = p2.matcher(pattern);
        LOG.debug("Expected Wildcard Index", "filling: " + pattern);
        while (m.find()) {
            String count = m.group(2);
            int ci = calculateOffset(count, current);
            pattern = m.replaceFirst(grouping.get(ci));
            LOG.debug("Expected Wildcard Index", "filled: " + pattern);
            current++;
            m = p2.matcher(pattern);
        }
        return pattern;
    }

    protected Object getExpectedValue(Object exp, String act1) {
        if((""+exp).contains("%")){
            exp = calculateKey(exp, act1);
        }
        Object result = exp;
        if (fromExpected != null) {
            if (fromExpectedResult == null) {
                if (fromExpected instanceof Employee) {
                    fromExpectedResult = ((Employee) fromExpected).defaultPeriod(period).syncWork();
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

    /**
     * Internal class for multi-threading the rolled up expectations.
     * Warning, there is no limiter on the number of threads this will spawn.
     * Ye' have been warned.
     */
    class Checker extends Thread {
        Goate exp;
        String key;
        Object rtrn;
        boolean result = false;
        volatile boolean running = true;
        StringBuilder evalBuffer = new StringBuilder();
        int patternIndex = 0;
        int checkIndex = 1;

        public String name() {
            return key;
        }

        public Checker(Goate exp, String key, Object rtrn) {
            this.exp = exp;
            this.key = key;
            this.rtrn = rtrn;
        }

        public boolean running() {
            return running;
        }

        public void run() {
            running = true;
            StringBuilder fm = new StringBuilder();
            fm.append(exp.get("actual")).append(exp.get("operator")).append(exp.get("expected")).append(" evaluated to false.\n");
//            boolean zeroOrMore = Boolean.parseBoolean("" + exp.get("extras", new Goate(), Goate.class).get("zeroOrMore", true));
            try {
                result = true;
                resultC = true;
                boolean check = false;
                if(rtrn instanceof Goate){
                    //This is much faster when using wildcard indexes, but requires an implementation of ToGoate for the from object type.
                    check = check(exp, key, (Goate)rtrn, zeroOrMore);
                } else {
                    //This is the older fall back implementation. It is only used when
                    //there is no proper ToGoate implementation found.
                    check = check2(exp, key, rtrn, zeroOrMore);
                }
                if (!check) {
                    result = false;
                    failed.append(fm);
                    fails.add(exp);
                }
            } catch (Throwable t) {
                if (key.contains("*")) {
                    if (!resultC) {
                        result = false;
                        failed.append(fm);
                        fails.add(exp);
                    }
                } else {
                    result = false;
                    failed.append(fm);
                    fails.add(exp);
                }
                LOG.debug("Evaluating", "This may or may not be an acutal problem: " + t.getMessage(), t);
            } finally {
                running = false;
            }
//            LOG.info("Check", evalBuffer.toString());
        }

        public boolean result() {
            return this.result;
        }

        private List<String> buildIndexChecks(String pattern) {
            List<String> checks = new ArrayList<>();
            while (pattern.contains("*") || pattern.contains("+")) {
                String[] calculated = calculateIndexCheck(pattern);
                pattern = calculated[patternIndex];
                if (calculated[checkIndex] != null) {
                    checks.add(calculated[checkIndex]);
                }
            }
            return checks;
        }

        private String[] calculateIndexCheck(String act1) {
            String[] ic = {null, null};
            int pattern = 0;
            int check = 1;
            int firstIndexStar = act1.indexOf("*");
            int firstIndexPlus = act1.indexOf("+");
            if (firstIndexStar > -1 && (firstIndexStar < firstIndexPlus)) {
                ic = calculateStarCheck(act1, firstIndexStar);
            } else {
                if (firstIndexPlus > -1) {
                    String base = act1.substring(0, firstIndexPlus);
                    ic[pattern] = base + starReplacement + act1.substring(firstIndexPlus + 1);
//                    String starReplace = "\\[*\\*[0-9]*,*[0-9]*\\]*";
                    ic[check] = base + "[0-9]+" + act1.substring(firstIndexPlus + 1).replaceAll("\\[*\\*[0-9]*,*[0-9]*\\]*", "*").replaceAll("\\+", "*") + ",>=,1";
                } else if (firstIndexStar > -1) {
                    ic = calculateStarCheck(act1, firstIndexStar);
                } else {
                    ic[pattern] = act1;
                }
            }
            return ic;
        }

        protected String[] calculateStarCheck(String act1, int firstIndexStar) {
            String[] ic = {null, null};
            int indexLeftSquare = act1.indexOf("[");
            String base = act1.substring(0, firstIndexStar);
            if (indexLeftSquare > -1 && indexLeftSquare < firstIndexStar) {
                int indexRightSquare = act1.indexOf("]");
                String[] i = act1.substring(firstIndexStar + 1, indexRightSquare).split(",");
                long size = Long.parseLong(i[0]);
                if (i.length == 2) {
                    size += Long.parseLong(i[1]);
                }
                ic[patternIndex] = base.substring(0, base.length() - 1) + starReplacement + act1.substring(indexRightSquare+1);
                ic[checkIndex] = base + "0-9]+"+act1.substring(indexRightSquare + 1).replaceAll("\\[*\\*[0-9]*,*[0-9]*\\]*", "*").replaceAll("\\+", "*")+",==," + size;
            } else {
                ic[patternIndex] = base + starReplacement + act1.substring(firstIndexStar + 1);
            }
            return ic;
        }

        protected boolean check(Goate exp, String key, Goate fromData, boolean zeroOrMore) {
            boolean result = true;
//            Object val = null;
            String filterPattern = "" + exp.get("actual");
            List<String> indexChecks = buildIndexChecks(filterPattern);
            filterPattern = filterPattern.replaceAll("\\[\\*[0-9]*,*[0-9]\\]","*").replace("+", "*").replace("*", "[0-9]*").replace(".", "\\.");
            Goate filteredData = fromData.filterStrict(filterPattern);
            if (filteredData.size() == 0) {
                if (exp.get("actual") instanceof String && ((String) exp.get("actual")).equalsIgnoreCase("return")) {
                    filteredData.put("val", fromData);
                }
            }
            if (exp.get("operator").equals("doesNotExist")) {
                if (filteredData.size() == 0 || filteredData.get("val") == null || filteredData.get("val") instanceof NotFound) {//filteredData.size()==0){//!filteredData.keys().contains(filterPattern)) {
                    exp.put("actual_value", new NotFound(filterPattern));
                    passes.add(exp);
                }
            } else {
                for (String vkey : filteredData.keys()) {
                    Goate ev = new Goate().merge(exp, true);
                    ev.put("actual", vkey);
                    Object val = filteredData.get(vkey);
                    ev.put("actual_value", val);
                    Object expV = getExpectedValue(ev.get("expected"), vkey);
                    ev.put("expected_value", expV);
                    LOG.info("Expect", new StringBuilder("evaluating:").append(vkey).append("(").append(val).append(") ").append(ev.get("operator")).append((ev.get("expected") == null ? "" : " " + ev.get("expected"))).append("\n").toString());
                    if (!compare(val, expV, ev)) {
                        result = false;
                    }
                }
                for (String checks : indexChecks) {
                    String[] check = checks.split(",");
                    Goate cev = new Goate();
                    check[0] = check[0].replace(starReplacement, "*").replace(".", "\\.");
                    cev.put("actual", check[0] + ".size()");
                    cev.put("operator", check[1]);
                    cev.put("expected", check[2]);
                    String[] plusCheck = check[0].split("\\[0-9\\]\\+");
                    String plusNext = plusCheck.length>1?plusCheck[1]:"";
                    if(plusNext.startsWith(".")){
                        plusNext = plusNext.substring(1);
                    }
                    if(plusNext.startsWith("\\.")){
                        plusNext = plusNext.replaceFirst("\\\\.","");
                    }
                    String[] pnc = plusNext.split("\\\\.");
                    if (check[1].equals("==")) {
                        Goate subset = fromData.filterStrict((plusCheck[0]+"[0-9]+").replace("*", "[0-9]*").replace("\\.[0-9]+",""));
                        //find expected total size
                        int totalSize = Integer.parseInt(""+check[2])*(subset.size()>0?subset.size():1);
                        //find actual total size
                        int actualSize = fromData.filterStrict((plusCheck[0]+"[0-9]+").replace("*", "[0-9]*")).size();
                        cev.put("actual_value", actualSize)
                                .put("expected_value", totalSize);
                        if (!compare(actualSize, totalSize, cev)) {
                            result = false;
                        }
                    } else {
                        //find how many are present
                        Goate plusFirstFilter = fromData.filterStrict((plusCheck[0]+"[0-9]+"+(plusCheck.length>1?"\\."+pnc[0]:"")).replace("*", "[0-9]*"));
                        //find how many should be present
                        Goate plusSecondFilter = fromData.filterStrict((plusCheck[0]+"[0-9]+").replace("*", "[0-9]*"));
                        cev.put("actual_value", plusFirstFilter.size())
                                .put("expected_value", plusSecondFilter.size());
                        if (!compare(plusFirstFilter.size(),plusSecondFilter.size(),cev)){//fromData.filterStrict(check[0].replace("*", "[0-9]*")).size(), i, cev)) {
                            result = false;
                        }
                    }
                }
            }
            return result;
        }

        /**
         * this is where the comparison actually takes place.
         * This is a good place for a breakpoint when trying to debug a comparator.
         * @param val The actual value
         * @param expV The expected value
         * @param ev The expecation definition
         * @return True if condition met, otherwise false
         */
        private boolean compare(Object val, Object expV, Goate ev) {
            boolean result = true;
            CompareUtility compare = new Compare(val).to(expV).using(ev.get("operator"));
            compare.setData(data);
            if (!(compare.evaluate())) {//step into here to debug the comparator
                fails.add(ev);
                result = false;
                Goate health = compare.healthCheck();
                if (health.size() > 0) {
                    ev.put("failed", health);
                }
            } else {
                passes.add(ev);
            }
            return result;
        }

        protected boolean check2(Goate exp, String key, Object rtrn, boolean zeroOrMore) {
            boolean result = true;
            Object val = null;
            String act = "" + exp.get("actual");
            String actP = act;
            int plus = act.indexOf("+");
            int star = act.indexOf("*");
            boolean moreThanZero = true;
            if (plus > -1 && (plus < star || star < 0)) {
                moreThanZero = false;
                act = act.substring(0, plus) + "*" + act.substring(plus + 1);
            }
            if (act.contains("*")) {
                int index = 0;
                star = act.indexOf("*") - 1;
                String start = null;
                String expectedSize = null;
                int size = -42;
                if (star >= 0) {
                    start = "" + act.charAt(star);
                    if (start.equals("[")) {
                        expectedSize = act.substring(star + 2, act.indexOf("]"));
                        actP = expectedSize;
                        try {
                            if (expectedSize.contains(",")) {
                                String[] es = expectedSize.split(",");
                                index = Integer.parseInt(es[0]);
                                expectedSize = es.length > 1 ? es[1] : "";
                            }
                            size = Integer.parseInt(expectedSize) + index;
                        } catch (NumberFormatException nfe) {
                            LOG.debug("Expectation", "Detected wildcard with expected, but seems it is malformed, defaulting to any number.", nfe);
                        }
                    }
                }
                String act1 = size > 0 || index > 0 ? act.replaceFirst("\\[", "").replaceFirst("" + actP + "]", "") : act;
                Goate expt = new Goate().merge(exp, true);
                try {
                    boolean running = true;
                    while (running) {
                        String act2 = act1.replaceFirst("\\*", "" + index);//act.substring(0, star) + index + act.substring(star + 1);
                        String keyt = key.replace(act, act2);
                        expt.put("actual", act2);
                        if (expt.get("expected") instanceof String) {
                            expt.put("expected", expt.get("expected", "", String.class).replaceAll("\\\\%", "_mwi_").replaceAll("\\%", "" + index).replaceAll("_mwi_", "%"));
                        }
                        boolean check = check2(expt, keyt, rtrn, zeroOrMore);
                        if (!check) {
                            resultC = false;
                            result = false;
                        }
                        index++;
                        if (size > 0 && index > size) {
                            running = false;
                            //if there are more than expected this should fail.
                            resultC = false;
                            result = false;
                        }
                    }
                } catch (Throwable t) {
                    if (size > 0 && (index == 0 || index != size)) {
                        //this should cause a failure if the expected size is not reached.
                        //use [*###] where ### is the expected size, ie: [*84]
                        resultC = false;
                        result = false;
                    }
                    if (!moreThanZero) {
                        if (index == 0) {
                            if (!checkEvaluated(passes, act1, index)) {
                                resultC = false;
                                result = false;
                            }
                        } else if (checkAct1(act1, rtrn, index)) {
                            resultC = false;
                            result = false;
                        }
                    }
//                    if (index == 0) {//escape back. This will leave the current result state
                    //that means if the first index is NotFound there is nothing to evaluate, if it should fail
                    //because a field should be present you will have to check for that some other way other than
                    //using the wild card, otherwise you could end up in an infinite recursive loop.
                    //you could, if checking elements inside an array, check that the size of the array is greater than 0.
                    throw t;
//                    }

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
                    Object expV = getExpectedValue(exp.getStrict("expected"),""+exp.get("actual"));
                    exp.put("expected_value", expV);
//                    LOG.info("evaluating \"" + fullName() + "\": " + exp.get("actual") + "(" + val + ") " + exp.get("operator") + (exp.get("expected") == null ? "" : " " + exp.get("expected")));
                    LOG.debug(new StringBuilder("evaluating \"").append(fullName()).append("\": ").append(exp.get("actual")).append("(").append(val).append(") ").append(exp.get("operator")).append((exp.get("expected") == null ? "" : " " + exp.get("expected"))).append("\n").toString());
                    result = compare(val, expV, exp);
                }
            }
            return result;
        }

        private boolean checkEvaluated(List<Goate> passed, String act1, int index) {
            boolean found = false;
            String find = calculateKey(act1, index);
            for (Goate exp : passed) {
                if (("" + exp.get("actual")).contains(find)) {
                    found = true;
                    break;
                }
            }
            return found;
        }

        private boolean checkAct1(String act1, Object from, int index) {
            boolean found = false;
            String check = calculateKey(act1, index);
            Object o = new Get(check).from(from);
            if (o != null && !(o instanceof NotFound)) {
                found = true;
            }
            return found;
        }

        private String calculateKey(String act1, int index) {
            int lastIndex = act1.indexOf("*");
            String key = act1;
            if (lastIndex > -1) {
                key = act1.substring(0, lastIndex - 1);
                if (lastIndex != act1.length()) {
                    key += "." + index;
                }
            }
            return key;
        }
    }
}
