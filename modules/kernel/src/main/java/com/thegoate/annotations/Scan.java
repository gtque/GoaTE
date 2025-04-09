package com.thegoate.annotations;

import com.thegoate.info.Info;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Only useful on annotations.
 * Used to indicate that the annotations should be scanned for on classes.
 */
@Retention(RetentionPolicy.RUNTIME)
@GoateDescription(description = "Marks the annotation for inclusion in class scanning.")
@Info
public @interface Scan {
}
