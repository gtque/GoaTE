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
import com.thegoate.data.DataModeler;
import com.thegoate.json.utils.get.GetJsonField;
import com.thegoate.utils.GoateUtils;
import com.thegoate.utils.file.Delete;
import com.thegoate.utils.fill.Fill;
import com.thegoate.utils.get.Get;
import com.thegoate.utils.get.GetFileAsString;
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
    String defaultGroup = "barn";
    boolean check_abstract = true;

    @Override
    public List<Goate> load() {
        cleanTempRoot("" + parameters.get("dir", ""));
        String testGroups = parameters.get("testGroups", "", String.class);
        String excludeGroups = parameters.get("excludeGroups", "", String.class);
        if (!testGroups.trim().isEmpty()) {
            groups = testGroups.split(",");
        }
        if (!excludeGroups.trim().isEmpty()) {
            excluded = excludeGroups.split(",");
        }
        List<Goate> data = new ArrayList<>();
        Object fo = new GetFileListFromDir(parameters.get("dir")).from("filedir::");
        List<File> files = (List<File>) fo;
        for (File file : files) {
            Object barn = new Get(file).from("file::");
            Goate rd = loadBarn(barn, file.getName());
            if (rd != null && checkGroups(rd)) {
                data.add(modelData(rd.scrub("extends")));//scrub(rd));
            }
        }
        return data;
    }

    private void cleanTempRoot(String theRoot) {
        if (!theRoot.isEmpty()) {
            if (theRoot.endsWith("/")) {
                theRoot += "..";
            } else {
                theRoot += "/..";
            }
            new Delete().rm(GoateUtils.getFilePath("temp/" + theRoot));
        }
    }

    public Goate loadBarn(Object barn, String fileName) {
        Goate rd = new ToGoate(barn).convert();
        if (rd != null) {
            if (fileName != null && (check_abstract && ("" + rd.get("abstract")).equals("true"))) {
                LOG.debug("Barn DataLoader", "skipping: " + fileName);
                rd = null;
            } else {
                rd = extend(rd, new Goate(), "" + parameters.get("dir", ""));
                rd.drop("abstract");
                rd.put("Scenario", (fileName != null ? fileName + ":" : "") + rd.get("Scenario", ""));
            }
        } else {
            LOG.error("Barn DataLoader", "Problem loading barn: " + fileName);
        }
        return rd;
    }

    protected Goate extend(Goate rd, Goate extension, String root) {
        String groups = rd.get("groups", "", String.class);
        String groupsE = "";
        if (extension != null && rd != null) {
            String[] extensions = getExtensions(rd);
            String theRoot = rd.get("_root", root, String.class);
            if (extensions != null) {
                for (String ext : extensions) {
                    ext = ext.trim();
                    if (ext.startsWith("/") || ext.startsWith("\\")) {
//                        ext = ext.substring(1);
                        theRoot = "";
                    } else {
                        if (!theRoot.endsWith("/")) {
                            theRoot += "/";
                        }
                    }
                    if (ext.contains("${")) {
                        ext = "" + new Fill(ext).with(parameters);
                    }
                    extension.merge(extend(new ToGoate(new GetFileAsString(theRoot + ext).explode().from("file::")).autoIncrement(false).convert(), new Goate(), theRoot), false);
                }
            }
            if (rd.get("expect") != null) {
                extension.scrub(".+_expect");
            }
            rd.merge(extension, false);
            groupsE = extension.get("groups", "", String.class);
        }
        if(!groups.isEmpty()){
            if(!groupsE.isEmpty()){
                groups += ","+groupsE;
            }
        } else {
            groups = groupsE;
        }
        rd.put("groups", groups);
        return rd;
    }

    private String[] getExtensions(Goate rd) {
        String[] result = null;
        if (rd.get("extends") != null) {
            String extensions = "" + rd.get("extends");
            if (new GetJsonField("").isType(extensions)) {
                Goate exts = new ToGoate(extensions).convert();
                result = new String[exts.size()];
                int index = 0;
                for (String ext : exts.keys()) {
                    result[index] = "" + exts.get(ext);
                    index++;
                }
            } else {
                result = extensions.split(",");
            }
        }
        return result;
    }

    protected boolean checkGroups(Goate tc) {
        boolean result = false;
        String group = tc.get("groups", "", String.class) + "," + parameters.get("groups", defaultGroup);
        if (groups.length == 0) {
            result = true;
        } else {
            result = Arrays.stream(groups).parallel().anyMatch(group::contains);
        }
        if (excluded.length != 0) {
            result = result && !(Arrays.stream(excluded).parallel().anyMatch(group::contains));
        }
        return result;
    }

    public BarnDataLoader testCaseDirectory(String dir) {
        setParameter("dir", dir);
        return this;
    }

    public BarnDataLoader groups(String label) {
        setParameter("testGroups", label);
        return this;
    }

    public BarnDataLoader excludes(String label) {
        setParameter("excludeGroups", label);
        return this;
    }

    public BarnDataLoader defaultGroup(String label) {
        this.defaultGroup = label;
        return this;
    }

    public BarnDataLoader ignoreAbstract() {
        this.check_abstract = false;
        return this;
    }

    public BarnDataLoader checkAbstract() {
        this.check_abstract = true;
        return this;
    }

    protected Goate modelData(Goate rd) {
        Goate dl = new ToGoate(rd.get("data modeler", rd.get("data modelers", "[]"))).convert();
        rd = rd.scrub("data modelers").scrub("data modeler");
        if (dl.size() > 0) {
            int index = 0;
            while (dl.get("" + index) != null) {
                Goate data = new ToGoate(dl.get("" + index, "{}")).convert();
                String dlp = data.get("name", null, String.class);
                if (dlp != null && !dlp.isEmpty()) {
                    rd.merge(new DataModeler(dlp, data).load().get(0), false);
                }
                index++;
                //String dlp = dl.get(""+index+".dlp","static barn",String.class);
                //Goate rd =
            }
        }
        return rd;
    }
}
