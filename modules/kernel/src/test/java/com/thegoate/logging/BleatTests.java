package com.thegoate.logging;

import com.thegoate.testng.TestNGEngineAnnotatedDL;
import org.testng.annotations.Test;

import static org.testng.Assert.fail;

/**
 * Created by Eric Angeli on 7/19/2017.
 */
public class BleatTests extends TestNGEngineAnnotatedDL {

    @Test(groups = {"unit"})
    public void logAllTypes(){
        try{
            LOG.info("level: " + LOG.level());
            LOG.info("hello");
            LOG.info("hello", new Throwable("a"));
            LOG.debug("hello");
            LOG.debug("hello", new Throwable("a"));
            LOG.warn("hello");
            LOG.warn("hello", new Throwable("a"));
            LOG.error("hello");
            LOG.error("hello", new Throwable("a"));
            LOG.fatal("hello");
            LOG.fatal("hello", new Throwable("a"));
            LOG.fail("hello");
            LOG.fail("hello", new Throwable("a"));
            LOG.flush();
            LOG.pass("hello");
            LOG.pass("hello", new Throwable("a"));
            LOG.unknown("hello");
            LOG.unknown("hello", new Throwable("a"));
            LOG.skip("hello");
            LOG.skip("hello", new Throwable("a"));
            LOG.trace("hello");
            LOG.trace("hello", new Throwable("a"));
        }catch(Throwable t){
            LOG.error("something threw an excpetion:"+t.getMessage(),t);
            fail("should not have thrown anything.");
        }
    }
}
