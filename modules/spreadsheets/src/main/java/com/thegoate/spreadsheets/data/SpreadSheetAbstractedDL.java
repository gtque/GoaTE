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
import com.thegoate.utils.fill.FillString;
import com.thegoate.utils.get.Get;
import com.thegoate.utils.togoate.ToGoate;


/**
 * Reads an abstract data location of a spreadsheet
 * from a json name / value pair, populates the interpolated
 * path segments and updates fileName to the actual path.
 *
 * Created by Brian Hill on 2019-02-28
 */
public class SpreadSheetAbstractedDL extends SpreadSheetDL {

    private String fileName;

    @Override
    public SpreadSheetDL fileName(String file){
        fileName = file;
        Goate rd = new ToGoate(new Get(file).from("file::")).convert();

        if (rd.get("spreadsheet") != null)
            fileName= "" + rd.get("spreadsheet");

        if(fileName.contains("${"))
            fileName = ""+new FillString(fileName).with(parameters);

        setParameter("fileName", fileName);

        return this;
    }

    public String getFileName() {
        return fileName;
    }
}
