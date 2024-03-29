package com.thegoate.utils;

import com.thegoate.annotations.GoateDescription;
import com.thegoate.info.Info;
import org.atteo.classindex.IndexAnnotated;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Eric Angeli on 7/6/2020.
 */
@Retention(RetentionPolicy.RUNTIME)
@GoateDescription(description = "Identifies get utilities.")
@Info
@IndexAnnotated
@Inherited
public @interface UtilCache {
	String name() default "kanto";
	boolean clear() default false;
	boolean useCache() default false;
}
