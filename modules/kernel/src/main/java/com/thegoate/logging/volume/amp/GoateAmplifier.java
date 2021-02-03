package com.thegoate.logging.volume.amp;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.atteo.classindex.IndexAnnotated;

import com.thegoate.annotations.GoateDescription;
import com.thegoate.info.Info;

/**
 * Created by Eric Angeli on 7/1/2020.
 */
@Retention(RetentionPolicy.RUNTIME)
@GoateDescription(description = "Add this annotation to your amplifier implementation")
@Info
@IndexAnnotated
public @interface GoateAmplifier {
	Class type() default Object.class;
}
