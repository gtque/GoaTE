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
package com.thegoate.xml;

import com.thegoate.Goate;
import com.thegoate.testng.TestNGEngineMethodDL;
import com.thegoate.utils.get.GetFileAsString;
import com.thegoate.xml.utils.get.GetXmlField;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by Eric Angeli on 2/9/2018.
 */
public class XmlTests extends TestNGEngineMethodDL {

    public XmlTests(){
        super();
    }

    public XmlTests(Goate data){
        super(data);
    }

    @Test(groups = {"unit", "xml"})
    public void getFromXml(){
        GetXmlField xml = new GetXmlField("howdy.0.field2.1.field2inner.1");
        String xmlDoc = ""+new GetFileAsString("example.xml").from("file::");
        assertEquals(xml.from(xmlDoc),"!");
        assertEquals(xml.get("howdy.0.field1.0"),"hello");
    }
}
