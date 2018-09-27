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
package com.thegoate.spreadsheets.staff;

import com.thegoate.Goate;
import com.thegoate.spreadsheets.csv.CSVParser;
import com.thegoate.staff.Employee;
import com.thegoate.staff.GoateJob;
import com.thegoate.utils.GoateUtils;
import com.thegoate.utils.get.Get;
import com.thegoate.utils.togoate.ToGoate;
import org.apache.commons.csv.CSVFormat;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Gets a specific result number.
 * Useful when used as part of expectations for an integration or multistep test case.
 * Created by Eric Angeli on 5/22/2017.
 */
@GoateJob(jobs = {"get csv"})
public class GetCsvEmployee extends Employee {

    CSVParser csv = null;

    @Override
    public Employee init() {
        boolean fromResult = definition.get("from result", false, Boolean.class);
        if(!fromResult){
            String fileName = definition.get("file",null, String.class);
            if(fileName!=null){
                try {
                    Reader in = new FileReader(GoateUtils.getFilePath(fileName));
                    csv = new CSVParser(CSVFormat.RFC4180.parse(in));
                } catch (IOException e) {
                    LOG.debug("Get CSV", "Problem loading csv: " + e.getMessage(), e);
                }
            }
        }else{
            try {
                csv = new CSVParser(CSVFormat.RFC4180.parse((InputStreamReader)new Get("input stream").from(data.get("_goate_result"))));
            } catch (IOException e) {
                LOG.debug("Get CSV", "Problem loading csv: " + e.getMessage(), e);
            }
        }
        return this;
    }

    @Override
    protected Object doWork() {
        return csv;
    }

    @Override
    public String[] detailedScrub(){
        String[] scrub = {};
        return scrub;
    }
}
