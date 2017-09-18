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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines the interface for interacting with a sheet.
 * Created by Eric Angeli on 9/14/2017.
 */
public abstract class SheetUtils {
    protected final BleatBox LOG = BleatFactory.getLogger(getClass());
    Goate data = new Goate();
    String fileName = "";
    String sheetName = "";
    List<String> headers = new ArrayList<>();
    boolean firstRowIsHeader = true;

    public SheetUtils file(String fileName){
        this.fileName = fileName;
        return this;
    }
    public SheetUtils sheet(String sheetName){
        this.sheetName = sheetName;
        return this;
    }
    public List<String> headers(){
        return headers;
    }

    public SheetUtils firstRowIsNotHeader(){
        this.firstRowIsHeader = false;
        return this;
    }

    public SheetUtils firstRowIsHeader(){
        this.firstRowIsHeader = true;
        return this;
    }

    public SheetUtils firstRowIsHeader(boolean firstRowIsHeader){
        this.firstRowIsHeader = firstRowIsHeader;
        return this;
    }

    public Goate getData(){
        return data;
    }

    public abstract Goate load();
    public Object get(int col, int row) {
        return get(col, row, null);
    }
    public Object get(int col, int row, Object def) {
        return get((headers.size()>col?headers.get(col):(""+col)), row, def);
    }
    public Object get(String col, int row) {
        return get(col, row, null);
    }
    public Object get(String col, int row, Object def) {
        return data.get(""+row, new Goate(), Goate.class).get(col,def);
    }
    public abstract SheetUtils set(int col, int row, Object value);
    public abstract SheetUtils set(String col, int row, Object value);
    public abstract SheetUtils writeToFile();

    public static SheetUtils build(String fileName, String sheetName) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        String ext = "";
        int extIndex = fileName.lastIndexOf(".");
        if(extIndex>=0&&extIndex<fileName.length()-1){
            ext = fileName.substring(extIndex+1);
        }
        AnnotationFactory af = new AnnotationFactory();
        return ((SheetUtils)af.annotatedWith(GoateSheet.class).find(ext).using("fileTypes").build()).file(fileName).sheet(sheetName);
    }
}
