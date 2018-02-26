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
import com.thegoate.json.utils.get.GetJsonField;
import com.thegoate.utils.fill.Fill;
import com.thegoate.utils.get.Get;
import com.thegoate.utils.get.GetFileListFromDir;
import com.thegoate.utils.togoate.ToGoate;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Loads all files from the given directory.
 * Created by Eric Angeli on 5/22/2017.
 */
public class BarnDataLoader extends DataLoader {
    String[] groups = {};
    String[] excluded = {};
    @Override
    public List<Goate> load() {
        String testGroups = parameters.get("testGroups","", String.class);
        String excludeGroups = parameters.get("excludeGroups", "", String.class);
        if(!testGroups.trim().isEmpty()) {
            groups = testGroups.split(",");
        }
        if(!excludeGroups.trim().isEmpty()) {
            excluded = excludeGroups.split(",");
        }
        List<Goate> data = new ArrayList<>();
        List<File> files = (List<File>) new GetFileListFromDir(parameters.get("dir")).from("filedir::");
        for (File file : files) {
            Goate rd = new ToGoate(new Get(file).from("file::")).convert();
            if(rd!=null) {
                if (("" + rd.get("abstract")).equals("true")) {
                    LOG.debug("Barn DataLoader","skipping: " + file.getName());
                } else {
                    rd = extend(rd, rd);
                    rd.drop("abstract");
                    rd.put("Scenario", file.getName() + ":" + rd.get("Scenario", ""));
                    if(checkGroups(rd)) {
                        data.add(rd);
                    }
                }
            }else{
                LOG.error("Barn DataLoader","Problem loading test: " + file.getName());
            }
        }
        return data;
    }

    protected Goate extend(Goate rd, Goate extension) {
        if (extension != null && rd != null) {
            String[] extensions = getExtensions(rd, extension);
            if(extensions!=null){
                for(String ext:extensions) {
                    ext = ext.trim();
                    if (ext.startsWith("/")) {
                        ext = ext.substring(1);
                    }
                    if(ext.contains("${")){
                        ext = ""+new Fill(ext).with(parameters);
                    }
                    extension.merge(extend(extension, new ToGoate(new Get("" + parameters.get("dir") + "/" + ext).from("file::")).convert()), false);
                }
            }
            rd.merge(extension, false);
        }
        return rd;
    }

    private String[] getExtensions(Goate rd, Goate extension){
        String[] result=null;
        if (extension.get("extends") != null) {
            String extensions = "" + rd.get("extends");
            if (new GetJsonField("").isType(extensions)) {
                Goate exts = new ToGoate(extensions).convert();
                result = new String[exts.size()];
                int index = 0;
                for(String ext:exts.keys()){
                    result[index] = "" + exts.get(ext);
                    index++;
                }
            } else {
                result = extensions.split(",");
            }
        }
        return result;
    }

    protected boolean checkGroups(Goate tc){
        boolean result = false;
        if(groups.length==0){
            result = true;
        } else {
            String group = tc.get("groups", "", String.class)+","+parameters.get("groups","");
            result = Arrays.stream(groups).parallel().anyMatch(group::contains);
        }
        if(excluded.length!=0){
            String group = tc.get("groups", "", String.class)+","+parameters.get("groups","");
            result = result && !(Arrays.stream(excluded).parallel().anyMatch(group::contains));
        }
        return result;
    }
    public BarnDataLoader testCaseDirectory(String dir) {
        setParameter("dir", dir);
        return this;
    }
    public BarnDataLoader groups(String label) {
        setParameter("groups", label);
        return this;
    }
}
