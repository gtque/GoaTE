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
package com.thegoate.barn.data;

import com.thegoate.Goate;
import com.thegoate.data.DataLoader;
import com.thegoate.testng.TestNGEngineMethodDL;
import com.thegoate.utils.GoateUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Test the Barn data loader
 * Created by Eric Angeli on 2/26/2018.
 */
public class TestBarnDataLoader extends TestNGEngineMethodDL {

    Object tg = null;
    Object eg = null;
    Object rg = null;

    @BeforeMethod
    public void clearGroups(){
        tg = GoateUtils.getProperty("testGroups");
        eg = GoateUtils.getProperty("excludeGroups");
        rg = GoateUtils.getProperty("runGroups");
        GoateUtils.setEnvironment("testGroups", null);
        GoateUtils.setEnvironment("excludeGroups", null);
        GoateUtils.setEnvironment("runGroups", null);
    }

    @AfterMethod
    public void resetGroups(){
        if(tg != null){
            GoateUtils.setEnvironment("testGroups", ""+tg);
        }
        if(eg != null){
            GoateUtils.setEnvironment("excludeGroups", ""+eg);
        }
        if(rg != null){
            GoateUtils.setEnvironment("runGroups", ""+rg);
        }
    }
    @Test(groups = {"unit"})
    public void nestedExtends(){
        DataLoader dl = new BarnDataLoader().testCaseDirectory("barn/data").defaultGroup("unit");
        List<Goate> list = dl.load();
        LOG.debug("size: " + list.size());
        assertEquals(list.size(),1);
        Goate d = list.get(0);
        LOG.debug("Nested Extends", d.toString());
        assertEquals(d.size(),6);
    }
}
