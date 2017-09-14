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

    public Goate getData(){
        return data;
    }

    public abstract Goate load();
    public Object get(int col, int row) {
        return get(col, row, null);
    }
    public abstract Object get(int col, int row, Object def);
    public Object get(String col, int row) {
        return get(col, row, null);
    }
    public abstract Object get(String col, int row, Object def);
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
