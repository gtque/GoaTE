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

package com.thegoate.utils.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by gtque on 5/3/2017.
 */
public class Append {
    Logger LOG = LoggerFactory.getLogger(getClass());
    String line = "";

    public Append line(String line){
        this.line = line;
        return this;
    }

    public void to(String file){
        to(file, false);
    }

    public void to(String file, boolean overwrite){
        PrintWriter pw = null;
        try {
            if(overwrite){
                new Delete().rm(file);
            }
            File temp = new File(file);
            if(temp.getParentFile()!=null) {
                temp.getParentFile().mkdirs();
            }
            FileWriter fw = new FileWriter(temp.getAbsolutePath(), true);
            pw = new PrintWriter(new BufferedWriter(fw));
            pw.println(line);
        } catch (IOException e) {
            LOG.error("Error writing "+line+" to "+file+".", e);
        } finally {
            if (pw != null)
                pw.close();
        }
    }
}
