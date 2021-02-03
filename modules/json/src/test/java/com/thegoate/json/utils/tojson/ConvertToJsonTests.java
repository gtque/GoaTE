package com.thegoate.json.utils.tojson;

import com.thegoate.Goate;
import com.thegoate.testng.TestNGEngineAnnotatedDL;
import com.thegoate.utils.compare.Compare;
import com.thegoate.utils.compare.CompareUtility;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Created by Eric Angeli on 10/17/2018.
 */
public class ConvertToJsonTests extends TestNGEngineAnnotatedDL {
    @Test(groups = {"unit"})
    public void convertArrayToJson(){
        Goate g = new Goate().put("a.1","nic").put("a.0","eric");
        String j = new GoateToJSON(g).convert();
        CompareUtility c = new Compare(j).to("{\"a\":[\"eric\",\"nic\"]}").using("==");
        assertTrue(c.evaluate());
    }
}
