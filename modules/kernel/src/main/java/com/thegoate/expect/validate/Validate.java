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
package com.thegoate.expect.validate;

import com.thegoate.Goate;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.staff.Employee;
import com.thegoate.utils.compare.Compare;
import com.thegoate.utils.compare.CompareUtil;
import com.thegoate.utils.compare.CompareUtility;
import com.thegoate.utils.get.Get;
import com.thegoate.utils.get.NotFound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Eric Angeli on 3/20/2019.
 */
public abstract class Validate extends Thread {
    protected BleatBox LOG = BleatFactory.getLogger(getClass());
    protected static BleatBox sLOG = BleatFactory.getLogger(Validate.class);
    protected String starReplacement = "_s#T$a%R_";
    protected String operator = "";
    protected Goate exp;
    protected Object from;
    protected Object fromExpected;
    protected Object fromExpectedResult = null;
    protected Goate data;
    protected String key;
    protected Object rtrn;
    protected boolean result = false;
    protected long period;
    private volatile boolean running = true;
    int patternIndex = 0;
    int checkIndex = 1;
    volatile List<Goate> fails = Collections.synchronizedList(new ArrayList<>());
    volatile List<Goate> passes = Collections.synchronizedList(new ArrayList<>());
    volatile List<Goate> notExecuted = Collections.synchronizedList(new ArrayList<>());

    public String operator() {
        return this.operator;
    }

    //The following should be implemented in each subclass of Validate.
    public static Validate using(String operator) {
        Validate vlad = new ValidateGoate();
        return vlad.setOperator(operator);
    }

    public static Validate using(CompareUtility compare) {
        return using(compare.getClass());
    }

    public static Validate using(Class compare) {
        CompareUtil op = getOp(compare);
        return using(op.operator());
    }
    //the above should be implemented in each subclass of validate to build the correct type.

    public static Validate lookUpGeneric(Goate exp, String key, Object from, Object fromExpected, Object rtrn, long cachePeriod, Goate data) {
        Validate vlad;
        String operator = "" + exp.get("operator");
        if (rtrn instanceof Goate) {
            if(operator.equalsIgnoreCase("isPresent")){
                vlad = new ValidatePresence(exp, key, from, fromExpected, rtrn, cachePeriod, data);
            } else {
                vlad = new ValidateGoate(exp, key, from, fromExpected, rtrn, cachePeriod, data);
            }
        } else {
            vlad = new ValidateNotGoate(exp, key, from, fromExpected, rtrn, cachePeriod, data);
        }
        return vlad
                .setExp(exp)
                .setKey(key)
                .setRtrn(rtrn)
                .setPeriod(cachePeriod)
                .setData(data)
                .setOperator(operator);
    }

    public static CompareUtil getOp(Class compare) {
        CompareUtil op = ((CompareUtil) compare.getAnnotation(CompareUtil.class));
        if (op == null) {
            sLOG.error("Validate", "The comparator class was not annotated with @CompareUtil");
        }
        return op;
    }

    public Validate setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public Validate setOperator(CompareUtility compare) {
        return setOperator(compare.getClass());
    }

    public Validate setOperator(Class compare) {
        CompareUtil op = getOp(compare);
        return setOperator(op.operator());
    }

    public List<Goate> getFails() {
        return fails;
    }

    public List<Goate> getPasses() {
        return passes;
    }

    public String name() {
        return getKey();
    }

    public Validate() {
    }

    public Validate(Goate exp, String key, Object from, Object fromExpected, Object rtrn, long cachePeriod, Goate data) {
        this.setExp(exp);
        this.setKey(key);
        this.setFrom(from);
        this.setFromExpected(fromExpected);
        this.setRtrn(rtrn);
        this.setData(data);
        this.setPeriod(cachePeriod);
    }

    public boolean running() {
        return running;
    }

