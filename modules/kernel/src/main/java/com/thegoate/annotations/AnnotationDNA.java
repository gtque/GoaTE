package com.thegoate.annotations;

import com.thegoate.utils.fill.serialize.GoateSource;

import java.util.Arrays;
import java.util.List;

@GoateSource(key = "annotations")
public class AnnotationDNA {

    public String scan;
    public String jars;
    public String stripNestedPackage = "BOOT-INF.classes.";
    public boolean debug = false;

    public List<String> listOfPackagesToIncludeInTheScan() {
        List<String> packages = null;
        if(scan != null) {
            packages = Arrays.stream(scan.split(",")).map(String::trim).toList();
        }
        return packages;
    }

    public List<String> listOfJarsToIncludeInTheScan() {
        List<String> packages = null;
        if(jars != null) {
            packages = Arrays.stream(jars.split(",")).map(String::trim).toList();
        }
        return packages;
    }

    public List<String> listOfPackagesToStripFromNestedJars() {
        List<String> packages = null;
        if(stripNestedPackage != null) {
            packages = Arrays.stream(stripNestedPackage.split(",")).map(String::trim).toList();
        }
        return packages;
    }
}
