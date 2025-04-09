package com.thegoate.testng;

import com.thegoate.annotations.AnnotationScanner;
import com.thegoate.expect.Expectation;
import com.thegoate.xml.utils.type.XmlType;
import org.testng.annotations.Test;

public class TestNGDslTests extends TestNGEngineMethodDL {

    @Test(groups = {"unit"})
    public void classScannerTest2() {
        AnnotationScanner.getAnnotations();
    }

    @Test(groups = {"unit"})
    public void getClassName() {
        expect(Expectation.build()
                .actual(get("class name", "test.class.name::", String.class))
                .isEqualTo(getClass().getCanonicalName()));
        expect(Expectation.build()
                .actual(getTestName())
                .isEqualTo("getClassName:(0)"));
    }

    @Test(groups = {"unit"})
    public void getMethodName() {
        expect(Expectation.build()
                .actual(get("method name", "test.method.name::", String.class))
                .isEqualTo("getMethodName"));
        expect(Expectation.build()
                .actual(getTestName())
                .isEqualTo("getMethodName:(0)"));
    }

}
