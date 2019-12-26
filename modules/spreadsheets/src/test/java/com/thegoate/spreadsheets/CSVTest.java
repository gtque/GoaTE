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
import com.thegoate.expect.Expectation;
import com.thegoate.json.utils.togoate.JSONToGoate;
import com.thegoate.spreadsheets.data.SpreadSheetAbstractedDL;
import com.thegoate.spreadsheets.dsl.words.LoadCsv;
import com.thegoate.spreadsheets.pojo.CsvSource;
import com.thegoate.spreadsheets.pojo.MapCsvPojo;
import com.thegoate.spreadsheets.staff.GetCsvEmployee;
import com.thegoate.spreadsheets.utils.SheetUtils;
import com.thegoate.spreadsheets.utils.togoate.SpreadSheetToGoate;
import com.thegoate.testng.TestNGEngineMethodDL;
import com.thegoate.utils.GoateUtils;
import com.thegoate.utils.file.Delete;
import com.thegoate.utils.get.Get;
import org.testng.annotations.Test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.thegoate.dsl.words.LoadFile.fileAsAString;
import static com.thegoate.utils.get.Nested.nested;
import static org.testng.Assert.assertEquals;

/**
 * Tests the loading of a csv file and getting cells from it.
 * Created by Eric Angeli on 9/14/2017.
 */
public class CSVTest extends TestNGEngineMethodDL {

