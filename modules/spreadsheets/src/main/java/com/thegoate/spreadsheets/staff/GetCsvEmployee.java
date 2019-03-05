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

import com.thegoate.spreadsheets.utils.SheetUtils;
import com.thegoate.staff.Employee;
import com.thegoate.staff.GoateJob;
import com.thegoate.utils.get.Get;

import java.io.File;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;

/**
 * Loads a csv file either from the path or result.
 * Useful when used as part of expectations for an integration or multistep test case.
 * Created by Eric Angeli on 5/22/2017.
 */
@GoateJob(jobs = {"get csv"})
public class GetCsvEmployee extends Employee {

    SheetUtils csv = null;
//    CSVParser csv = null;

//    public GetCsvEmployee fromResult(){
//        definition.put("from result", true);
//        return this;
//    }

//    public GetCsvEmployee from(Object source){
//        //definition.put("from result", true);
//        definition.put("_goate_result", source);
//        return this;
//    }

    public GetCsvEmployee from(String path){
        definition.put("file", path);
        return this;
    }

    public GetCsvEmployee from(File file){
        definition.put("file", file);
        return this;
    }

    public GetCsvEmployee from(Reader in){
        definition.put("file", in);
        return this;
    }

    @Override
    public Employee init() {
        boolean fromResult = definition.get("from result", false, Boolean.class);
        if(fromResult) {
            definition.put("file", new Get("input stream").from(definition.get("_goate_result")));
        }
        try {
            if (definition.get("file") instanceof String) {
                csv = SheetUtils.build("" + definition.get("file"));
            } else if (definition.get("file") instanceof File) {
                csv = SheetUtils.build(null, null, (File)definition.get("file"));
            } else if (definition.get("file") instanceof Reader) {
                csv = SheetUtils.build(null, null, (Reader) definition.get("file"));
            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            LOG.error("Get CSV", "Problem loading csv file:"+ e.getMessage(), e);
        }
        return this;
    }

    @Override
    protected Object doWork() {
        if(csv!=null){
            csv.load();
        }
        return csv;
    }

    @Override
    public String[] detailedScrub(){
        String[] scrub = {};
        return scrub;
    }
}
