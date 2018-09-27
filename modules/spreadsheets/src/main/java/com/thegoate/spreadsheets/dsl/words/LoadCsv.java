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
import com.thegoate.annotations.GoateDescription;
import com.thegoate.dsl.DSL;
import com.thegoate.dsl.GoateDSL;
import com.thegoate.spreadsheets.csv.CSVParser;
import com.thegoate.spreadsheets.csv.CSVRecord;
import com.thegoate.spreadsheets.staff.GetCsvEmployee;

import java.io.IOException;

/**
 * Created by Eric Angeli on 8/09/2018.
 */
@GoateDSL(word = "csv")
@GoateDescription(description = "Loads the specified CSV into a CsvParser, if a row number is specified then it returns the CsvRecord for that row.",
parameters = {"The path to the csv file", "The row number to return, optional:if not specified the entire CsvParser is returned."})
public class LoadCsv extends DSL {

    public LoadCsv(Object value){
        super(value);
    }

    @Override
    public Object evaluate(Goate data) {
        String filePath = ""+get(1,data);
        Goate definition = new Goate();
        definition.put("file", filePath);
        CSVParser csv =  (CSVParser)new GetCsvEmployee().init(definition).work();
        String i = ""+get(2,data);
        int row = -42;
        if(!i.equals("null")){
            row = Integer.parseInt(i);
        }
        Object val = null;
        try{
            val = row>=0?new CSVRecord(csv,csv.getRecords().get(row)):csv;
        } catch (IOException e) {
            LOG.debug("Load CSV", "Failed to get records: " + e.getMessage(), e);
        }
        return val;
    }
}
