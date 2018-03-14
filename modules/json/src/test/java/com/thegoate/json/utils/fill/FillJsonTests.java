package com.thegoate.json.utils.fill;

import com.thegoate.Goate;
import com.thegoate.data.StaticDL;
import com.thegoate.testng.TestNGEngineMethodDL;
import com.thegoate.utils.compare.Compare;
import org.json.JSONArray;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Tests against CompareJson utilities.
 * Created by Eric Angeli on 10/5/2017.
 */
public class FillJsonTests extends TestNGEngineMethodDL {
    public FillJsonTests() {
        super();
    }

    @Factory(dataProvider = "dataLoader")
    public FillJsonTests(Goate data) {
        super(data);
    }

    @Override
    public void defineDataLoaders() {
        runData
                .put("dl##", new StaticDL().add("Scenario", "equals returns true when equal")
                        .add("json1", "{\"dropme\":42,\"empty\":\"should be empty\",\"isnull\":\"should be null\",\"e\":\"42\",\"c\":\"x\",\"d\":\"z\",\"r\":[{\"a\":\"45\",\"g\":\"g\"},{\"a\":\"b\"},[\"b\"]]}")
                        .add("json2", "{\"empty\":\"\",\"isnull\":null,\"c\":\"x\",\"d\":\"z\",\"r\":[{\"a\":\"c\",\"g\":\"h\"},{\"a\":\"b\"},[\"a\"]],\"e\":\"y\"}")
                        .add("e", "y")
                        .add("dropme", "drop field::")
                        .add("empty", "empty field::")
                        .add("isnull", "null field::")
                        .add("r.0", "json object::{\"a\":\"c\"\\,\"g\":\"h\"}")
                        .add("r.2.0", "a")
                        .add("operator", "==")
                        .add("expected", true))
                .put("dl##", new StaticDL().add("Scenario", "json array")
                        .add("json1", "[\n" +
                                "  {\n" +
                                "    \"field\": \"varianceChangeOptions.equityVariance\",\n" +
                                "    \"rejected\": 9066473733806080234472943,\n" +
                                "    \"message\": \"numeric value out of bounds (<5 digits><6 digits> expected)\"\n" +
                                "  }\n" +
                                "]")
                        .add("json2", "[\n" +
                                "  {\n" +
                                "    \"field\": \"varianceChangeOptions.equityVariance\",\n" +
                                "    \"rejected\": 9066473733806080234472943,\n" +
                                "    \"message\": \"numeric value out of bounds (<5 digits><6 digits> expected)\"\n" +
                                "  }\n" +
                                "]")
                        .add("operator", "==")
                        .add("expected", true))
        ;
    }

    @Test(groups = {"unit"})
    public void fillJson() {
        String json1 = "" + get("json1");
        json1 = new FillJson(json1).with(getData());
        LOG.debug(new FillJson(json1).getDescription());
        LOG.debug(json1);
        LOG.debug("" + get("json2"));
        put("json1", json1);
        assertEquals(new Compare(get("json1")).to(get("json2")).using("" + get("operator")).evaluate(), get("expected"));
    }
}
