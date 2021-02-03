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

import static com.thegoate.dsl.words.LoadFile.fileAsAString;
import static com.thegoate.xml.XmlHelper.generateDocument;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.thegoate.Goate;
import com.thegoate.utils.GoateUtils;
import com.thegoate.utils.get.GetFileAsString;
import com.thegoate.xml.utils.togoate.XmlToGoate;

/**
 * Loads a simple flat file into a single data set.
 * Each entry should be on a separate line using key=value
 * Use a # at the start of a line to enter a comment.
 * Created by Eric Angeli on 5/5/2017.
 */
public class XmlFileDL extends DataLoader {

    @Override
    public List<Goate> load() {
        List<Goate> data = new ArrayList<>();
        Goate props = loadXml(new Goate(), "" + parameters.get("file"));
        data.add(props);
        return data;
    }

    private Goate loadXml(Goate props, String file) {
        if (props != null && file != null && GoateUtils.fileExists(file)) {
            props = new XmlToGoate(generateDocument(fileAsAString(file))).convert();
        }
        return props;
    }

    public XmlFileDL file(String file) {
        setParameter("file", file);
        return this;
    }

    public XmlFileDL file(File file) {
        setParameter("file", file);
        return this;
    }
}
