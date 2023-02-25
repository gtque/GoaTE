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

import com.thegoate.data.DLProvider;
import com.thegoate.data.GoateDLP;
import com.thegoate.data.StaticDL;
import com.thegoate.testng.pojos.SimplePojo;

/**
 * Data provider for testing of named parameters with a hidden "Scenario" field.
 * Created by Eric Angeli on 5/12/2017.
 */
@GoateDLP(name = "hidden scenario with named test parameters")
public class NamedParametersHiddenScenarioDLP extends DLProvider {
    @Override
    public void init() {
        //String greeting, int number, boolean flag, SimplePojo pojo
        runData.put("dl##", new StaticDL().add("greeting", "howdy").add("number", 42).add("flag", true).add("Scenario", "this really worked."));
        runData.put("dl##", new StaticDL().add("pleasentries", "howdy").add("fubar", 42).add("flag", "true").add("Scenario", "I am the second run."));
        runData.put("dl##", new StaticDL().add("pleasentries", "howdy").add("fubar", 42).add("flag", "true").add("Scenario", "I am the third run."));
        runData.put("dl##", new StaticDL().add("pleasentries", "howdy").add("doda", 42).add("flag", "true").add("Scenario", "I am the fourth run."));
        runData.put("dl##", new StaticDL().add("pleasentries", "howdy").add("fubar", 42).add("flag", "true").add("Scenario", "I am the fifth run."));
        runData.put("dl##", new StaticDL().add("pleasentries", "howdy").add("fubar", 42).add("flag", "true").add("Scenario", "I am the sixth run."));
        runData.put("dl##", new StaticDL().add("pleasentries", "howdy").add("doda", 42).add("flag", "true").add("Scenario", "I am the seventh run."));
        runData.put("dl##", new StaticDL().add("pleasentries", "howdy").add("fubar", 42).add("flag", "true").add("Scenario", "I am the eighth run."));
        runData.put("dl##", new StaticDL().add("pleasentries", "howdy").add("fubar", 42).add("flag", "true").add("Scenario", "I am the ninth run."));
        runData.put("dl##", new StaticDL().add("pleasentries", "howdy").add("fubar", 42).add("flag", "true").add("Scenario", "I am the tenth run."));
        runData.put("dl##", new StaticDL().add("pleasentries", "howdy").add("fubar", 42).add("flag", "true").add("Scenario", "I am the eleventh run."));
        runData.put("dl##", new StaticDL().add("pleasentries", "howdy").add("doda", 42).add("fubar", 84).add("flag", "true").add("Scenario", "I am the twelfth run, no fubar."));
        runData.put("dl##", new StaticDL().add("pleasentries", "howdy").add("doda", 42).add("flag", "true").add("Scenario", "I am the thirteenth run."));

        constantData.put("dl##", new StaticDL().add("pojo",new SimplePojo().setFieldA("sploosh")).add("hidden", "o::Scenario"));
    }
}
