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
package com.thegoate.staff;

import com.thegoate.Goate;
import com.thegoate.annotations.GoateDescription;
import com.thegoate.expect.ExpectEvaluator;
import com.thegoate.expect.Expectation;
import com.thegoate.expect.ExpectationThreadBuilder;
import com.thegoate.utils.togoate.ToGoate;

/**
 * Runs the job until either the timeout has lapsed
 * or the condition(s) are met.
 * Created by Eric Angeli on 9/7/2018.
 */
@GoateJob(jobs = {"work until"})
@GoateDescription(description = "Run the defined job until the condition is met or it timesout. The default time out is 15 seconds.",
        parameters = {"timeout = the timeout duration in milliseconds, defaults to 15000, set a negative time to enver timeout.",
                "period = the period in milliseconds at which to re-run the job, defaults to 1000",
                "work = the definition of the job to execute",
                "return last = true/false to always return the last result, if false returns null, defaults to false",
                "expect = the array, [], of conditions/expectations that should be met to consider the job completed.",
                "timeout_expect = the timeout in milliseconds for the expected conditions to be checked, defaults to 50mS, optional and not usually needed",
                "period_expect = the period in milliseconds at which to check the expected condtions, defaults to 50mS, optional and not usually needed"})
public class WorkUntilConditionMet extends Employee {

    private Employee actualJob;
    private Long timeout = 15000L;
    private Long period = 1000L;
    private boolean lastReturn = false;

    public WorkUntilConditionMet returnLast(){
        lastReturn = true;
        definition.put("return last", lastReturn);
        return this;
    }

    public WorkUntilConditionMet job(String job){
        return setJob(job);
    }

    public WorkUntilConditionMet job(Employee job){
        return setJob(job);
    }

    private WorkUntilConditionMet setJob(Object job){
        Goate work = definition.get("work", new Goate(), Goate.class);
        work.put("job", job);
        return this;
    }

    public WorkUntilConditionMet expect(Expectation expectation){
        Goate expect = definition.get("expect", new Goate(), Goate.class);
        expect.put(""+expect.size(), expectation);
        return this;
    }

    public WorkUntilConditionMet expectTimeout(long timeoutMS){
        definition.put("timeout_expect", timeoutMS);
        return this;
    }

    public WorkUntilConditionMet expectPeriod(long periodMS){
        definition.put("period_expect", periodMS);
        return this;
    }

    public WorkUntilConditionMet period(long periodMS){
        super.period(periodMS);
        definition.put("period", period);
        return this;
    }

    public WorkUntilConditionMet timeout(long timeoutMS){
        definition.put("timeout", timeoutMS);
        return this;
    }

    @Override
    public String[] detailedScrub() {
        return new String[]{"timeout", "work", "expect"};
    }

    @Override
    public Employee init() {
        Goate actualJobDefinition = new ToGoate(definition.get("work", new Goate())).convert();
        timeout = Long.parseLong("" + definition.get("timeout", timeout));
        period = Long.parseLong("" + definition.get("period", period));
        if(actualJobDefinition.get("job") instanceof Employee){
            actualJob = (Employee)actualJobDefinition.get("job");
        } else {
            actualJob = recruit("" + actualJobDefinition.get("job"), actualJobDefinition, data);
        }
        lastReturn = Boolean.parseBoolean(""+definition.get("return last", lastReturn));
        return this;
    }

    @Override
    protected Object doWork() {
        Object result = null;
        boolean conditionsMet = false;
        if (actualJob != null) {
            Long startTime = System.currentTimeMillis();
            while (!conditionsMet && (System.currentTimeMillis() - startTime) < timeout) {
                result = actualJob.work();
                if (checkConditions(result)) {
                    conditionsMet = true;
                } else {
                    try{
                        Thread.sleep(period);
                    } catch (InterruptedException e) {
                        LOG.debug("Work Until Condition", "Problem sleeping: " + e.getMessage(), e);
                    }
                }
            }
        }
        if(!conditionsMet){
            LOG.debug("Work Until Condition", "Last work result:\n" + result);
            if(!lastReturn) {
                result = null;
            }
        }
        return result;
    }

    private boolean checkConditions(Object check) {
        Object conditions = definition.get("expect", null);
        boolean met = conditions == null;
        if (!met) {
            met = evaluate(new ToGoate(conditions).convert().put("_goate_result", check));
            if (met) {
                LOG.debug("Work Until Condition", "Condition has been met, stopping execution");
            } else {
                LOG.debug("Work Until Condition", "Condition has not been met, execution will continue unless timedout:\n" + conditions.toString());
            }
        }
        return met;
    }

    protected boolean evaluate(Goate conditions) {
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(new Goate().put("parent", definition).merge(data, false).put("_goate_result", conditions.get("_goate_result")));
        etb.expect(conditions)
                .timeout(Long.parseLong("" + definition.get("timeout_expect", 50L)))
                .period(Long.parseLong("" + definition.get("period_expect", 50L)));
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        return ev.evaluate();
    }
}
