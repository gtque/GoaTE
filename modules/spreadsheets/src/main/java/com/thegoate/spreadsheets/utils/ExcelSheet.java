package com.thegoate.spreadsheets.utils;

import com.thegoate.Goate;

/**
 * Provides support for excel files (xls and xlsx)
 * Created by Eric Angeli on 9/14/2017.
 */
@GoateSheet(fileTypes = {"xls","xlsx"})
public class ExcelSheet extends SheetUtils{

    @Override
    public Goate load() {
        return null;
    }

    @Override
    public Object get(int col, int row) {
        return null;
    }

    @Override
    public Object get(int col, int row, Object def) {
        return null;
    }

    @Override
    public Object get(String col, int row) {
        return null;
    }

    @Override
    public Object get(String col, int row, Object def) {
        return null;
    }

    @Override
    public SheetUtils set(int col, int row, Object value) {
        return null;
    }

    @Override
    public SheetUtils set(String col, int row, Object value) {
        return null;
    }

    @Override
    public SheetUtils writeToFile() {
        return null;
    }
}
