package com.thegoate.annotations;

import com.thegoate.utils.fill.serialize.GoateSource;

import java.util.Arrays;
import java.util.List;

@GoateSource(key = "annotations")
public class AnnotationDNA {

    public String scan;

    public List<String> listOfPackagesToIncludeInTheScan() {
        List<String> packages = null;
        if(scan != null) {
            packages = Arrays.stream(scan.split(",")).map(String::trim).toList();
        }
        return packages;
    }
}
