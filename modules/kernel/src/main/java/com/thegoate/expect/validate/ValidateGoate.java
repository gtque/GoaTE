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
import com.thegoate.utils.get.NotFound;

import java.util.List;

import static com.thegoate.expect.Expectation.EmployeeWorkResult;

/**
 * Validators don't care about type.
 * There must be at least one comparator for each operator the Validator supports.
 * Created by Eric Angeli on 3/20/2019.
 */
@Validator(operators = {""})
public class ValidateGoate extends Validate{
    protected boolean doNotSkipIndexCheck = true;
    public ValidateGoate(){super();}
    public ValidateGoate(Goate exp, String key, Object from, Object fromExpected, Object rtrn, long cachePeriod, Goate data) {
        super(exp, key, from, fromExpected, rtrn, cachePeriod, data);
    }

    public static Validate using(String operator){
        Validate vlad = new ValidateGoate();
        return vlad.setOperator(operator);
    }

    public static Validate using(CompareUtility compare){
        return using(compare.getClass());
    }

    public static Validate using(Class compare){
        CompareUtil op = getOp(compare);
        return using(op.operator());
    }

    protected boolean check(Goate exp, String key, Goate fromData){
        boolean result = true;
//            Object val = null;
        String filterPattern = "" + exp.get("actual");
        String ofp = filterPattern;
        int extraIndex = filterPattern.indexOf(">");
        String filterExtra = "";
        if (extraIndex > 0) {
            filterExtra = filterPattern.substring(extraIndex);
            filterPattern = filterPattern.substring(0, extraIndex);
        }

        filterPattern = filterPattern.replaceAll("\\[\\*[0-9]*,*[0-9]\\]", "*").replace("+", "*").replace("*", "[0-9]*").replace("([0-9])[0-9]", "([0-9])").replace(".", "\\.").replace("\\\\.", "\\.");
        Goate filteredData = fromData.filterStrict(filterPattern);
        if (exp.get("operator").equals("isPresent")) {
            LOG.info("Validate", "It is strongly encourage that you use the ValidatePresence validator when checking isPresent.");
            if (filteredData.size() == 0) {
                filteredData.put("" + exp.get("actual"), new NotFound(filterPattern));
            }
        }
        if (filteredData.size() == 0) {
            if (exp.get("actual") instanceof String && (((String) exp.get("actual")).equalsIgnoreCase("return") || ((String) exp.get("actual")).equalsIgnoreCase(EmployeeWorkResult))) {
                filteredData.put("" + exp.get("actual"), fromData);
            } else {
                if(!ofp.contains("+") && !(ofp.contains("*"))) {
                    filteredData.put("" + exp.get("actual"), new NotFound("" + exp.get("actual")));
                }
            }
        }
        if(!evalComparisons(filteredData, filterExtra)){
            result = false;
        }
        if(doNotSkipIndexCheck) {
            if (!evalWildcardIndex(ofp, fromData)) {
                result = false;
            }
        }
        return result;
    }

    protected boolean evalComparisons(Goate filteredData, String filterExtra){
        boolean result = true;
        for (String vkey : filteredData.keys()) {
            Goate ev = new Goate().merge(getExp(), true);
            ev.put("actual", vkey + filterExtra);
            Object val = getActualValue(filteredData, vkey + filterExtra);//.get(vkey);
            ev.put("actual_value", val);
            Object expV = getExpectedValue(ev.get("expected"), vkey);
            ev.put("expected_value", expV);
            LOG.debug("Expect", new StringBuilder("evaluating:").append(vkey).append("(").append(val).append(") ").append(ev.get("operator")).append((ev.get("expected") == null ? "" : " " + ev.get("expected"))).append("\n").toString());
            if (!compare(val, expV, ev)) {
                result = false;
            }
        }
        return result;
    }
    protected boolean evalWildcardIndex(String filterPattern, Goate fromData) {
        List<String> indexChecks = buildIndexChecks(filterPattern);
        boolean result = true;
        for (String checks : indexChecks) {
            String[] check = checks.split(",");
            Goate cev = new Goate();
            String og_check = check[0].replace(starReplacement, "*").replace(".", "\\.").replace(plusReplacement, "+");
            check[0] = check[0].replace(starReplacement, "*").replace(".", "\\.").replace(plusReplacement, "*");
            cev.put("actual", check[0] + ".size()");
            cev.put("operator", check[1]);
            cev.put("expected", check[2]);
            String[] plusCheck = check[0].split("\\[0-9\\]\\+");
            String plusNext = plusCheck.length > 1 ? plusCheck[1] : "";
            if (plusNext.startsWith(".")) {
                plusNext = plusNext.substring(1);
            }
            if (plusNext.startsWith("\\\\.")) {
                plusNext = plusNext.replaceFirst("\\\\\\\\.", "");
            }
            if (plusNext.startsWith("\\.")) {
                plusNext = plusNext.replaceFirst("\\\\\\.", "");
            }
            String[] pnc = plusNext.split("\\\\.");
            if (check[1].equals("==")) {
                Goate subset = fromData.filterStrict((plusCheck[0] + "[0-9]+").replace("*", "[0-9]*").replace("\\\\","\\").replace("([0-9])[0-9]","([0-9])").replace("\\.[0-9]+", ""));
                //find expected total size
                int totalSize = Integer.parseInt("" + check[2]) * (subset.size() > 0 ? subset.size() : 1);
                //find actual total size
                int actualSize = fromData.filterStrict((plusCheck[0] + "[0-9]+").replace("*", "[0-9]*").replace("*", "[0-9]*").replace("\\\\","\\").replace("([0-9])[0-9]","([0-9])")).size();
                cev.put("actual_value", actualSize)
                        .put("expected_value", totalSize);
                if (!compare(actualSize, totalSize, cev)) {
                    result = false;
                }
            } else {
                //find how many are present
                Goate plusFirstFilter = fromData.filterStrict((plusCheck[0] + "[0-9]+" + (plusCheck.length > 1 ? "\\." + pnc[0] : "")).replace("*", "[0-9]*").replace("\\\\","\\").replace("([0-9])[0-9]","([0-9])"));
                //find how many should be present
                int plusSecondFilterSize = calculatedMinimumSize(fromData, plusCheck, og_check);//plusSecondFilter.size()==0?1:plusSecondFilter.size();
                cev.put("actual_value", plusFirstFilter.size())
                        .put("expected_value", plusSecondFilterSize);
                if (!compare(plusFirstFilter.size(), plusSecondFilterSize, cev)) {//fromData.filterStrict(check[0].replace("*", "[0-9]*")).size(), i, cev)) {
                    result = false;
                }
            }
        }
        return result;
    }

    protected int calculatedMinimumSize(Goate fromData, String[] plusCheck, String check){
        int minSize = 1;
        Goate plusSecondFilter = fromData.filterStrict((plusCheck[0] + "[0-9]+").replace("*", "[0-9]*").replace("\\\\","\\").replace("([0-9])[0-9]","([0-9])"));
        if(starBefore(check)){
            minSize = plusSecondFilter.size();//fromData.filterStrict((plusCheck[0].substring(0,plusCheck[0].length()-2)).replace("*", "[0-9]*").replace("\\\\","\\").replace("([0-9])[0-9]","([0-9])")).size();
        } else {
            if (plusSecondFilter.size() == 0) {
                minSize = check.split("\\+").length;
            } else {
                minSize = plusSecondFilter.size();
            }
        }
        return minSize;
    }


}
