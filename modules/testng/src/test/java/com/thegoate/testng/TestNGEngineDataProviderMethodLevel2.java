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
package com.thegoate.testng;

import com.thegoate.Goate;
import com.thegoate.data.GoateDLP;
import com.thegoate.data.GoateProvider;
import com.thegoate.data.StaticDL;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by Eric Angeli on 5/11/2017.
 */
public class TestNGEngineDataProviderMethodLevel2 extends TestNGEngineMethodDL {

    public TestNGEngineDataProviderMethodLevel2(){
        super();
    }

    @GoateDLP(name="sample2")
    public Goate[] dlp(){
        Goate[] d = new Goate[2];
        d[0] = new Goate().put("dl##", new StaticDL().add("a","x").add("Scenario", "use method provider 1."))
        .put("dl##", new StaticDL().add("a","x").add("Scenario", "use method provider 2."));
        return d;
    }

    public Goate[] sample3(){
        Goate[] d = new Goate[2];
        d[0] = new Goate().put("dl##", new StaticDL().add("a","x").add("Scenario", "use method provider 3."));
        d[1] = new Goate().put("dl##", new StaticDL().add("b","y"));
        return d;
    }

    @GoateProvider(name = "sample2")
    @Test(groups = {"unit"}, dataProvider = "methodLoader")
    public void putRunData(Goate d) throws Exception {
        assertEquals(data.size(), 2);
        assertEquals(get("b"),null);
        assertEquals(get("a"),"x");
        put("c", 3);
        assertEquals(get("c"),3);
        assertEquals(data.size(), 3);
    }

    @GoateProvider(name = "sample3")
    @Test(groups = {"unit"}, dataProvider = "methodLoader")
    public void putRunData2(Goate d) throws Exception {
        assertEquals(data.size(), 3);
        assertEquals(get("b"),"y");
        assertEquals(get("a"),"x");
        put("c", 3);
        assertEquals(get("c"),3);
        assertEquals(data.size(), 4);
    }

}
