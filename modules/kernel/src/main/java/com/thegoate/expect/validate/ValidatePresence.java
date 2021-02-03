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

/**
 * Validators don't care about type.
 * There must be at least one comparator for each operator the Validator supports.
 * Created by Eric Angeli on 3/20/2019.
 */
@Validator(operators = {"isPresent"})
public class ValidatePresence extends ValidateGoate{
    protected boolean invert = false;
    public ValidatePresence(){super();}
    public ValidatePresence(Goate exp, String key, Object from, Object fromExpected, Object rtrn, long cachePeriod, Goate data) {
        super(exp, key, from, fromExpected, rtrn, cachePeriod, data);
    }

    public static Validate using(String operator){
        Validate vlad = new ValidatePresence();
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
        doNotSkipIndexCheck = invert?!calculatePresence():calculatePresence();
        boolean r = super.check(exp, key, fromData);
        if(!doNotSkipIndexCheck){
            if(r){
                passes.add(exp);
            } else {
                fails.add(exp);
            }
        }
        return r;
    }

    protected boolean calculatePresence(){
        boolean presence = false;
        Object expId = exp.get("expected");
        if(expId instanceof String){
            expId = (""+expId).replace("%","*");
        }
        Object expV = getExpectedValue(expId, "");
        presence = Boolean.parseBoolean(""+expV);
        if(!(""+exp.get("actual")).contains("+")){
            presence = false;
        }
        return presence;
    }
}
