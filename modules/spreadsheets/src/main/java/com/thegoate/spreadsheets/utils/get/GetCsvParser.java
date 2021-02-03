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
package com.thegoate.spreadsheets.utils.get;

import com.thegoate.spreadsheets.csv.CSVParser;
import com.thegoate.spreadsheets.csv.CSVRecord;
import com.thegoate.spreadsheets.utils.CsvParserUtil;
import com.thegoate.utils.get.Get;
import com.thegoate.utils.get.GetUtil;
import com.thegoate.utils.get.GetUtility;

import java.io.IOException;

/**
 * Get the field from the given json.
 * Created by Eric Angeli on 5/19/2017.
 */
@GetUtil(type = CSVParser.class)
public class GetCsvParser extends CsvParserUtil implements GetUtility {

    public GetCsvParser(Object val) {
        super(val);
    }

    /**
     * Helpful definitions of what can be accessed from a csv page.
     */
    public static class page {
        public static String headers = "headers";
        public static String rowCount = "rowCount";
        public static String colCount = "colCount";
        public static String getRow(int rowNumber){
            return getRow(""+rowNumber);
        }
        public static String getRow(String rowNumber){
            return "get row#"+rowNumber;
        }
    }

    @Override
    protected Object processNested(Object subContainer) {
        Object result = subContainer;
        if(nested!=null){
            result = new Get(nested).from(subContainer);
        }
        return result;
    }

    @Override
    public Object from(Object container) {
        Object result = null;
        String selector = "" + takeActionOn;
        CSVParser csv = (CSVParser)container;
        if(selector.equals("headers")){
            try {
                result = new CSVRecord(csv,csv.getRecords().get(0));//csv.getRecords().get(0);
            }catch(IOException ioe){
                LOG.debug("Get Csv Field", "Problem getting headers: " + ioe.getMessage(), ioe);
            }
        }else if(selector.startsWith("rowCount")) {
            try {
                result = csv.getRecords().size();
            }catch(IOException ioe){
                LOG.debug("Get Csv Field", "Problem getting row count: " + ioe.getMessage(), ioe);
            }
        }else if(selector.startsWith("colCount")) {
            try {
                result = csv.getRecords().get(0).size();
            }catch(IOException ioe){
                LOG.debug("Get Csv Field", "Problem getting col count: " + ioe.getMessage(), ioe);
            }
        }else if(selector.startsWith("get row#")) {
            try {
                int rowNumber = Integer.parseInt((selector.substring(selector.indexOf("#")+1).trim()));
                result = new CSVRecord(csv,csv.getRecords().get(rowNumber));
            }catch(IOException ioe){
                LOG.debug("Get Csv Field", "Problem "+selector+": " + ioe.getMessage(), ioe);
            }
        }
        result = processNested(result);
        return result;
    }
}
