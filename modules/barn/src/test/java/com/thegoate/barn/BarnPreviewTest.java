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
package com.thegoate.barn;

import com.thegoate.Goate;
import com.thegoate.barn.staff.BarnPreviewEmployee;
import com.thegoate.utils.GoateUtils;
import com.thegoate.utils.get.Get;
import com.thegoate.utils.togoate.ToGoate;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by Eric Angeli on 4/18/2018.
 */
public class BarnPreviewTest {
    @Test(groups = {"unit"})
    public void barnPreview(){
        Goate d = new Goate().put("barn expected",new Get("testcases/apis/simple.json").from("file::")).put("barn root","testcases/apis");
        BarnPreviewEmployee bpe = new BarnPreviewEmployee();
        String preview = "" + bpe.init(d).work();
        System.out.println(preview);
        String expected = "simple,extendsarray,base";
        assertEquals(new ToGoate(preview).convert().get("groups"), expected);
    }

    @Test(groups = {"unit"})
    public void barnJobPreview(){
        Goate d = new Goate().put("barn expected",new Get("testcases/apis/simplejob.json").from("file::")).put("barn root","testcases/apis");
        BarnPreviewEmployee bpe = new BarnPreviewEmployee();
        String preview = "" + bpe.init(d).work();
        System.out.println(preview);
        String expected = "hello_world";
        assertEquals(new ToGoate(preview).convert().get("job"), expected);
    }
}
