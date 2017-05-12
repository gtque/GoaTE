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

package com.thegoate.testng.test;

import com.thegoate.Goate;
import com.thegoate.data.StaticDL;
import com.thegoate.testng.TestNGEngine;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by Eric Angeli on 5/11/2017.
 */
public class SampleTestNGEngineTest extends TestNGEngine {

    public SampleTestNGEngineTest(){
        super();
    }

    @Factory(dataProvider = "dataLoader")
    public SampleTestNGEngineTest(Goate data){
        super(data);
    }

    @Override
    public void defineDataLoaders() {
        runData.put("dl##", new StaticDL().add("runNumber",1).add("Scenario", "first run"))
                .put("dl##", new StaticDL().add("runNumber",2))
                .put("dl##", new StaticDL().add("runNumber",3).add("Scenario", "first run"))
                .put("dl##", new StaticDL().add("runNumber",4).add("Scenario", "fourth"));
    }

}
