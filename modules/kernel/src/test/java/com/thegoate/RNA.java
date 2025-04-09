package com.thegoate;

import com.thegoate.annotations.AnnotationDNA;

public class RNA extends DNA{
    public static final RNA rna = new RNA();
    public Settings settings;

    private RNA() {
        super();
        if (yml != null) {
            settings = analyzeDna(new Settings(), yml, "");
        }
    }
}
