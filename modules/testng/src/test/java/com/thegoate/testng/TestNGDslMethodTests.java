package com.thegoate.testng;

import com.thegoate.Goate;
import com.thegoate.data.GoateProvider;
import com.thegoate.expect.Expectation;
import org.testng.annotations.Test;

public class TestNGDslMethodTests extends TestNGEngineMethodDL {

    public TestNGDslMethodTests() {
        super();
    }

    @GoateProvider(name = "dsl factory")
    @Test(groups = {"unit"}, dataProvider = "methodLoader")
    public void getClassName(Goate testData) {
        expect(Expectation.build()
                .actual(get("class name", "test.class.name::", String.class))
                .isEqualTo(getClass().getCanonicalName()));
        expect(Expectation.build()
                .actual(getTestName())
                .isEqualTo("getClassName:"+get("Scenario")+"("+getRunNumber()+")"));
    }

    @GoateProvider(name = "dsl factory")
    @Test(groups = {"unit"}, dataProvider = "methodLoader")
    public void getMethodName(Goate testData) {
        expect(Expectation.build()
                .actual(get("method name", "test.method.name::", String.class))
                .isEqualTo("getMethodName"));
        expect(Expectation.build()
                .actual(getTestName())
                .isEqualTo("getMethodName:"+get("Scenario")+"("+getRunNumber()+")"));
    }

}
