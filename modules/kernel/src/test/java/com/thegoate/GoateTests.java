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

package com.thegoate;

import com.thegoate.data.DataLoader;
import com.thegoate.data.PropertyFileDL;
import com.thegoate.data.StaticDL;
import com.thegoate.utils.GoateUtils;
import com.thegoate.utils.get.Get;
import com.thegoate.utils.togoate.ToGoate;
import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.assertEquals;

/**
 * Created by gtque on 4/21/2017.
 */
public class GoateTests {

    @Test(groups = {"unit"})
    public void addAutoIncrement(){
        Goate data = new Goate().put("test##", "a").put("atest", "nanotime::").put("test2","c").put("test##", "b");
        assertEquals(data.size(), 3);
        assertEquals(data.get("test2"),"b");
    }
    @Test(groups = {"unit"})
    public void staticDataLoader(){
        DataLoader dl = new StaticDL().add("test##", "a").add("atest", "nanotime::").add("test2","c").add("test##", "b");
        Goate data = dl.load().get(0);
        assertEquals(data.size(), 3);
        assertEquals(data.get("test2"),"b");
    }

    @Test(groups = {"unit"})
    public void propertyFileDataLoaderStringPath(){
        DataLoader dl = new PropertyFileDL().file("sample.prop");
        Goate data = dl.load().get(0);

        assertEquals(data.size(), 5);
        assertEquals(data.get("test3"),"d");
    }

    @Test(groups = {"unit"})
    public void propertyFileDataLoaderFile(){
        System.out.println(System.currentTimeMillis());
        DataLoader dl = new PropertyFileDL().file(new File(GoateUtils.getFilePath("sample.prop")));
        Goate data = dl.load().get(0);

        assertEquals(data.size(), 5);
        assertEquals(data.get("test3"),"d");
    }
}
