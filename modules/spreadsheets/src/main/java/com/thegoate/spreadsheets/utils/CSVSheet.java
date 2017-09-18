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
import com.thegoate.annotations.IsDefault;
import com.thegoate.utils.GoateUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.Reader;
import java.util.Iterator;

/**
 * Provides support for csv files and is the default.
 * Opens using RFC4180
 * Created by Eric Angeli on 9/14/2017.
 */
@IsDefault
@GoateSheet(fileTypes = {"csv"})
public class CSVSheet extends SheetUtils{

    @Override
    public Goate load() {
        data = new Goate();
        if(fileName!=null&&!fileName.isEmpty()){
            fileName = GoateUtils.getFilePath(fileName);
            try {
                Reader in = new FileReader(fileName);
                Iterable<CSVRecord> records =CSVFormat.RFC4180.parse(in);
                boolean headersFound = false;
                int row = 0;
                for(CSVRecord record : records){
                    Iterator iterator = record.iterator();
                    int count = 0;
                    Goate theRow = new Goate();
                    while(iterator.hasNext()) {
                        Object value = iterator.next();
                        if (firstRowIsHeader && !headersFound) {
                            headers.add(""+value);
                        }else{
                            String id = ""+count;
                            if(firstRowIsHeader){
                                id = headers.get(count);
                            }
                            theRow.put(id, value);
                        }
                        count++;
                    }
                    if(headersFound||!firstRowIsHeader) {
                        data.put(""+row, theRow);
                        row++;
                    }
                    headersFound = true;
                }
            }catch(Exception e){
                LOG.warn("problem loading csv file ("+fileName+"):"+e.getMessage(), e);
            }
        }
        return data;
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
