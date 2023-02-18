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
package com.thegoate.json.staff;

import com.thegoate.Goate;
import com.thegoate.data.StaticDL;
import com.thegoate.expect.Expectation;
import com.thegoate.testng.TestNGEngineMethodDL;
import com.thegoate.utils.get.Get;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

/**
 * Created by Eric Angeli on 8/28/2018.
 */
public class GetJsonByFieldValueTest extends TestNGEngineMethodDL {
    public GetJsonByFieldValueTest() {
        super();
    }

    @Factory(dataProvider = "dataLoader")
    public GetJsonByFieldValueTest(Goate data) {
        super(data);
    }

    @Override
    public void defineDataLoaders() {
        runData
                .put("dl##", new StaticDL().add("Scenario", "find a by Goku")
                        .add(".definition", "file::find_defA.json")
                        .add("expected", "a"))
                .put("dl##", new StaticDL().add("Scenario", "find b by 42")
                        .add(".definition", "file::find_defB.json")
                        .add("expected", "b"))
                .put("dl##", new StaticDL().add("Scenario", "find c by false")
                        .add(".definition", "file::find_defC.json")
                        .add("expected", "c"))
                .put("dl##", new StaticDL().add("Scenario", "find d by pickles")
                        .add(".definition", "file::find_defD.json")
                        .add("expected", "d"))
                .put("dl##", new StaticDL().add("Scenario", "find nested by cc")
                        .add(".definition", "file::find_defD.json")
                        .add("expected", 2)
                        .add("check field", "size()"));
    }

    @Test(groups = {"unit"})
    public void findObjectInArray() {
        Object found = new GetJsonByFieldValue().init(data).work();
        String actual = "" + new Get(get("check field", "name")).from(found);
//        assertEquals(actual, ""+data.get("expected"));
        expect(Expectation.build()
                .actual(actual)
                .isEqualTo("" + data.get("expected")));
    }
}
