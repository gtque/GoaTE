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

import com.thegoate.utils.GoateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple thread executor for evaluating expectations in a thread.
 * Created by Eric Angeli on 5/10/2017.
 */
public class ExpectThreadExecuter extends Thread {
    final Logger LOG = LoggerFactory.getLogger(getClass());
    Expectation expectation = null;
    volatile boolean status = false;
    volatile boolean running = false;
    volatile boolean executing = false;
    long timeoutMS = 60000;
    long period = 50;
    long startTime = 0;
    long endTime = 0;
    StringBuilder failed = new StringBuilder("");

    public ExpectThreadExecuter(Expectation expectation) {
        this.expectation = expectation;
    }

    public void terminate() {
        this.executing = false;
    }

    public ExpectThreadExecuter timeout(long timeoutMS) {
        this.timeoutMS = timeoutMS;
        return this;
    }

    public ExpectThreadExecuter period(long period) {
        this.period = period;
        return this;
    }

    public Expectation getExpectation(){
        return expectation;
    }
    public boolean isRunning(){
        return running;
    }

    @Override
    public void run() {
        status = false;
        if (expectation != null) {
            executing = true;
            startTime = System.currentTimeMillis();
            long current = System.currentTimeMillis();
            while (executing && !status && (current - startTime) <= timeoutMS) {
                status = expectation.evaluate();
                GoateUtils.sleep(period, LOG);
                if(Thread.interrupted()){
                    executing = false;
                }
                current = System.currentTimeMillis();
            }
            if (!status) {
                failed.append("The expectation failed or timed out.\n");
                failed.append(expectation.failed());
            }
            executing = false;
        }
        running = false;
    }

    public boolean status(){
        return this.status;
    }

    public String failedMessage(){
        return failed.toString();
    }

    @Override
    public void start(){
        running = true;
        LOG.debug("starting: " + expectation.fullName());
        try {
            super.start();
        }catch (Throwable t){
            LOG.debug("problem starting: " + expectation.fullName(), t);
            throw t;
        }
    }
}
