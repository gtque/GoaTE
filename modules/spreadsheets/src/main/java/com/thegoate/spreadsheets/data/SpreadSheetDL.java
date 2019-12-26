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
package com.thegoate.spreadsheets.data;

import com.thegoate.Goate;
import com.thegoate.data.DataLoader;
import com.thegoate.spreadsheets.utils.SheetUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Loads a simple flat file into a single data set.
 * Each entry should be on a separate line using key=value
 * Use a # at the start of a line to enter a comment.
 * Created by Eric Angeli on 5/5/2017.
 */
public class SpreadSheetDL extends DataLoader {

    @Override
    public List<Goate> load() {
        List<Goate> data = new ArrayList<>();
        String fileName = ""+parameters.get("fileName");
        String sheetName = ""+parameters.get("sheetName","Sheet1");
        boolean firstRow = Boolean.parseBoolean(""+parameters.get("firstRowIsHeader",true));
        try {
            SheetUtils sheet = SheetUtils.build(fileName, sheetName).firstRowIsHeader(firstRow);
            Goate info = sheet.load();
            for(String key:info.keys()){
                Goate page = (Goate)info.get(key);
                for(String row:page.keys()){
                    data.add((Goate)page.get(row));
                }
            }
        } catch(Exception e){
            LOG.error("failed to load spread sheet ("+fileName+"): "+e.getMessage(), e);
        }
        return data;
    }

    public SpreadSheetDL fileName(String file){
        setParameter("fileName", file);
        return this;
    }

    public SpreadSheetDL sheetName(String sheet){
        setParameter("sheetName", sheet);
        return this;
    }

    public SpreadSheetDL firstRowIsHeader(boolean headerRow){
        setParameter("firstRowIsHeader", headerRow);
        return this;
    }
}
