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
import com.thegoate.dsl.GoateDSL;
import com.thegoate.utils.GoateUtils;

import java.io.File;
import java.io.Reader;

/**
 * Created by Eric Angeli on 12/5/2018.
 */
@GoateDSL(word = "excel")
public class LoadExcel extends LoadSpreadSheet {
    public LoadExcel(Object value) {
        super(value);
    }

    public static Object loadExcel(Reader file, String sheet) {
        return loadExcel("file.xlsx", sheet, false, file, new Goate());
    }

    public static Object loadExcel(File file, String sheet) {
        return loadExcel("file.xlsx", sheet, false, file, new Goate());
    }

    public static Object loadExcel(Reader file, String sheet, Goate data) {
        return loadExcel("file.xlsx", sheet, false, file, data);
    }

    public static Object loadExcel(File file, String sheet, Goate data) {
        return loadExcel("file.xlsx", sheet, false, file, data);
    }

    public static Object loadExcel(Reader file, String sheet, boolean firstRowIsHeaders) {
        return loadExcel("file.xlsx", sheet, firstRowIsHeaders, file, new Goate());
    }

    public static Object loadExcel(File file, String sheet, boolean firstRowIsHeaders) {
        return loadExcel("file.xlsx", sheet, firstRowIsHeaders, file, new Goate());
    }

    public static Object loadExcel(Reader file, String sheet, boolean firstRowIsHeaders,  Goate data) {
        return loadExcel("file.xlsx", sheet, firstRowIsHeaders, file, data);
    }

    public static Object loadExcel(File file, String sheet, boolean firstRowIsHeaders, Goate data) {
        return loadExcel("file.xlsx", sheet, firstRowIsHeaders, file, data);
    }

    public static Object loadExcel(String file, String sheet) {
        return loadExcel(GoateUtils.getFilePath(file), sheet, false, null, new Goate());
    }

    public static Object loadExcel(String file, String sheet, boolean firstRowIsHeaders){
        return loadExcel(GoateUtils.getFilePath(file), sheet, firstRowIsHeaders, null, new Goate());
    }

    public static Object loadExcel(String file, String sheet, Goate data) {
        return loadExcel(GoateUtils.getFilePath(file), sheet, false, null, data);
    }

    public static Object loadExcel(String file, String sheet, boolean firstRowIsHeaders, Goate data) {
        return loadExcel(GoateUtils.getFilePath(file), sheet, firstRowIsHeaders, null, data);
    }

    public static Object loadExcel(String file, String sheet, boolean firstRowIsHeaders, Object fileObject, Goate data) {
        return new LoadExcel("excel::" + file + "," + firstRowIsHeaders).sheet(sheet).setFile(fileObject).evaluate(data);
    }
}
