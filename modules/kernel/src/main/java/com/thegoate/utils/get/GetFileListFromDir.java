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

package com.thegoate.utils.get;

import com.thegoate.utils.GoateUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * recursively gets all files in a given directory.
 * Created by Eric Angeli on 5/5/2017.
 */
@GetUtil
public class GetFileListFromDir extends GetTool{

    List<File> files = new ArrayList<>();

    public GetFileListFromDir(){
        super("filedir::");
    }

    public GetFileListFromDir(Object selector) {
        super(selector);
    }

    @Override
    public boolean isType(Object check) {
        return (""+check).equalsIgnoreCase("filedir::");
    }

    @Override
    public Object from(Object container) {
        File original = null;
        if(selector instanceof File){
                if(((File) selector).isDirectory()){
                    original = (File)selector;
                    for(File f:original.listFiles()) {
                        selector = f;
                        from(container);
                    }
                    selector = original;
                }
                else if(((File)selector).exists()){
                    files.add((File)selector);
                }
        }else {
            String file = "" + selector;
            if (!file.equals("") && !file.equalsIgnoreCase("null")) {
                String fullPath = GoateUtils.getFilePath(file);
                try {
                    if (fullPath.contains(":")) {
                        if (fullPath.indexOf("/") == 0) {
                            fullPath = fullPath.substring(1);
                        }
                    }
                    selector = new File(fullPath);
                    if(((File) selector).isDirectory()){
                        original = (File)selector;
                        for(File f:original.listFiles()) {
                            selector = f;
                            from(container);
                        }
                        selector = original;
                    }
                    else if(((File)selector).exists()){
                        files.add((File)selector);
                    }
                } catch (Exception e) {
                    LOG.error("Problem loading file into a string: " + e.getMessage(), e);
                }
            }
        }
        Object result = processNested(files);
        return result;
    }
}
