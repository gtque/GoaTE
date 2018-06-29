package com.thegoate.barn.data;

import com.thegoate.Goate;
import com.thegoate.data.DataLoader;
import com.thegoate.testng.TestNGEngineMethodDL;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Test the Barn data loader
 * Created by Eric Angeli on 2/26/2018.
 */
public class TestBarnDataLoader extends TestNGEngineMethodDL {

    @Test(groups = {"unit"})
    public void nestedExtends(){
        DataLoader dl = new BarnDataLoader().testCaseDirectory("barn/data");
        List<Goate> list = dl.load();
        assertEquals(list.size(),1);
        Goate d = list.get(0);
        LOG.debug("Nested Extends", d.toString());
        assertEquals(d.size(),5);
    }
}
