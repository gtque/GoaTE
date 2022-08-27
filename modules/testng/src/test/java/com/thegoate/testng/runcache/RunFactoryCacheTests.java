package com.thegoate.testng.runcache;

import com.thegoate.Goate;
import com.thegoate.data.GoateProvider;
import com.thegoate.expect.Expectation;
import com.thegoate.testng.TestNGEngineMethodDL;
import com.thegoate.testng.runcache.providers.LongOneDLP;
import org.testng.annotations.Test;

import static com.thegoate.testng.TestNGRunFactory.runCacheEnabled;

public class RunFactoryCacheTests extends TestNGEngineMethodDL {

    @Test(groups = {"unit","runcache"})
    public void testPlaceHolder() {
        LOG.debug("hello, world!");
    }

    @GoateProvider(name = "longOne")
    @Test(groups = {"unit","runcache"}, dataProvider = "methodLoader")
    public void testInitialDataLoad(Goate testData) {
        if (runCacheEnabled) {
            expect(Expectation.build()
                    .actual(LongOneDLP.touched)
                    .isEqualTo(1));
            expect(Expectation.build()
                    .actual(get("same", 42))
                    .isEqualTo(42));
            put("a", "y");
            expect(Expectation.build()
                    .actual(get("a"))
                    .isEqualTo("y"));
        }
    }

    @GoateProvider(name = "longOne")
    @Test(groups = {"unit","runcache"}, dataProvider = "methodLoader", dependsOnMethods = "testInitialDataLoad")
    public void testClonedDataLoad(Goate testData) {
        if (runCacheEnabled) {
            expect(Expectation.build()
                    .actual(LongOneDLP.touched)
                    .isEqualTo(1));
            expect(Expectation.build()
                    .actual(get("same", 84))
                    .isEqualTo(84));
            expect(Expectation.build()
                    .actual(get("a"))
                    .isEqualTo("x"));
        }
    }

    @GoateProvider(name = "longOne", nickName = "sparta")
    @Test(groups = {"unit","runcache"}, dataProvider = "methodLoader", dependsOnMethods = "testClonedDataLoad")
    public void testClonedInitialNickname(Goate testData) {
        if (runCacheEnabled) {
            expect(Expectation.build()
                    .actual(LongOneDLP.touched)
                    .isEqualTo(1));
            expect(Expectation.build()
                    .actual(get("same", 84))
                    .isEqualTo(84));
            put("a", "z" + runNumber);
            expect(Expectation.build()
                    .actual(get("a"))
                    .isEqualTo("z" + runNumber));
        }
    }

    @GoateProvider(name = "longOne", nickName = "sparta")
    @Test(groups = {"unit","runcache"}, dataProvider = "methodLoader", dependsOnMethods = "testClonedInitialNickname")
    public void testClonedAfterNickname(Goate testData) {
        if (runCacheEnabled) {
            expect(Expectation.build()
                    .actual(LongOneDLP.touched)
                    .isEqualTo(1));
            expect(Expectation.build()
                    .actual(get("same", 168))
                    .isEqualTo(84));
            expect(Expectation.build()
                    .actual(get("a"))
                    .isEqualTo("z" + runNumber));
        }
    }
}
