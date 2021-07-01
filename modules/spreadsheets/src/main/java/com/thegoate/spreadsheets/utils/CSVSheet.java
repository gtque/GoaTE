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
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.thegoate.dsl.words.EutConfigDSL.eut;

/**
 * Provides support for csv files and is the default.
 * Opens using RFC4180
 * Created by Eric Angeli on 9/14/2017.
 */
@IsDefault
@GoateSheet(fileTypes = {"csv"})
public class CSVSheet extends SheetUtils {

    int maxColumnCount = 0;

    @Override
    public SheetUtils sheet(String sheetName) {
        return this;
    }

    @Override
    public Goate load() {
        data = new Goate();
        Reader in = null;
        Goate sheetData = new Goate();
        if (fileName != null && !fileName.isEmpty() && (file == null || !(file instanceof Reader))) {
            fileName = GoateUtils.getFilePath(fileName);
            try {
                if (new File(fileName).exists()) {
                    in = new FileReader(fileName);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (file != null || in == null) {
            if (file instanceof Reader) {
                in = (Reader) file;
            } else {
                try {
                    in = new FileReader((File) file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (in != null) {
            try {
                Iterable<CSVRecord> records = CSVFormat.RFC4180.withTrim(trimWhiteSpace).parse(in);
                boolean headersFound = false;
                int row = 0;
                for (CSVRecord record : records) {
                    Iterator iterator = record.iterator();
                    int count = 0;
                    Goate theRow = new Goate();
                    List<String> headerList = new ArrayList<>();
                    while (iterator.hasNext()) {
                        Object value = iterator.next();
                        if (firstRowIsHeader && !headersFound) {
                            headerList.add("" + value);
                        } else {
                            String id = "" + count;
                            if (firstRowIsHeader) {
                                id = headers.get(sheetName).get(count);
                            }
                            if(eut("sheet.load.emptyAsNull", true, Boolean.class)) {
                                if (value instanceof String) {
                                    if (((String) value).isEmpty()) {
                                        value = null;
                                    }
                                }
                            }
                            theRow.put(id, value, false);
                        }
                        count++;
                    }
                    if (headersFound || !firstRowIsHeader) {
                        sheetData.put("" + row, theRow);
                        row++;
                    } else {
                        headers.put(sheetName, headerList);
                    }
                    headersFound = true;
                    if(count>maxColumnCount){
                        maxColumnCount=count;
                    }
                }
                data.put(sheetName, sheetData);
                in.close();
            } catch (Exception e) {
                LOG.warn("problem loading csv file (" + fileName + "):" + e.getMessage(), e);
            }
        }
        return data;
    }

    @Override
    public SheetUtils set(int col, int row, Object value) {
        if(col+1>maxColumnCount){
            maxColumnCount = col+1;
        }
        return super.set(col, row, value);
    }
    @Override
    public int rowCount() {
        return data.get(sheetName, new Goate(), Goate.class).size();
    }

    @Override
    public SheetUtils createNew() {
        return this;
    }

    @Override
    public SheetUtils writeToFile() {
        try{
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(this.fileName));
            CSVFormat format = CSVFormat.DEFAULT;
            if(firstRowIsHeader){
                String[] head = headers().toArray(new String[headers.size()]);
                format = format.withHeader(head);
            }
            CSVPrinter printer = new CSVPrinter(writer, format);
            for(int i=0; i<rowCount(); i++){
                printer.printRecord(rowAsList(i));
            }
            printer.flush();
            printer.close();
        } catch (Exception e){
            LOG.info("CSV Sheet", "Problem writing file: " + e.getMessage(), e);
        }
        return this;
    }

    private List<Object> rowAsList(int rowNumber){
        Goate row = getRow(rowNumber);
        List<Object> rowData = new ArrayList<>();
        if(firstRowIsHeader){
            headers().stream().forEach(header -> rowData.add(row.get(header)==null?"":row.get(header)));
        } else {
            for(int i=0; i<maxColumnCount; i++){
                Object value = get(i, rowNumber);
                rowData.add(value==null?"":value);
            }
        }
        return rowData;
    }
    @Override
    public SheetUtils close() {
        return this;
    }
}
