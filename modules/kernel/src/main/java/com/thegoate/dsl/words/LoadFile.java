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
package com.thegoate.dsl.words;

import java.io.File;

import com.thegoate.Goate;
import com.thegoate.annotations.GoateDescription;
import com.thegoate.dsl.DSL;
import com.thegoate.dsl.GoateDSL;
import com.thegoate.utils.get.GetFileAsString;

/**
 * Created by Eric Angeli on 7/5/2017.
 */
@GoateDSL(word = "file")
@GoateDescription(description = "Returns the string representation of the file content. "
    + "Note: 2nd parameter only used when providing the key value and test data to read the file path from",
    parameters = {"String: file path or File: file or Object: file","Goate: data"})
public class LoadFile extends DSL {

    public LoadFile(Object value){
        super(value);
    }

    public static String fileAsAString(String filePath){
        return fileAsAString(filePath, new Goate());
    }

    public static String fileAsAString(File file){
        return fileAsAString(file, new Goate());
    }

    public static String fileAsAString(Object file, Goate data){
        return ""+new LoadFile("file::"+file).evaluate(data);
    }

    @Override
    public Object evaluate(Goate data) {
        String filePath = ""+get(1,data);
        return new GetFileAsString(filePath).explode().from("file::");
    }
}
