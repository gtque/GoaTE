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
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Created by Eric Angeli on 5/11/2017.
 */
public class TestNGEngineDataProviderMethodLevel extends TestNGEngineMethodDL {

    public TestNGEngineDataProviderMethodLevel(){
        super();
    }

    @GoateProvider(name = "sample")
    @Test(groups = {"unit"}, dataProvider = "methodLoader")
    public void putRunData(Goate d) throws Exception {
        LOG.info("data size: " + data.size());
        LOG.info("data: " + data.toString());
        assertEquals(data.size(), 3);
        assertEquals(get("b","z"),"y");
        assertEquals(get("a","y"),"x");
        //put("c", 3);
        assertEquals(get("c",3),3);
        assertEquals(data.size(), 4);
    }

    @GoateProvider(name = "com.thegoate.testng.test.SampleDLP")
    @Test(groups = {"unit"}, dataProvider = "methodLoader")
    public void putRunData2(Goate d) throws Exception {
        LOG.info("data size: " + data.size());
        LOG.info("data: " + data.toString());
        assertEquals(data.size(), 3);
        assertEquals(get("b"),"y");
        assertEquals(get("a"),"x");
        put("c", 3);
        assertEquals(get("c"),3);
        assertEquals(data.size(), 4);
    }

    public int removeDuplicates(int[] nums)
    {
        int size = 0;
        int lastNum = 0;
        for(int index = 0; index < nums.length; index++){
            if(size == 0 || lastNum!=nums[index]){
                lastNum = nums[index];
                nums[size] = lastNum;
                size++;
            }
        }
        return size;
    }

    @Test
    public void testDup(){
        int[] ia = {0,0,1,1,1,2,2,3,3,4};
        List<String> list = new ArrayList<>();
        list.add(0, "hello");
        int size = removeDuplicates(ia);
        assertEquals(size, 5);
    }
}
