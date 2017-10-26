package com.thegoate.json.utils.compare;

import com.thegoate.Goate;
import com.thegoate.data.StaticDL;
import com.thegoate.testng.TestNGEngineMethodDL;
import com.thegoate.utils.compare.Compare;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Tests against CompareJson utilities.
 * Created by Eric Angeli on 10/5/2017.
 */
public class CompareJsonTests extends TestNGEngineMethodDL {
    public CompareJsonTests() {
        super();
    }

    @Factory(dataProvider = "dataLoader")
    public CompareJsonTests(Goate data) {
        super(data);
    }

    @Override
    public void defineDataLoaders() {
        runData.put("dl##", new StaticDL().add("Scenario", "equals returns true when equal")
                .add("json1", "{\"e\":\"y\",\"c\":\"x\",\"d\":\"z\",\"r\":[{\"a\":\"c\",\"g\":\"h\"},{\"a\":\"b\"},[\"a\"]]}")
                .add("json2", "{\"c\":\"x\",\"d\":\"z\",\"r\":[{\"a\":\"b\"},{\"a\":\"c\",\"g\":\"h\"},[\"a\"]],\"e\":\"y\"}")
                .add("operator", "==")
                .add("expected", true))
                .put("dl##", new StaticDL().add("Scenario", "equals returns false when not equal")
                        .add("json1", "{\"e\":\"y\",\"c\":\"x\",\"d\":\"z\",\"r\":[{\"a\":\"c\"},{\"a\":\"b\"},[\"a\"]]}")
                        .add("json2", "{\"c\":\"x\",\"d\":\"z\",\"r\":[{\"a\":\"b\"},{\"a\":\"b\"},[\"a\"]],\"e\":\"y\"}")
                        .add("operator", "==")
                        .add("expected", false));
    }

    @Test(groups = {"unit"})
    public void compareJson() {
        assertEquals(new Compare(get("json1")).to(get("json2")).using("" + get("operator")).evaluate(), get("expected"));
    }
}
