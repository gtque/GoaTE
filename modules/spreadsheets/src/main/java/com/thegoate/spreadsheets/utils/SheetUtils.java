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
package com.thegoate.spreadsheets.utils;

import com.thegoate.Goate;
import com.thegoate.annotations.AnnotationFactory;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;

import java.io.File;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Defines the interface for interacting with a sheet.
 * Created by Eric Angeli on 9/14/2017.
 */
public abstract class SheetUtils {
    protected final BleatBox LOG = BleatFactory.getLogger(getClass());
    protected Goate data = new Goate();
    protected String fileName = "";
    protected String sheetName = "" + System.nanoTime();
    protected Map<String, List<String>> headers = new ConcurrentHashMap<>();
    protected boolean firstRowIsHeader = true;
    protected boolean loadAllData = false;
    protected Object file = null;

    public abstract int rowCount();

    public Goate getRow(int rowNumber) {
        return currentSheet().get("" + rowNumber, new Goate(), Goate.class);
    }

    public Goate currentSheet() {
        return data.get(sheetName, new Goate(), Goate.class);
    }

    public SheetUtils newFile(String fileName) {
        file(fileName);
        return newFile();
    }

    public SheetUtils newFile() {
        return createNew();
    }

    public SheetUtils loadAllData() {
        this.loadAllData = true;
        return this;
    }

    public SheetUtils stopAtFirstEmptyRow() {
        this.loadAllData = false;
        return this;
    }

    public abstract SheetUtils createNew();

    public SheetUtils file(File file) {
        this.file = file;
        return this;
    }

    public SheetUtils file(Reader file) {
        this.file = file;
        return this;
    }

    public SheetUtils file(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public SheetUtils sheet(String sheetName) {
        this.sheetName = sheetName;
        return this;
    }

    public String sheetName() {
        return sheetName;
    }

    public List<String> headers() {
        return headers(sheetName);
    }

    public List<String> headers(String sheetName) {
        return headers.get(sheetName) == null ? new ArrayList<>() : headers.get(sheetName);
    }

    public SheetUtils firstRowIsNotHeader() {
        this.firstRowIsHeader = false;
        return this;
    }

    public SheetUtils firstRowIsHeader() {
        this.firstRowIsHeader = true;
        return this;
    }

    public SheetUtils firstRowIsHeader(boolean firstRowIsHeader) {
        this.firstRowIsHeader = firstRowIsHeader;
        return this;
    }

    public Goate getData() {
        return data;
    }

    public abstract Goate load();

    public Object get(int col, int row) {
        return get(col, row, null);
    }

    public Object get(int col, int row, Object def) {
        return get((headers(sheetName).size() > col ? headers(sheetName).get(col) : ("" + col)), row, def);
    }

    public Object get(String col, int row) {
        return get(col, row, null);
    }

    public Object get(String col, int row, Object def) {
        return currentSheet().get("" + row, new Goate(), Goate.class).get(col, def);
    }

    public SheetUtils setHeader(int col, String header) {
        List<String> pageHeaders = checkHeaders(col + 1);
        pageHeaders.set(col, header);
        return this;
    }

    public List<String> checkHeaders(int size) {
        if (headers == null) {
            headers = new ConcurrentHashMap<>();
        }
        headers.computeIfAbsent(sheetName, k -> new ArrayList<>());
        if (headers.get(sheetName).size() < size) {
            for (int i = headers.get(sheetName).size(); i < size; i++) {
                headers.get(sheetName).add("");
            }
        }
        return headers.get(sheetName);
    }

    public SheetUtils set(int col, int row, Object value) {
        String colId = "" + col;
        if (firstRowIsHeader) {
            if (col < headers.get(sheetName).size()) {
                colId = headers.get(sheetName).get(col);
            }
        }
        return set(colId, row, value);
    }

    public SheetUtils set(String col, int row, Object value) {
        Goate page = currentSheet();
        Goate rowData = page.get("" + row, new Goate(), Goate.class);
        rowData.put(col, value);
        return this;
    }

    public abstract SheetUtils writeToFile();

    public abstract SheetUtils close();

    public static SheetUtils build(String fileName) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return build(fileName, (String) null);
    }

    public static SheetUtils build(String fileName, File file) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return build(fileName, null, file);
    }

    public static SheetUtils build(String fileName, Reader in) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return build(fileName, null, in);
    }

    public static SheetUtils build(String fileName, String sheetName) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return build(fileName, sheetName, (File) null);
    }

    public static SheetUtils build(String fileName, String sheetName, File file) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return getUtil(getExt(fileName)).file(fileName).sheet(sheetName).file(file);
    }

    public static SheetUtils build(String fileName, String sheetName, Reader file) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        SheetUtils util = getUtil(getExt(fileName));
        util.file(fileName);
        util.sheet(sheetName);
        util.file(file);
        return util;
    }

    protected static String getExt(String fileName) {
        String ext = "";
        int extIndex = fileName == null ? -1 : fileName.lastIndexOf(".");
        if (extIndex >= 0 && extIndex < fileName.length() - 1) {
            ext = fileName.substring(extIndex + 1);
        }
        if(!ext.equalsIgnoreCase("xls")&&!ext.equalsIgnoreCase("xlsx")){
            ext = "csv";
        }
        return ext;
    }

    protected static SheetUtils getUtil(String ext) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        AnnotationFactory af = new AnnotationFactory();
        af.annotatedWith(GoateSheet.class).find(ext).using("fileTypes");
        return (SheetUtils) af.build();
    }
}
