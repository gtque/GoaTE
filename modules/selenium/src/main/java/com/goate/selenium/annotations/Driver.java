package com.goate.selenium.annotations;

import com.thegoate.annotations.GoateDescription;
import com.thegoate.info.Info;
import org.atteo.classindex.IndexAnnotated;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Mark an implemention of a web driver builder object.
 * Created by Eric Angeli on 6/28/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@GoateDescription(description = "Identifies a web driver builder implementation")
@Info(classifier = "type")
@IndexAnnotated
public @interface Driver {
    String type();
    String property() default "";
}
