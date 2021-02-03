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
@GoateDSL(word = "csv")
public class LoadCsv extends LoadSpreadSheet {
    public LoadCsv(Object value) {
        super(value);
    }
    public static Object loadCsv(String file) {
        return loadCsv(GoateUtils.getFilePath(file), new Goate());
    }

    public static Object loadCsv(String file, boolean firstRowIsHeader) {
        return loadCsv(GoateUtils.getFilePath(file), -1, firstRowIsHeader, null, new Goate());
    }

    public static Object loadCsv(String file, Goate data) {
        return loadCsv(GoateUtils.getFilePath(file), -1, data);
    }

    public static Object loadCsv(String file, int rowNumber) {
        return loadCsv(GoateUtils.getFilePath(file), rowNumber, new Goate());
    }

    public static Object loadCsv(String file, int rowNumber, boolean firstRowIsHeader) {
        return loadCsv(GoateUtils.getFilePath(file), rowNumber, firstRowIsHeader, null, new Goate());
    }

    public static Object loadCsv(String file, int rowNumber, Goate data) {
        return loadCsv(GoateUtils.getFilePath(file), rowNumber, false, null, data);
    }

    public static Object loadCsv(Reader stream){
        return loadCsv(stream, -1, false, new Goate());
    }

    public static Object loadCsv(Reader stream, Goate data){
        return loadCsv(stream, -1, false, data);
    }

    public static Object loadCsv(Reader stream, int row){
        return loadCsv(stream, row, false, new Goate());
    }

    public static Object loadCsv(Reader stream, int row, Goate data){
        return loadCsv(stream, row, false, data);
    }

    public static Object loadCsv(Reader stream, boolean firstRowIsHeader){
        return loadCsv(stream, -1, firstRowIsHeader, new Goate());
    }

    public static Object loadCsv(Reader stream, boolean firstRowIsHeader, Goate data){
        return loadCsv(stream, -1, firstRowIsHeader, data);
    }

    public static Object loadCsv(Reader stream, int row, boolean firstRowIsHeader){
        return loadCsv(stream, row, firstRowIsHeader, new Goate());
    }

    public static Object loadCsv(Reader stream, int row, boolean firstRowIsHeader, Goate data){
        return loadCsv("reader.csv", row, firstRowIsHeader, stream, data);
    }

    public static Object loadCsv(File file){
        return loadCsv(file, new Goate());
    }

    public static Object loadCsv(File file, Goate data){
        return loadCsv(file, -1, false, data);
    }

    public static Object loadCsv(File file, int row){
        return loadCsv(file, row, false, new Goate());
    }

    public static Object loadCsv(File file, int row, Goate data){
        return loadCsv(file, row, false, data);
    }

    public static Object loadCsv(File file, boolean firstRowIsHeader){
        return loadCsv(file, -1, firstRowIsHeader, new Goate());
    }

    public static Object loadCsv(File file, boolean firstRowIsHeader, Goate data){
        return loadCsv(file, -1, firstRowIsHeader, data);
    }

    public static Object loadCsv(File file, int row, boolean firstRowIsHeader) {
        return loadCsv(file, row, firstRowIsHeader, new Goate());
    }

    public static Object loadCsv(File file, int row, boolean firstRowIsHeader, Goate data){
        return loadCsv("file.csv", row, firstRowIsHeader, file, data);
    }
    public static Object loadCsv(String file, int rowNumber, boolean firstRowIsHeader, Object fileObject, Goate data) {
        String row = (rowNumber >= 0 ? ("," + rowNumber) : "");
        String firstRow = ","+firstRowIsHeader;
        return new LoadCsv("csv::" + file + row + firstRow).setFile(fileObject).evaluate(data);
    }
}
