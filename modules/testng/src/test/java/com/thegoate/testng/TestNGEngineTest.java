package com.thegoate.testng;

import com.thegoate.Goate;
import com.thegoate.data.StaticDL;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by Eric Angeli on 5/11/2017.
 */
public class TestNGEngineTest extends TestNGEngine {

    public TestNGEngineTest(){
        super();
    }

    @Factory(dataProvider = "dataLoader")
    public TestNGEngineTest(Goate data){
        super(data);
    }

    @Override
    public void defineDataLoaders() {
        runData.put("dl##", new StaticDL().add("runNumber",1).add("Scenario", "first run"))
                .put("dl##", new StaticDL().add("runNumber",2))
                .put("dl##", new StaticDL().add("runNumber",3));
    }

    @Test(groups = {"unit"})
    public void runNumberCheck(){
        assertEquals(getRunNumber(), get("runNumber",-42));
    }

}
