package com.thegoate.utils.fill.serialize.nestedmultisource;

import com.thegoate.Goate;
import com.thegoate.expect.Expectation;
import com.thegoate.testng.TestNGEngineMethodDL;
import com.thegoate.utils.fill.serialize.DeSerializer;
import com.thegoate.utils.fill.serialize.pojos.Cheese;
import com.thegoate.utils.fill.serialize.pojos.FlatNestList;
import com.thegoate.utils.fill.serialize.pojos.RootSource;
import com.thegoate.utils.togoate.ToGoate;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NestedMultiSourceTest extends TestNGEngineMethodDL {

    String json = "{\"content\":[{\"name\":\"Tarun\",\"job\":\"Bringer of Pain\"}]}";
    String json2 = "[{\"name\":\"Brian\",\"job\":\"Harbinger of Doom\"}]";

    @Test(groups = {"unit"})
    public void flatNestList() {
        Goate g = getDatabaseData();
        Goate j = new ToGoate(json).convert();
        Goate j2 = new ToGoate(json2).convert();
        FlatNestList cheese = new DeSerializer().data(g).from(Cheese.class).build(FlatNestList.class);
        FlatNestList nest   = new DeSerializer().data(j).from(RootSource.class).build(FlatNestList.class);
        FlatNestList nest2   = new DeSerializer().data(j2).build(FlatNestList.class);

        expect(Expectation.build()
                .actual(cheese)
                .isNull(false));
        expect(Expectation.build()
                .actual(cheese.getNest().get(0).getInner().getName())
                .isEqualTo("Tarun"));
        expect(Expectation.build().actual(cheese).isEqualTo(nest));
        expect(Expectation.build().actual(nest).isNotEqualTo(nest2));
    }

    protected Goate formatResults(List<?> listOf){
        Goate results = new Goate();
        List<Map<String, ?>> listOfMaps = (List<Map<String,?>>)listOf;
        for (int i = 0; i < listOfMaps.size(); i++) {
            results.put("row." + i, listOfMaps.get(i));
            for (Map.Entry<String, ?> entry : listOfMaps.get(i).entrySet()) {
                results.put("row." + i + "." + entry.getKey(), entry.getValue());
            }
        }
        results.put("size", listOfMaps.size());
        return results;
    }

    protected Goate getDatabaseData(){
        List<Map<String,?>> db = new ArrayList<>();
        Map<String, String> dbData = new HashMap<>();
        dbData.put("inner_name", "Tarun");
        dbData.put("inner_job", "Bringer of Pain");
        db.add(dbData);
        return formatResults(db);
    }
}
