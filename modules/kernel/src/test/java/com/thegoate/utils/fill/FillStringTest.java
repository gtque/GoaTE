package com.thegoate.utils.fill;

import com.thegoate.Goate;
import com.thegoate.testng.TestNGEngineAnnotatedDL;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Simple test of Filling a String.
 * Created by Eric Angeli on 7/3/2017.
 */
public class FillStringTest extends TestNGEngineAnnotatedDL {

    @Test
    public void simpleFillString(){
        String test = "hello, ${eric}. how ${are} \\$ you?";
        FillString fs = new FillString(test);
        String result = "" + fs.with(new Goate().put("eric", "alex").put("are", "is"));
        String expected = "hello, alex. how is \\$ you?";
        assertEquals(result, expected);
    }

    @Test
    public void lookupFillString(){
        String test = "hello, ${eric}. how ${are} \\$ you?";
        Fill fs = new Fill(test);
        String result = "" + fs.with(new Goate().put("eric", "alex").put("are", "is"));
        String expected = "hello, alex. how is \\$ you?";
        assertEquals(result, expected);
    }
}
