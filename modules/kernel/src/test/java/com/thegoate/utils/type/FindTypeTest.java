package com.thegoate.utils.type;

import com.thegoate.expect.Expectation;
import com.thegoate.testng.TestNGEngineMethodDL;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.util.Date;

public class FindTypeTest extends TestNGEngineMethodDL {

    @Test(groups = {"unit"})
    public void typeIsClass() {
        Object findType = getClass();
        Class theType = new FindType().type(findType);
        expect(Expectation.build()
                .actual(theType)
                .isNull(false));
    }

    @Test(groups = {"unit"})
    public void typeIsJsonObject() {
        Object findType = "{\"hello\":\"world\"}";
        Class theType = new FindType().type(findType);
        expect(Expectation.build()
                .actual(theType)
                .isEqualTo(JSONObject.class));
    }

    @Test(groups = {"unit"})
    public void typeIsJsonArray() {
        Object findType = "[{\"hello\":\"world\"}]";
        Class theType = new FindType().type(findType);
        expect(Expectation.build()
                .actual(theType)
                .isEqualTo(JSONObject.class));
    }

    @Test(groups = {"unit"})
    public void typeIsDate() {
        Object findType = "2009-11-24";
        Class theType = new FindType().type(findType);
        expect(Expectation.build()
                .actual(theType)
                .isEqualTo(Date.class));
    }

}
