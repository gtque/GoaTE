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
package com.thegoate.spreadsheets.utils.get;

import com.thegoate.spreadsheets.utils.SheetUtils;
import com.thegoate.spreadsheets.utils.SpreadSheetUtil;
import com.thegoate.utils.get.Get;
import com.thegoate.utils.get.GetUtil;
import com.thegoate.utils.get.GetUtility;
import com.thegoate.utils.get.NotFound;

/**
 * Get from the excel sheet.
 * Created by Eric Angeli on 5/19/2017.
 */
@GetUtil(type = SheetUtils.class)
public class GetSpreadSheet extends SpreadSheetUtil implements GetUtility {

    public GetSpreadSheet(Object val) {
        super(val);
    }

    @Override
    protected Object processNested(Object subContainer) {
        Object result = subContainer;
        if (nested != null) {
            result = new Get(nested).from(subContainer);
        }
        return result;
    }

    @Override
    public Object from(Object container) {
        Object result = null;
        String selector = "" + takeActionOn;
        SheetUtils sheet = (SheetUtils) container;
        if (selector.equals("headers")) {
            result = sheet.headers();//csv.getRecords().get(0);
        } else if (selector.startsWith("rowCount")) {
            result = sheet.rowCount();
        } else if (selector.startsWith("colCount")) {
            result = sheet.headers().size();
        } else if (selector.startsWith("get row#")) {
            int rowNumber = Integer.parseInt((selector.substring(selector.indexOf("#") + 1).trim()));
            result = sheet.getRow(rowNumber);//new CSVRecord(excel,excel.getRecords().get(rowNumber));
        } else if (selector.startsWith("load sheet:=")) {
            String sheetname = selector.substring(selector.indexOf(":=") + 2).trim();
            sheet.sheet(sheetname).load();
            result = sheet;
        } else {
            String[] cellId = selector.split("\\.");
            boolean reloaded = false;
            String originalSheet = sheet.sheetName();
            if (selector.contains(":")) {
                String sheetName = selector.substring(0, cellId[0].indexOf(":"));
                cellId[0] = cellId[0].replace(sheetName + ":", "");
                if (!sheetName.equals(originalSheet)) {
                    sheet.sheet(sheetName).load();
                    reloaded = true;
                }
            }
            result = sheet.get(cellId[1], Integer.parseInt(cellId[0]), new NotFound(selector));
            if (reloaded) {
                sheet.sheet(originalSheet).load();
            }
        }
        result = processNested(result);
        return result;
    }
}