    @Test(groups = {"unit"})
    public void loadSimpleCSV() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        SheetUtils sheet = SheetUtils.build("sample.csv", "the sheet name does not matter.").firstRowIsNotHeader().firstRowIsHeader();
        sheet.load();
        assertEquals(sheet.get("a", 0), "b");
        assertEquals(sheet.get("a", 1), "false");
        assertEquals(sheet.get(0, 0), "b");
        assertEquals(sheet.get("c", 0, "e"), "e");
        assertEquals(sheet.get(2, 1, 3), 3);
    }

    @Test(groups = {"unit"})
    public void writeSimpleCsv() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        SheetUtils sheet = SheetUtils.build("sample.csv", "the sheet name does not matter.").firstRowIsNotHeader().firstRowIsHeader();
        sheet.load();
        int row = sheet.rowCount();
        assertEquals(sheet.get("a", 0), "b");
        assertEquals(sheet.get("a", 1), "false");
        assertEquals(sheet.get(0, 0), "b");
        assertEquals(sheet.get("c", 0, "e"), "e");
        assertEquals(sheet.get(2, 1, 3), 3);
        sheet.set("a", row, 42)
                .set("c", row, 42)
                .set("a", row+2, 42)
                .set(3, row+1, true);
        sheet.newFile("temp/sample2.csv").writeToFile();
        SheetUtils sheet2 = SheetUtils.build("temp/sample2.csv", "the sheet name does not matter.").firstRowIsNotHeader().firstRowIsHeader();
        sheet2.load();
        new Delete().rm("temp/sample2.csv");
        assertEquals(sheet2.get("c", 0), "e");
        assertEquals(sheet2.get("a", 0), "b");
        assertEquals(sheet2.get("a", 1), "false");
        assertEquals(sheet2.get(0, 0), "b");
        assertEquals(sheet2.get(2, 1), "3");
        assertEquals(sheet2.get("a", 2), "42");
        assertEquals(sheet2.get("c", 2), "42");
        assertEquals(sheet2.get(3, 3), "true");
        assertEquals(sheet2.get(3, 0), "");
        assertEquals(sheet2.get("c", 3), "");
    }

    @Test(groups = {"unit"})
    public void loadSimpleCSVNoHeaders() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        SheetUtils sheet = SheetUtils.build("sample.csv", "the sheet name does not matter.").firstRowIsNotHeader();
        sheet.load();
        assertEquals(sheet.get(0, 0), "a");
        assertEquals(sheet.get(1, 1), "42,g");
        assertEquals(sheet.get("c", 0, "e"), "e");
        assertEquals(sheet.get(2, 0, 3), 3);
    }

    @Test(groups = {"unit"})
    public void getCSVByHeader() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        if (data == null) {
            data = new Goate();
        }
        Goate def = new Goate();
        data.put(".definition", def);
        def.put("file", "sample.csv");
        GetCsvEmployee csv = new GetCsvEmployee();
        Object parser = csv.init(data).work();
        Get gp = new Get("get row#0>a");
        assertEquals(gp.from(parser), "b");
    }

    @Test(groups = {"unit"})
    public void loadCSVByHeader() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Object parser = LoadCsv.loadCsv("sample.csv", true);
        Get gp = new Get("get row#0>a");
        assertEquals(gp.from(parser), "b");
    }

    @Test(groups = {"unit"})
    public void loadSimpleAbstractCSV() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        GoateUtils.setEnvironment("eut", ""+GoateUtils.getProperty("eut",GoateUtils.getProperty("profile", "foobar")));
        String fileName = ((SpreadSheetAbstractedDL)(new SpreadSheetAbstractedDL().fileName("scenarios/foo.json"))).getFileName();
        SheetUtils sheet = SheetUtils.build(fileName,"the sheet name does not matter.").firstRowIsNotHeader().firstRowIsHeader();
        sheet.load();
        assertEquals(sheet.get("a",0),"b");
        assertEquals(sheet.get("a",1),"false");
        assertEquals(sheet.get(0,0),"b");
        assertEquals(sheet.get("c",0,"e"),"e");
        assertEquals(sheet.get(2,0,3),"e");
    }

    @Test(groups = {"unit"})
    public void loadSimpleAbstractCSVNoHeaders() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        GoateUtils.setEnvironment("eut", ""+GoateUtils.getProperty("eut",GoateUtils.getProperty("profile", "foobar")));
        String fileName = ((SpreadSheetAbstractedDL)(new SpreadSheetAbstractedDL().fileName("scenarios/foo.json"))).getFileName();
        SheetUtils sheet = SheetUtils.build(fileName,"the sheet name does not matter.").firstRowIsNotHeader();
        sheet.load();
        assertEquals(sheet.get(0,0),"a");
        assertEquals(sheet.get(1,1),"42,g");
        assertEquals(sheet.get("c",0,"e"),"e");
        assertEquals(sheet.get(2,0,3),3);
    }


    @Test(groups = {"unit"})
    public void CSVAbstractDLTest() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        GoateUtils.setEnvironment("eut", ""+GoateUtils.getProperty("eut",GoateUtils.getProperty("profile", "foobar")));
        DataLoader dl = new SpreadSheetAbstractedDL().fileName("scenarios/foo.json").firstRowIsHeader(true);
        List<Goate> dataList = dl.load();
        assertEquals(dataList.get(0).get("a"),"b");
        assertEquals(dataList.get(1).get("z"),"42,g");
        assertEquals(dataList.size(),2);
    }

    @Test(groups = {"unit"})
    public void CSVAbstractDLTestNoHeader() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        GoateUtils.setEnvironment("eut", ""+GoateUtils.getProperty("eut",GoateUtils.getProperty("profile", "foobar")));
        DataLoader dl = new SpreadSheetAbstractedDL().fileName("scenarios/foo.json").firstRowIsHeader(false);
        List<Goate> dataList = dl.load();
        assertEquals(dataList.get(0).get("0"),"a");
        assertEquals(dataList.get(1).get("1"),"42,g");
        assertEquals(dataList.size(),3);
    }

	@Test(groups = {"unit"})
    public void loadCSVByHeaderFromFile() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Object parser = LoadCsv.loadCsv(new File(GoateUtils.getFilePath("sample.csv")), true);
        Get gp = new Get(nested("get row#0", "a"));
        assertEquals(gp.from(parser), "b");
    }

    @Test(groups = {"unit"})
    public void mapCsvToJsonKeys() {
        Object parser = LoadCsv.loadCsv(new File(GoateUtils.getFilePath("map.csv")), true);
        Goate data = new SpreadSheetToGoate(parser).convert();
        Goate jdata = new JSONToGoate(fileAsAString("map.json")).convert();
        expect(Expectation.build()
                .actual(data)
                .isEqualTo(jdata));
    }

    @Test(groups = {"unit"})
    public void mapWithDifferentKeysCsvToJsonKeys() {
        Object parser = LoadCsv.loadCsv(new File(GoateUtils.getFilePath("map2.csv")), true);
        Goate data = new SpreadSheetToGoate(parser).mapTo(CSVTOJSON.class).convert();
        Goate jdata = new JSONToGoate(fileAsAString("map.json")).convert();
        expect(Expectation.build()
                .actual(data)
                .isEqualTo(jdata));
    }

    @Test(groups = {"unit"})
    public void mapWithDifferentKeysMapCsvToJsonKeys() {
        Object parser = LoadCsv.loadCsv(new File(GoateUtils.getFilePath("map2.csv")), true);
        Map<String,String> map = new HashMap<>();
        map.put("a", "nick");
        map.put("z", "paul");
        Goate data = new SpreadSheetToGoate(parser).mapTo(map).convert();
        Goate jdata = new JSONToGoate(fileAsAString("map.json")).convert();
        expect(Expectation.build()
                .actual(data)
                .isEqualTo(jdata));
    }

    @Test(groups = {"unit"})
    public void mapWithDifferentKeysPojoCsvToJsonKeys() {
        Object parser = LoadCsv.loadCsv(new File(GoateUtils.getFilePath("map2.csv")), true);
        Goate data = new SpreadSheetToGoate(parser).mapToPojo(MapCsvPojo.class, CsvSource.class).convert();
        Goate jdata = new JSONToGoate(fileAsAString("map.json")).convert();
        expect(Expectation.build()
                .actual(data)
                .isEqualTo(jdata));
    }

    public enum CSVTOJSON{
        a("nick"), z("paul");
        String csvValue;
        CSVTOJSON(String value){
            this.csvValue = value;
        }

        public boolean map(String value){
            return csvValue.equals(value);
        }
    }
}