    public void run() {
        running = true;
        try {
            result = true;
            if (!check(getExp(), getKey(), (Goate) getRtrn())) {
                result = false;
                if (getKey().contains("*")) {
                    fails.add(getExp());
                }
            }
        } catch (Throwable t) {
            fails.add(getExp());
        } finally {
            running = false;
        }
    }

    public boolean result() {
        return this.result;
    }

    protected List<String> buildIndexChecks(String pattern) {
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
            if (indexRightSquare > firstIndexStar) {
                String[] i = act1.substring(firstIndexStar + 1, indexRightSquare).split(",");
                long size = Long.parseLong(i[0]);
                if (i.length == 2) {
                    size += Long.parseLong(i[1]);
                }
                ic[patternIndex] = base.substring(0, base.length() - 1) + starReplacement + act1.substring(indexRightSquare + 1);
                ic[checkIndex] = base + "0-9]+" + act1.substring(indexRightSquare + 1).replaceAll("\\[*\\*[0-9]*,*[0-9]*\\]*", "*").replaceAll("\\+", "*") + ",==," + size;
            } else {
                ic[patternIndex] = base + starReplacement + act1.substring(firstIndexStar + 1);
            }
        } else {
            ic[patternIndex] = base + starReplacement + act1.substring(firstIndexStar + 1);
        }
        return ic;
    }

    protected abstract boolean check(Goate exp, String key, Goate fromData);

    /**
     * this is where the comparison actually takes place.
     * This is a good place for a breakpoint when trying to debug a comparator.
     *
     * @param val  The actual value
     * @param expV The expected value
     * @param ev   The expecation definition
     * @return True if condition met, otherwise false
     */
    protected boolean compare(Object val, Object expV, Goate ev) {
        boolean result = true;
        CompareUtility compare = new Compare(val).to(expV).using(ev.get("operator"));
        compare.setData(getData());
        if (!(compare.evaluate())) {//step into here to debug the comparator
            fails.add(ev);
            result = false;
        } else {
            passes.add(ev);
        }
        Goate health = compare.healthCheck();
        if (health.size() > 0) {
            ev.put("health check", health);
        }
        return result;
    }

    protected boolean checkEvaluated(List<Goate> passed, String act1, int index) {
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

    protected boolean checkAct1(String act1, Object from, int index) {
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

    protected Object getActualValue(Object data, String key) {
        String[] keys = key.split(">");
        Object act = data;
        for (String k : keys) {
            act = new Get(k).from(act);
        }
        return act;
    }

    protected Object getExpectedValue(Object exp, String act1) {
        if (("" + exp).contains("%")&&getFromExpected()!=null) {
            exp = calculateKey(exp, act1);
        }
        Object result = exp;
        if (getFromExpected() != null) {
            if (fromExpectedResult == null) {
                if (getFromExpected() instanceof Employee) {
                    fromExpectedResult = ((Employee) getFromExpected()).defaultPeriod(getPeriod()).syncWork();
                } else {
                    fromExpectedResult = getFromExpected();
                }
            }
            result = new Get(exp).from(fromExpectedResult);
        } else {
            result = new Goate().get("result", result);
        }
        return result;
    }

    public Goate getExp() {
        return exp;
    }

    public Validate setExp(Goate exp) {
        this.exp = exp;
        return this;
    }

    public Object getFrom() {
        return from;
    }

    public Validate setFrom(Object from) {
        this.from = from;
        return this;
    }

    public Object getFromExpected() {
        return fromExpected;
    }

    public Validate setFromExpected(Object fromExpected) {
        this.fromExpected = fromExpected;
        return this;
    }

    public Goate getData() {
        return data;
    }

    public Validate setData(Goate data) {
        this.data = data;
        return this;
    }

    public String getKey() {
        return key;
    }

    public Validate setKey(String key) {
        this.key = key;
        return this;
    }

    public Object getRtrn() {
        return rtrn;
    }

    public Validate setRtrn(Object rtrn) {
        this.rtrn = rtrn;
        return this;
    }

    public long getPeriod() {
        return period;
    }

    public Validate setPeriod(long period) {
        this.period = period;
        return this;
    }
}
