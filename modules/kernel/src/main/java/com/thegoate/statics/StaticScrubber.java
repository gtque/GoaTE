package com.thegoate.statics;

import com.thegoate.annotations.AnnotationFactory;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;

import java.util.Map;

/**
 * Collects all the classes annotated with ResetStatics and calls resetStatic on them.<br>
 * Intended to help manage memory a keep it from getting too out of hand.
 * Created by Eric Angeli on 6/15/2017.
 */
public class StaticScrubber {
    public void scrub(){
        BleatBox LOG = BleatFactory.getLogger(getClass());
        AnnotationFactory af = new AnnotationFactory();
        Map<String, Class> scrubbers = af.annotatedWith(ResetStatics.class).getDirectory(ResetStatics.class.getCanonicalName());
        for(String key:scrubbers.keySet()){
            Class c = scrubbers.get(key);
            try{
                ResetStatic scrubber = (ResetStatic)c.newInstance();
                scrubber.resetStatics();
            } catch (IllegalAccessException | InstantiationException e) {
                LOG.warn("Failed to scrub: " + c.getCanonicalName() + "\n"+e.getMessage(), e);
            }
        }
    }
}
