package com.thegoate.testng;

import com.thegoate.Goate;
import com.thegoate.data.GoateProvider;
import com.thegoate.expect.Expectation;
import com.thegoate.expect.ExpectationError;
import com.thegoate.expect.builder.ExpectationBuilder;
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
                .isEqualTo("getClassName:" + get("Scenario") + "(" + getRunNumber() + ")"));
    }

    @GoateProvider(name = "dsl factory")
    @Test(groups = {"unit"}, dataProvider = "methodLoader")
    public void getMethodName(Goate testData) {
        expect(Expectation.build()
                .actual(get("method name", "test.method.name::", String.class))
                .isEqualTo("getMethodName"));
        expect(Expectation.build()
                .actual(getTestName())
                .isEqualTo("getMethodName:" + get("Scenario") + "(" + getRunNumber() + ")"));
    }

    @Test(groups = {"unit"})
    public void passNowMultiple() {
        Goate truth = new Goate().put("truth", true);
        expectNow(Expectation.build()
                .actual("truth")
                .from(truth)
                .isEqualTo(true));
        truth.put("truth", false);
        expectNow(Expectation.build()
                .actual("truth")
                .from(truth)
                .isEqualTo(false));
    }

    @Test(groups = {"unit"})
    public void passNowWithDelayedEvaluation() {
        Goate truth = new Goate().put("truth", true);
        expectNow(Expectation.build()
                .actual("truth")
                .from(truth)
                .isEqualTo(true));
        truth.put("truth", false);
        expectNow(Expectation.build()
                .actual("truth")
                .from(truth)
                .isEqualTo(false));
        expect(Expectation.build()
                .actual("truth")
                .from(truth)
                .isEqualTo(false));
    }

    @ExpectToFail
    @Test(groups = {"unit"})
    public void failNowMultiple() {
        Goate truth = new Goate().put("truth", true);
        boolean result1 = expectNow(Expectation.build()
                .actual("truth")
                .from(truth)
                .isEqualTo(false));
        truth.put("truth", false);
        boolean result2 = expectNow(Expectation.build()
                .actual("truth")
                .from(truth)
                .isEqualTo(true));
        expect(Expectation.build()
                .actual(result1)
                .isEqualTo(false).failureMessage("true != false, i should have failed."));
        expect(Expectation.build()
                .actual(result1)
                .isEqualTo(false).failureMessage("false != true, i should have failed."));
    }

    @Test(groups = {"unit"})
    public void failNowImmediately() {
        Goate truth = new Goate().put("truth", true);
        boolean failed = true;
        try {
            failed = expectNow(Expectation.build()
                            .actual("truth")
                            .from(truth)
                            .isEqualTo(false),
                    true);
        } catch (ExpectationError expectationError) {
            LOG.info("I was supposed to fail, so if you see this I should pass.");
        }
        clearEvaluated();
        expect(Expectation.build()
                .actual(failed)
                .isEqualTo(true)
                .failureMessage("I should be true, but apparently the original expect now did not fail immediately."));
    }
}
