package com.thegoate.spreadsheets.utils;

import org.atteo.classindex.IndexAnnotated;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Use to indicate file type supported.
 * Created by Eric Angeli on 9/14/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@IndexAnnotated
public @interface GoateSheet {
    String[] fileTypes();
}
