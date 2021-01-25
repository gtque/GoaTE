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
import com.thegoate.utils.compare.CompareUtil;
import com.thegoate.utils.compare.CompareUtility;
import com.thegoate.utils.get.Get;
import com.thegoate.utils.get.NotFound;

import static com.thegoate.expect.Expectation.EmployeeWorkResult;

/**
 * Validators don't care about type.
 * There must be at least one comparator for each operator the Validator supports.
 * Created by Eric Angeli on 3/20/2019.
 */
@Validator(operators = {""})
public class ValidateNotGoate extends Validate {

    boolean resultC = true;

    public ValidateNotGoate() {
        super();
    }

    public ValidateNotGoate(Goate exp, String key, Object from, Object fromExpected, Object rtrn, long cachePeriod, Goate data) {
        super(exp, key, from, fromExpected, rtrn, cachePeriod, data);
    }

    public static Validate using(String operator) {
        Validate vlad = new ValidateNotGoate();
        return vlad.setOperator(operator);
    }

    public static Validate using(CompareUtility compare) {
        return using(compare.getClass());
    }

    public static Validate using(Class compare) {
        CompareUtil op = getOp(compare);
        return using(op.operator());
    }

    public Object getRtrn() {
        return new Goate().put("return", rtrn);
    }

    protected boolean check(Goate exp, String key, Goate rtrn) {
        boolean result = true;
        try {
            if (!check2(exp, key, rtrn.get("return"), 0)) {
                result = false;
                resultC = false;
            }
        } catch (Throwable t) {
            if (key.contains("*")||key.contains("+")) {
                if (!resultC) {
                    result = false;
                    fails.add(exp);
                }
            } else {
                result = false;
            }
        }
        return result;
    }

    private boolean check2(Goate exp, String key, Object rtrn, int indexLevel) {
        exp = new Goate().merge(exp, true);
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
            String act2 = "fiddly bit";
            try {
                boolean running = true;
                while (running) {
                    act2 = act1.replaceFirst("\\*", "" + index);//act.substring(0, star) + index + act.substring(star + 1);
                    String keyt = key.replace(act, act2);
                    expt.put("actual", act2);
                    if (expt.get("expected") instanceof String) {
                        expt.put("expected", expt.get("expected", "", String.class).replaceAll("\\\\%", "_mwi_").replaceAll("\\%", "" + index).replaceAll("_mwi_", "%"));
                    }
                    boolean check = check2(expt, keyt, rtrn, indexLevel+1);
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
                    if (indexLevel == 0) {
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
//                if(!parentFound) {
                if(indexLevel>0&&index==0) {
                    throw t;
                }
//                }
//                    }

            }
        } else {
            if (exp.get("actual") instanceof String && (((String) exp.get("actual")).equalsIgnoreCase("return") || ((String) exp.get("actual")).equalsIgnoreCase(EmployeeWorkResult))) {
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
                Object expV = getExpectedValue(exp.getStrict("expected"), "" + exp.get("actual"));
                exp.put("expected_value", expV);
//                    LOG.info("evaluating \"" + fullName() + "\": " + exp.get("actual") + "(" + val + ") " + exp.get("operator") + (exp.get("expected") == null ? "" : " " + exp.get("expected")));
                LOG.debug(new StringBuilder("evaluating \"").append(exp.get("actual")).append("(").append(val).append(") ").append(exp.get("operator")).append((exp.get("expected") == null ? "" : " " + exp.get("expected"))).append("\n").toString());
                result = compare(val, expV, exp);
            }
        }
        return result;
    }
}
