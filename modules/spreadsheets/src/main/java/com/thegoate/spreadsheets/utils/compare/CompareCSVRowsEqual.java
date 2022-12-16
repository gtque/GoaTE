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
package com.thegoate.spreadsheets.utils.compare;

import com.thegoate.spreadsheets.csv.CSVRecord;
import com.thegoate.utils.compare.CompareUtil;

/**
 * Compares a csv record (aka a row) to another csv record)<br>
 * returns true of they are equal, otherwise false.
 * Created by Eric Angeli on 8/10/2018.
 */
@CompareUtil(operator = "==", type = CSVRecord.class)
public class CompareCSVRowsEqual extends CompareCsvRecord {

    public CompareCSVRowsEqual(Object val) {
        super(val);
    }

    @Override
    protected void init(Object val){
        processNested = false;
        super.init(val);
    }

    @Override
    protected Object processNested(Object subContainer) {
        return null;
    }

    @Override
    public boolean evaluate() {
        boolean result = actual.size()==expected.size();
        if(result) {
            for (int i = 0; i < actual.size(); i++) {
                if (!actual.get(i).equals(expected.get(i))) {
                    result = false;
                    health.put("not equal##", "("+i+"): " + actual.get(i) + " != " + expected.get(i));
                }
            }
        } else {
            health.put("number of columns##", "different number of columns: " + actual.size() + " != " + expected.size());
        }
        return result;
    }
}
