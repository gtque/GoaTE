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
import com.thegoate.reflection.Executioner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Maintains a simple thread pool and executes expectations in threads.
 * Created by Eric Angeli on 5/10/2017.
 */
public class ExpectEvaluator {
    final BleatBox LOG = BleatFactory.getLogger(getClass());

    List<ExpectThreadExecuter> expectations = null;
    volatile StringBuilder failed = new StringBuilder("");
    volatile List<Goate> fails = Collections.synchronizedList(new ArrayList<>());//new ArrayList<>();
    volatile List<Goate> passes = Collections.synchronizedList(new ArrayList<>());//new ArrayList<>();
    volatile List<Goate> skipped = Collections.synchronizedList(new ArrayList<>());//new ArrayList<>();
    volatile List<Goate> zeroOrMore = Collections.synchronizedList(new ArrayList<>());//new ArrayList<>();

    public ExpectEvaluator(ExpectationThreadBuilder etb){
        buildExpectations(etb);
    }

    public ExpectEvaluator(List<ExpectEvaluator> evs){
        if(evs != null && evs.size()>0) {
            for (ExpectEvaluator ev : evs) {
                failed.append("\n").append(ev.failed());
                fails.addAll(ev.fails());
                passes.addAll(ev.passes());
                skipped.addAll(ev.skipped());
                zeroOrMore.addAll(ev.zeroOrMore());
            }
        }
    }

    protected void buildExpectations(ExpectationThreadBuilder etb){
        expectations = etb.build();
    }

    public boolean evaluate() {
        return evaluate(10);
    }

    public boolean evaluate(int threadSize) {
        boolean result = true;
        failed = new StringBuilder("");
        process(threadSize);
        for(ExpectThreadExecuter expect:expectations){
            if(!expect.status()){
//                LOG.debug("Expect Evaluation", "detected a failed expectation: " + expect.getExpectation().getExpectations());
                result = false;
                LOG.info("Expect Evaluation", "detected failed expectations: " + expect.failedMessage());
                failed.append(expect.failedMessage());
                fails.addAll(expect.fails());
            }
            passes.addAll(expect.passes());
        }
        checkForSkipped();
        return result;
    }

    public List<ExpectThreadExecuter> expectations(){
        return expectations;
    }

    public List<Goate> fails(){
        return fails;
    }

    public List<Goate> passes(){
        return passes;
    }

    public List<Goate> zeroOrMore(){
        return zeroOrMore;
    }

    public List<Goate> skipped(){
        return skipped;
    }

    public String failed(){
        return failed!=null?failed.toString():"no failed messages";
    }

    protected void process(int threadSize){
        LOG.debug("starting executor to evaluate expectations.");
        if(new Executioner<ExpectThreadExecuter>(threadSize).process(expectations())){
            LOG.debug("Expectations", "Evaluation finished successfully");
        } else {
            LOG.info("Expectations", "Evaluation did not finish successfully, at least one expectation was not executed for some reason.");
            failed.append("/nNot all expectation threads were executed successfully for some reason, please review the logs.");
            fails.add(new Goate().put("actual", expectations().size()).put("health check", "failed to execute all the threads for some reason."));
        }
    }

    private void checkForSkipped(){
        for (ExpectThreadExecuter expectation : expectations()) {
            Expectation ex = expectation.getExpectation();
            Goate eval = ex.getExpectations();
            //if(eval.size()>(passes().size()+fails().size())) {
                //LOG.debug("Evaluate", "detected possible skipped expectation.");
                for (String key : eval.keys()) {
                    Goate exp = eval.get(key, null, Goate.class);
                    if (!checkInExpectationList(exp.get("actual"), exp.get("operator", null, String.class), passes())) {
                        if (!checkInExpectationList(exp.get("actual"), exp.get("operator", null, String.class), fails())) {
                            if (!("" + exp.get("actual")).contains("*") && !("" + exp.get("actual")).contains("+")) {
                                skipped.add(exp);
                            } else {
                                if (!("" + exp.get("actual")).contains("+")) {
                                    zeroOrMore.add(exp);
                                }
                            }
                        }
                    }
                }
            //}
        }
    }

    private boolean checkInExpectationList(Object actual, String operator, List<Goate> list) {
        String act = "" + actual;
        boolean result = false;
        for (Goate expectation : list) {
            if (act.equals("" + expectation.get("actual", null))) {
                if (operator.equals("" + expectation.get("operator", null, String.class))) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

}
