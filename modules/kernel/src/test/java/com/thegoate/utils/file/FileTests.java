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

package com.thegoate.utils.file;

import com.thegoate.utils.get.Get;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Simple tests of the file utilities.
 * Created by Eric Angeli on 5/11/2017.
 */
public class FileTests {
    @Test(groups = {"unit"})
    public void appendToFile(){
        new Append().line("howdy!").to("temp/test.txt");
        String howdy = ""+new Get("temp/test.txt").from("file::");
        howdy = howdy.replace("\n","\r").replace("\r","");
        assertEquals(howdy, "howdy!");
    }

    @Test(groups = {"unit"}, dependsOnMethods = {"appendToFile"})
    public void copyFile() throws IOException {
        String unique = ""+System.nanoTime();
        new Append().line("blargle!").to("temp/test"+unique+".txt");
        new Copy("temp/test.txt").to("temp/test"+unique+".txt");
        new Copy(new File("temp/test.txt").toURI().toURL().openStream()).to("temp/test"+unique+"b.txt");
        new Append().line("doody!").to("temp/test"+unique+".txt", true);
        String howdy = ""+new Get("temp/test"+unique+".txt").from("file::");
        howdy = howdy.replace("\n","\r").replace("\r","");
        assertEquals(howdy, "doody!");
        File f1a = new File("temp/test"+unique+"b.txt");
        assertTrue(f1a.exists());
    }

    @Test(groups = {"unit"}, dependsOnMethods = {"appendToFile"})
    public void deleteFile() throws MalformedURLException {
        String unique = ""+System.nanoTime();
        File f0 = new File("temp/test.txt");
        assertEquals((""+new Get(f0).from("file::")).replace("\n","\r").replace("\r",""),"howdy!");
        new Copy(f0).to("temp/test"+unique+".txt");
        new Copy().file(new File("temp/test.txt").toURI().toURL()).toDir("temp/temp");
        new Append().line("doody!").to("temp/test"+unique+".txt");
        String howdy = ""+new Get("temp/test"+unique+".txt").from("file::");
        howdy = howdy.replace("\n","\r").replace("\r","");
        assertEquals(howdy, "howdy!doody!");
        File f1 = new File("temp/test"+unique+".txt");
        assertTrue(f1.exists());
        File f1a = new File("temp/temp/test.txt");
        assertTrue(f1a.exists());
        new Delete().rm("temp/test"+unique+".txt");
        File f = new File("temp/test"+unique+".txt");
        new Delete().rm("temp/temp");
        assertFalse(f.exists());
        File f2a = new File("temp/temp/test.txt");
        assertFalse(f2a.exists());
    }

    @BeforeSuite(alwaysRun = true)
    @AfterSuite(alwaysRun = true)
    public void cleanUp(){
        new Delete().rm("temp");
    }
}
