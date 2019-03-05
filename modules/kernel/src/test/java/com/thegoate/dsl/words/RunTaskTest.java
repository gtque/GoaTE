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

package com.thegoate.dsl.words;

import com.thegoate.Goate;

import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.fail;

/**
 * Tests the dsl for getting the current system nano time.
 * Created by gtque on 4/21/2017.
 */
public class RunTaskTest {
    protected final BleatBox LOG = BleatFactory.getLogger(getClass());
    @Test(groups = {"unit"})
    public void runHello(){
        Goate data = new Goate();
        data.put("test", "do::say hello");
        String nt = "" + data.get("test");
        LOG.debug("output: " + nt);
        Assert.assertEquals(nt, "hello");
    }

    @Test(groups = {"unit"})
    public void runHowdy(){
        Goate data = new Goate();
        data.put("test", "do::say howdy,o::var1").put("var1","Matt");
        String nt = "" + data.get("test");
        LOG.debug("output: " + nt);
        Assert.assertEquals(nt, "howdy Matt");
    }

    @Test(groups = {"unit"})
    public void runMultipleAge(){
        Goate data = new Goate();
        data.put("test", "do::multiply age by ${x},o::var1").put("var1",10).put("x",2);
        String nt = "" + data.get("test");
        LOG.debug("output: " + nt);
        Assert.assertEquals(nt, "20");
    }

    @Test(groups = {"unit"})
    public void runMultiple3(){
        Goate data = new Goate();
        data.put("test", "do::multiply three numbers constructor times ${x} times ${y},o::constructorValue,o::paul")
                .put("paul", 100)
                .put("constructorValue",10).put("x",2).put("y",3);
        String nt = "" + data.get("test");
        LOG.debug("output: " + nt);
        Assert.assertEquals(nt, "60");
    }

    @Test(groups = {"unit"})
    public void runGoodbye(){
        Goate data = new Goate();
        data.put("test", "do::say goodbye");
        String nt = "" + data.get("test");
        LOG.debug("output: " + nt);
        Assert.assertEquals(nt, "goodbye");
    }

    @Test(groups = {"unit"})
    public void runAdd(){
        Goate data = new Goate();
        data.put("test", "do::add ${x} + ${y}").put("x", 1).put("y", "int::2");
        int nt = (int)data.get("test");
        LOG.debug("output: " + nt);
        Assert.assertEquals(nt, 3);
    }

    @Test(groups = {"unit"})
    public void runSay(){
        Goate data = new Goate();
        data.put("test", "do::say ${word} and print ${data}").put("word", "howdy").put("data", "goate::");
        String nt = ""+data.get("test");
        LOG.debug("output: " + nt);
        Assert.assertEquals(nt, "howdy\n"+data.toString());
    }

    @Test(groups = {"unit"})
    public void runVoidBoolean(){
        Goate data = new Goate();
        data.put("test", "do::void ${truth}").put("truth", "boolean::true").put("data", "goate::");
        String nt = ""+data.get("test");
        LOG.debug("output: " + nt);
        Assert.assertEquals(nt, "null");
    }

    @Test(groups = {"unit"})
    public void runVoidBooleanFail(){
        Goate data = new Goate();
        data.put("test", "do::void ${truth}").put("truth", "boolean::false").put("data", "goate::");
        Object nt = null;
        try {
            nt = data.get("test");
            if(nt instanceof Exception){
                throw (Exception) nt;
            }
            fail("Should have thrown an exception, instead got: " + nt);
        }catch(Exception e) {
            LOG.debug("output: " + e.getMessage());
            Assert.assertEquals(e.getMessage(), "FAIL");
        }
    }
}
