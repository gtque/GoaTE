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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Used for building the expect threads.
 * Created by Eric Angeli on 5/10/2017.
 */
public class ExpectationThreadBuilder {
    List<Expectation> expectations = new ArrayList<>();
    Goate data = null;
    long timeoutMS = 0;
    long period = 50;

    public ExpectationThreadBuilder timeout(long timeoutMS){
        this.timeoutMS = timeoutMS;
        return this;
    }

    public ExpectationThreadBuilder period(long period){
        this.period = period;
        return this;
    }

    public ExpectationThreadBuilder(Goate data){
        this.data = data;
    }

    public ExpectationThreadBuilder expect(Goate expectations){
        if(expectations!=null){
            for(String key:expectations.keys()){
                if(expectations.getStrict(key) instanceof String) {
                    expect("" + expectations.getStrict(key));
                }else if(expectations.getStrict(key) instanceof Expectation){
                    expect((Expectation)expectations.get(key));
                }
            }
        }
        return this;
    }
    
    public ExpectationThreadBuilder expect(String definition){
        return expect(new Expectation(data).define(definition));
    }

    public ExpectationThreadBuilder expect(Expectation expectation){
        expectations.add(expectation);
        return this;
    }

    public List<ExpectThreadExecuter> build(){
        Map<String, ExpectThreadExecuter> map = new ConcurrentHashMap<>();
        List<ExpectThreadExecuter> e = new ArrayList<>();
        for(Expectation expectation:expectations){
            if(map.containsKey(expectation.fullName())){
                Expectation expTemp = map.get(expectation.fullName()).getExpectation();
                expTemp.addNewExpectation().add(expectation);
            }else{
                ExpectThreadExecuter et = new ExpectThreadExecuter(expectation).timeout(timeoutMS).period(period);
                map.put(expectation.fullName(), et);
                e.add(et);
            }
        }
        return e;
    }
}
