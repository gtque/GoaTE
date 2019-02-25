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
package com.thegoate.spreadsheets;

import com.thegoate.Goate;
import com.thegoate.data.DataLoader;
import com.thegoate.spreadsheets.data.SpreadSheetAbstractedDL;
import com.thegoate.spreadsheets.data.SpreadSheetDL;
import com.thegoate.spreadsheets.utils.SheetUtils;
import com.thegoate.testng.TestNGEngineMethodDL;
import com.thegoate.utils.GoateUtils;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Tests the loading of a csv file and getting cells from it.
 * Created by Eric Angeli on 9/14/2017.
 */
public class ExcelTest extends TestNGEngineMethodDL {

    @Test(groups = {"unit"})
    public void loadSimpleExcel() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        SheetUtils sheet = SheetUtils.build("sample.xlsx","Sheet1").firstRowIsNotHeader().firstRowIsHeader();
        sheet.load();
        assertEquals(sheet.get("a",0),"b");
        assertEquals(sheet.get("a",1),"FALSE");
        assertEquals(sheet.get(0,0),"b");
        assertEquals(Integer.parseInt(""+sheet.get("z",0)),42);
        assertEquals(sheet.get("z",1),"42.01");
        assertEquals(sheet.get("c",0,"e"),"e");
        assertEquals(sheet.get(2,0,3),3);
    }

    @Test(groups = {"unit"})
    public void loadSimpleExcelNoHeaders() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        SheetUtils sheet = SheetUtils.build("sample.xlsx","Sheet1").firstRowIsNotHeader();
        sheet.load();
        assertEquals(sheet.get(0,0),"a");
        assertEquals(sheet.get(1,1),"42");
        assertEquals(sheet.get("c",0,"e"),"e");
        assertEquals(sheet.get(2,0,3),3);
    }

    @Test(groups = {"unit"})
    public void ExcelSheetDLTest() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        DataLoader dl = new SpreadSheetDL().fileName("sample.xlsx").sheetName("Sheet1");
        List<Goate> dataList = dl.load();
        assertEquals(dataList.get(0).get("a"),"b");
        assertEquals(dataList.get(1).get("z"),"42.01");
        assertEquals(dataList.size(),2);
    }

    @Test(groups = {"unit"})
    public void ExcelSheetDLTestNoHeader() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        DataLoader dl = new SpreadSheetDL().fileName("sample.xlsx").sheetName("Sheet1").firstRowIsHeader(false);
        List<Goate> dataList = dl.load();
        assertEquals(dataList.get(0).get("0"),"a");
        assertEquals(dataList.get(1).get("1"),"42");
        assertEquals(dataList.size(),3);
    }

    @Test(groups = {"unit"})
    public void loadSimpleAbstractExcel() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        GoateUtils.setEnvironment("eut", ""+GoateUtils.getProperty("eut",GoateUtils.getProperty("profile", "foobar")));
        String fileName = ((SpreadSheetAbstractedDL)(new SpreadSheetAbstractedDL().fileName("scenarios/bar.json"))).getFileName();
        SheetUtils sheet = SheetUtils.build(fileName,"Sheet1").firstRowIsNotHeader().firstRowIsHeader();
        sheet.load();
        assertEquals(sheet.get("a",0),"b");
        assertEquals(sheet.get("a",1),"FALSE");
        assertEquals(sheet.get(0,0),"b");
        assertEquals(Integer.parseInt(""+sheet.get("z",0)),42);
        assertEquals(sheet.get("z",1),"42.01");
        assertEquals(sheet.get("c",0,"e"),"e");
        assertEquals(sheet.get(2,0,3),3);
    }

    @Test(groups = {"unit"})
    public void loadSimpleAbstractExcelNoHeaders() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        GoateUtils.setEnvironment("eut", ""+GoateUtils.getProperty("eut",GoateUtils.getProperty("profile", "foobar")));
        String fileName = ((SpreadSheetAbstractedDL)(new SpreadSheetAbstractedDL().fileName("scenarios/bar.json"))).getFileName();
        SheetUtils sheet = SheetUtils.build(fileName,"Sheet1").firstRowIsNotHeader();
        sheet.load();
        assertEquals(sheet.get(0,0),"a");
        assertEquals(sheet.get(1,1),"42");
        assertEquals(sheet.get("c",0,"e"),"e");
        assertEquals(sheet.get(2,0,3),3);
    }

    @Test(groups = {"unit"})
    public void ExcelSheetAbstractDLTest() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        GoateUtils.setEnvironment("eut", ""+GoateUtils.getProperty("eut",GoateUtils.getProperty("profile", "foobar")));
        DataLoader dl = new SpreadSheetAbstractedDL().fileName("scenarios/bar.json").sheetName("Sheet1");
        List<Goate> dataList = dl.load();
        assertEquals(dataList.get(0).get("a"),"b");
        assertEquals(dataList.get(1).get("z"),"42.01");
        assertEquals(dataList.size(),2);
    }

    @Test(groups = {"unit"})
    public void ExcelSheetAbstractDLTestNoHeader() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        GoateUtils.setEnvironment("eut", ""+GoateUtils.getProperty("eut",GoateUtils.getProperty("profile", "foobar")));
        DataLoader dl = new SpreadSheetAbstractedDL().fileName("scenarios/bar.json").sheetName("Sheet1").firstRowIsHeader(false);
        List<Goate> dataList = dl.load();
        assertEquals(dataList.get(0).get("0"),"a");
        assertEquals(dataList.get(1).get("1"),"42");
        assertEquals(dataList.size(),3);
    }
}
