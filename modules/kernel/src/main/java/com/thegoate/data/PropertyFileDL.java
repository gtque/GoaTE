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
package com.thegoate.data;

import com.thegoate.Goate;
import com.thegoate.utils.GoateUtils;
import com.thegoate.utils.get.GetFileAsString;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads a simple flat file into a single data set.
 * Each entry should be on a separate line using key=value
 * Use a # at the start of a line to enter a comment.
 * Created by Eric Angeli on 5/5/2017.
 */
public class PropertyFileDL extends DataLoader {

    @Override
    public List<Goate> load() {
        List<Goate> data = new ArrayList<>();
        Goate props = loadProperties(new Goate(), "" + parameters.get("file"));
        data.add(props);
        return data;
    }

    private Goate loadProperties(Goate props, String file) {
        if (props != null && file != null && new File(GoateUtils.getFilePath(file)).exists()) {
            String pf = "" + new GetFileAsString(file).from("file::");
            if (!pf.isEmpty()) {
                pf = pf.replace("\n\r", "\r").replace("\r", "\n");
                for (String line : pf.split("\n")) {
                    if (line != null && !line.isEmpty() && !line.startsWith("#")) {
                        String[] prop = line.split("=");
                        if (prop.length >= 2) {
                            props.put(prop[0].trim(), prop[1].trim());
                        }
                    }
                }
            }
            String[] files;
            String ext = props.get("extends", null, String.class);
            if (ext != null) {
                files = ext.split(",");
            } else {
                files = new String[0];
            }
            for (String extendedFile : files) {
                props = loadProperties(props.filterExclude("extends"), extendedFile);
            }
        }
        return props;
    }

    public PropertyFileDL file(String file) {
        setParameter("file", file);
        return this;
    }

    public PropertyFileDL file(File file) {
        setParameter("file", file);
        return this;
    }
}
