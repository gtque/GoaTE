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
public class CSVTest extends TestNGEngineMethodDL {

    @Test(groups = {"unit"})
    public void loadSimpleCSV() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        SheetUtils sheet = SheetUtils.build("sample.csv","the sheet name does not matter.");
        sheet.load();
        assertEquals(sheet.get("a",0),"b");
        assertEquals(sheet.get("a",1),"false");
        assertEquals(sheet.get(0,0),"b");
        assertEquals(sheet.get("c",0,"e"),"e");
        assertEquals(sheet.get(2,0,3),3);
    }

    @Test(groups = {"unit"})
    public void loadSimpleCSVNoHeaders() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        SheetUtils sheet = SheetUtils.build("sample.csv","the sheet name does not matter.").firstRowIsNotHeader();
        sheet.load();
        assertEquals(sheet.get(0,0),"a");
        assertEquals(sheet.get(1,1),"42,g");
        assertEquals(sheet.get("c",0,"e"),"e");
        assertEquals(sheet.get(2,0,3),3);
    }
}
