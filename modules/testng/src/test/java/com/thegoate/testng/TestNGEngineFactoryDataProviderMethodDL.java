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
import com.thegoate.data.GoateProvider;
import com.thegoate.data.StaticDL;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;

/**
 * Created by Eric Angeli on 5/11/2017.
 */
@GoateProvider(name = "sample")
public class TestNGEngineFactoryDataProviderMethodDL extends TestNGEngineMethodDL {

    public TestNGEngineFactoryDataProviderMethodDL(){
        super();
    }

    @Factory(dataProvider = "dataLoader")
    public TestNGEngineFactoryDataProviderMethodDL(Goate data){
        super(data);
    }

    @Test(groups = {"unit"})
    public void putRunData() throws Exception {
        assertEquals(data.size(), 3);
        assertEquals(get("b"),"y");
        assertEquals(get("a"),"x");
        put("c", 3);
        assertEquals(get("c"),3);
        assertEquals(data.size(), 4);
    }

    @Test(groups = {"unit"}, dependsOnMethods = {"putRunData"})
    public void putRunData2() throws Exception {
        assertEquals(data.size(), 4);
        assertEquals(get("b"),"y");
        assertEquals(get("a"),"x");
        System.out.println("bigdecimal: " + new BigDecimal("1,23,4.56".replace(",","")));
        put("c", 0);
        assertEquals(get("c"),0);
        assertEquals(data.size(), 4);
    }

}
