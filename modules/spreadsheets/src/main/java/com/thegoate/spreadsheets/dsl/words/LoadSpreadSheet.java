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
package com.thegoate.spreadsheets.dsl.words;

import com.thegoate.Goate;
import com.thegoate.dsl.DSL;
import com.thegoate.dsl.GoateDSL;
import com.thegoate.spreadsheets.utils.SheetUtils;

import java.io.File;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Eric Angeli on 12/5/2018.
 */
@GoateDSL(word = "spreadsheet")
public class LoadSpreadSheet extends DSL {
    protected Object file = null;
    protected String sheetName = null;

    public LoadSpreadSheet setFile(Object file){
        this.file = file;
        return this;
    }

    public LoadSpreadSheet sheet(String sheetName){
        this.sheetName = sheetName;
        return this;
    }

    public LoadSpreadSheet(Object value) {
        super(value);
    }

    @Override
    public Object evaluate(Goate data) {
        String filePath = "" + get(1, data);
        String i = "" + get(2, data);
        boolean firstRow = Boolean.parseBoolean("" + get(3, data));
        int row = -42;
        if (!i.equals("null")) {
            try {
                row = Integer.parseInt(i);
            } catch (Exception e) {
                if (i.equalsIgnoreCase("true") || i.equalsIgnoreCase("false")) {
                    firstRow = Boolean.parseBoolean(i);
                }
            }
        }
        SheetUtils sheet = null;
        try{
            if(file instanceof Reader) {
                sheet = SheetUtils.build(filePath, sheetName, (Reader) file);
            } else {
                sheet = SheetUtils.build(filePath, sheetName, (File) file);
            }
            sheet.firstRowIsHeader(firstRow);
            sheet.load();
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            LOG.error("Load Spreadsheet", "Failed to build the sheet: "+e.getMessage(), e);
        }

        Object val = sheet;
        if(row>=0){
            val = sheet.getRow(row);
        }
        return val;
    }
}
