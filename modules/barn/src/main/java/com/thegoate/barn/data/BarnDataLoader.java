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
package com.thegoate.barn.data;

import com.thegoate.Goate;
import com.thegoate.data.DataLoader;
import com.thegoate.utils.get.Get;
import com.thegoate.utils.get.GetFileListFromDir;
import com.thegoate.utils.togoate.ToGoate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads all files from the given directory.
 * Created by Eric Angeli on 5/22/2017.
 */
public class BarnDataLoader extends DataLoader {

    @Override
    public List<Goate> load() {
        List<Goate> data = new ArrayList<>();
        List<File> files =  new GetFileListFromDir(parameters.get("dir")).from("filedir::");
        for(File file:files){
            Goate rd = new ToGoate(new Get(file).from("file::")).convert();
            rd.put("Scenario", file.getName()+":"+rd.get("Scenario", ""));
            data.add(rd);
        }
        return data;
    }

    public BarnDataLoader testCaseDirectory(String dir){
        setParameter("dir", dir);
        return this;
    }
}
