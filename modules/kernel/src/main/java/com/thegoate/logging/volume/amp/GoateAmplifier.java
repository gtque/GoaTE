package com.thegoate.logging.volume.amp;

import com.thegoate.annotations.GoateDescription;
import com.thegoate.annotations.Scan;
import com.thegoate.info.Info;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Eric Angeli on 7/1/2020.
 */
@Retention(RetentionPolicy.RUNTIME)
@GoateDescription(description = "Add this annotation to your amplifier implementation")
@Info
@Scan
public @interface GoateAmplifier {
	Class type() default Object.class;
}
