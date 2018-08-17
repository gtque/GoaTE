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

import com.thegoate.spreadsheets.csv.CSVParser;
import com.thegoate.spreadsheets.utils.CsvParserUtil;
import com.thegoate.utils.compare.CompareUtility;

/**
 * Base class for comparing json.
 * Created by Eric Angeli on 8/09/2018.
 */
public abstract class CompareCsvParser extends CsvParserUtil implements CompareUtility {

    String operator = "";
    CSVParser expected = null;
    CSVParser actual = null;

    public CompareCsvParser(Object val) {
        super(val);
        actual(val);
    }

    @Override
    protected void init(Object val) {
        processNested = false;
        super.init(val);
    }

    @Override
    public CompareUtility actual(Object actual){
        this.actual = (CSVParser)actual;
        return this;
    }

    @Override
    public CompareUtility to(Object expected){
        this.expected = (CSVParser)expected;
        return this;
    }

    @Override
    public CompareUtility using(Object operator){
        this.operator = "" + operator;
        return this;
    }

}
