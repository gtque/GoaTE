package com.thegoate.eut;

import com.thegoate.eut.properties.Simple2Eut;
import com.thegoate.eut.properties.SimpleEut;
import com.thegoate.expect.Expectation;
import com.thegoate.testng.TestNGEngineMethodDL;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.thegoate.utils.compare.tools.CompareInvertedContains.LIST_CONTAINS;

public class EutTest extends TestNGEngineMethodDL {

    @Test(groups = {"unit"})
    public void simpleEutAccess() {
        expect(Expectation.build()
                .actual(SimpleEut.eut.FIELD_A)
                .isEqualTo("Fuzzy Wuzzy was a bear"));
        LOG.debug(SimpleEut.getEut(SimpleEut.class).FIELD_A);
        expect(Expectation.build()
                .actual(Simple2Eut.eut.FIELD_A)
                .isEqualTo("Fuzzy Wuzzy had no hair"));
    }

    @Test(groups = {"unit"})
    public void sourcePrefixList() {
        List<String> prefixes = SimpleEut.eut.sourcePrefixList();
        expect(Expectation.build()
                .actual(prefixes.size())
                .isEqualTo(6));
        expect(Expectation.build()
                .actual(prefixes.get(1))
                .isEqualTo("second"));
    }

    @Test(groups = {"unit"})
    public void alternateNameList() throws NoSuchFieldException {
        List<String> prefixes = SimpleEut.eut.sourcePrefixList();
        Field fieldA = SimpleEut.class.getField("FIELD_A");
        List<String> alternates = SimpleEut.eut.getAlternateNames(fieldA, fieldA.getName(), prefixes);
        expect(Expectation.build()
                .actual(prefixes.size())
                .isEqualTo(6));
        expect(Expectation.build()
                .actual(alternates.size())
                .isEqualTo(38));
        expect(Expectation.build()
                .actual(alternates)
                .is(LIST_CONTAINS)
                .expected("simpleeut.field.a"));
    }
}
