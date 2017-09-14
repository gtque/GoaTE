package com.thegoate.spreadsheets;

import com.thegoate.spreadsheets.utils.SheetUtils;
import com.thegoate.testng.TestNGEngineMethodDL;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;

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
}
