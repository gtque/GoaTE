package com.thegoate.dsl.words;

import com.thegoate.Goate;
import com.thegoate.data.DataLoader;
import com.thegoate.data.DataSeriesLoader;
import com.thegoate.expect.Expectation;
import com.thegoate.spreadsheets.data.SpreadSheetDL;
import com.thegoate.testng.TestNGEngineMethodDL;
import com.thegoate.utils.fill.serialize.pojos.SimplePojo;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Created by Eric Angeli on 11/18/2019.
 */
public class TestWords extends TestNGEngineMethodDL {

    @Test(groups = {"unit"})
    public void sumNoFilter() {
        Goate td = new Goate()
                .put("calculator", "sum::")
                .put("a", 42)
                .put("b", 42L)
                .put("c", true)
                .put("d", "howdy")
                .put("e", new Goate().put("aa", -42).put("bb", "-42"))
                .put("a1", 3.14159D);
        expect(Expectation.build().actual(td.get("calculator")).isEqualTo(87.14159));
    }

    @Test(groups = {"unit"})
    public void sumWithFilter() {
        Goate td = new Goate()
                .put("calculator", "sum::a")
                .put("a", 42)
                .put("b", 42L)
                .put("c", true)
                .put("d", "howdy")
                .put("e", new Goate().put("aa", -42).put("bb", "-42"))
                .put("a1", 3.14159D);
        expect(Expectation.build().actual(td.get("calculator")).isEqualTo(45.14159));
    }

    @Test(groups = {"unit"})
    public void dateShiftStatic() {
        expect(Expectation.build()
                .actual(DateShiftDSL.dateShift("yyyy-MM-dd", "2009-11-22", 2))
                .isEqualTo("2009-11-24"));
    }

    @Test(groups = {"unit"})
    public void dateShiftStaticNegativeUnits() {
        expect(Expectation.build()
                .actual(DateShiftDSL.dateShift("yyyy-MM-dd", "2009-12-24", -1, "n"))
                .isEqualTo("2009-11-24"));
    }

    @Test(groups = {"unit"})
    public void dateTimeShiftStatic() {
        expect(Expectation.build()
                .actual(DateTimeShiftDSL.dateTimeShift("yyyy-MM-dd HH:mm:ss", "2009-11-23 00:00:00", 24, "h"))
                .isEqualTo("2009-11-24 00:00:00"));
    }

    @Test(groups = {"unit"})
    public void dateTimeShiftNegativeStatic() {
        expect(Expectation.build()
                .actual(DateTimeShiftDSL.dateTimeShift("uuuu-MM-dd HH:mm:ss", "2009-11-25 00:00:00", -1))
                .isEqualTo("2009-11-24  00:00:00"));
    }

    @Test(groups = {"unit"})
    public void generateListFromCsv() {
        List<Object> expectedList = new ArrayList<>();
        expectedList.add("hello");
        expectedList.add(new SimplePojo().setFieldName("Barb"));
        Goate d = new Goate()
                .put("barb", new SimplePojo().setFieldName("Barb"))
                .put("csv", "list from csv::hello,o::barb");
        expect(Expectation.build()
                .actual(d.get("csv"))
                .isEqualTo(expectedList));
    }

    @Test(groups = {"unit"})
    public void concatCurrentTime() {
        Goate d = new SpreadSheetDL().fileName("data/the_initconcattime.csv").load().get(0);
        LOG.info("result: " + d.get("value"));
        expect(Expectation.build()
                .actual(d.get("value", "", String.class).contains("current time::"))
                .isEqualTo(false)
                .failureMessage(d.get("value", "", String.class)));
    }
}
