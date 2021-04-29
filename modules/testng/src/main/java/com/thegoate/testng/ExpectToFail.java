package com.thegoate.testng;

import com.thegoate.annotations.GoateDescription;
import com.thegoate.info.Info;
import org.atteo.classindex.IndexAnnotated;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@GoateDescription(description = "Identifies a test method that is actually expected to fail.")
@Info()
@IndexAnnotated
public @interface ExpectToFail {
}
