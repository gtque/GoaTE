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
import com.thegoate.utils.get.GetFile;

/**
 * Created by Eric Angeli on 7/5/2017.
 */
@GoateDSL(word = "fileIO")
@GoateDescription(description = "Loads the file specified by the file path into a file and returns it "
    + "Note: 2nd parameter only used when providing the key value and test data to read the file path from",
    parameters = {"String: file path or File: file or Object: file","Goate: data"})
public class LoadFileIO extends DSL {

    public LoadFileIO(Object value){
        super(value);
    }

    public static String file(String filePath){
        return file(filePath, new Goate());
    }

    public static String file(File file){
        return file(file.getPath(), new Goate());
    }

    public static String file(Object file, Goate data){
        return ""+new LoadFileIO("fileIO::"+file).evaluate(data);
    }

    @Override
    public Object evaluate(Goate data) {
        String filePath = ""+get(1,data);
        return new GetFile(filePath).explode().from("fileIO::");
    }
}
