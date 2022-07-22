package com.thegoate.json.utils.compare;

import com.thegoate.Goate;
import com.thegoate.expect.Expectation;
import com.thegoate.testng.TestNGEngineMethodDL;
import org.testng.annotations.Test;

public class TryingToImproveJsonCompareHealthCheck extends TestNGEngineMethodDL {

    @Test(groups = {"unit"})
    public void jsonCompare() {
        Goate goate = new Goate()
                .put("actual", "file::healthcheck_actual.json")
                .put("expected", "file::healthcheck_expected.json");
        expect(Expectation.build()
                .actual("actual")
                .from(goate)
                .isEqualTo("expected")
                .fromExpected(goate));
    }
}
