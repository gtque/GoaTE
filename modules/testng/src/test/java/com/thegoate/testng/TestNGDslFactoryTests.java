package com.thegoate.testng;

import com.thegoate.Goate;
import com.thegoate.data.GoateProvider;
import com.thegoate.expect.Expectation;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

@GoateProvider(name = "dsl factory")
public class TestNGDslFactoryTests extends TestNGEngineMethodDL {

    public TestNGDslFactoryTests() {
        super();
    }

    @Factory(dataProvider = "dataLoader")
    public TestNGDslFactoryTests(Goate data) {
        super(data);
    }


    @Test(groups = {"unit"})
    public void getClassName() {
        expect(Expectation.build()
                .actual(get("class name", "test.class.name::", String.class))
                .isEqualTo(getClass().getCanonicalName()));
        expect(Expectation.build()
                .actual(getTestName())
                .isEqualTo("getClassName:"+get("Scenario")+"("+getRunNumber()+")"));
    }

    @Test(groups = {"unit"})
    public void getMethodName() {
        expect(Expectation.build()
                .actual(get("method name", "test.method.name::", String.class))
                .isEqualTo("getMethodName"));
        expect(Expectation.build()
                .actual(getTestName())
                .isEqualTo("getMethodName:"+get("Scenario")+"("+getRunNumber()+")"));
    }

}
