package com.thegoate.testng;

import com.thegoate.Goate;
import com.thegoate.data.GoateDLP;
import com.thegoate.data.GoateProvider;
import com.thegoate.data.StaticDL;
import com.thegoate.expect.Expectation;
import com.thegoate.utils.GoateUtils;
import org.testng.annotations.Test;

/**
 * Created by Eric Angeli on 2/17/2020.
 */
public class TestNGRunFactoryTest extends TestNGEngineMethodDL {

    @Test(groups = {"unit"})
    public void noRunDataHasConstantData1Run() {
        Object[][] runs = TestNGRunFactory.loadRuns(null, new Goate().put("run##", new StaticDL().add("groups", " do")), true, new String[0], new String[0]);
        String space = "-r- do    -r-";
        LOG.info("regex", space.replaceAll("\\s*-r-\\s*", "-r-"));
        expect(Expectation.build().actual(runs.length).isEqualTo(1));
    }

    @Test(groups = {"unit"})
    public void noRunData0Run() {
        Object[][] runs = TestNGRunFactory.loadRuns(null, null, true, new String[0], new String[0]);
        String space = "-r- do    -r-";
        LOG.info("regex", space.replaceAll("\\s*-r-\\s*", "-r-"));
        expect(Expectation.build().actual(runs.length).isEqualTo(0));
    }

    @Test(groups = {"unit"})
    public void runGroupsNoRuns() {
        Object run = GoateUtils.getProperty("runGroups");
        GoateUtils.setEnvironment("runGroups", "do");
//		Goate runData = new Goate();
        Object[][] runs = TestNGRunFactory.loadRuns(null, null, true, new String[0], new String[0]);
        String space = "-r- do    -r-";
        LOG.info("regex", space.replaceAll("\\s*-r-\\s*", "-r-"));
        expect(Expectation.build().actual(runs.length).isEqualTo(0));

        if (run != null) {
            GoateUtils.setEnvironment("runGroups", "" + run);
        } else {
            GoateUtils.setEnvironment("runGroups", "");
        }
    }

    @Test(groups = {"unit"})
    public void runGroupsConstantData() {
        Object run = GoateUtils.getProperty("runGroups");
        GoateUtils.setEnvironment("runGroups", "do");
        Goate runData = new Goate()
                .put("run##", new StaticDL().add("floop", " do"))
                .put("run##", new StaticDL().add("floop", "do not"))
                .put("run##", new StaticDL().add("floop", "do not"))
                .put("run##", new StaticDL().add("floop", "maybe,    do , yes"))
                .put("run##", new StaticDL().add("floop", "do not"))
                .put("run##", new StaticDL().add("floop", "do not,do "))
                .put("run##", new StaticDL().add("floop", "do"));
        Goate constantData = new Goate()
                .put("run##", new StaticDL().add("groups", "do"));
        Object[][] runs = TestNGRunFactory.loadRuns(runData, constantData, true, new String[0], new String[0]);
        String space = "-r- do    -r-";
        LOG.info("regex", space.replaceAll("\\s*-r-\\s*", "-r-"));
        expect(Expectation.build().actual(runs.length).isEqualTo(runData.size()));

        if (run != null) {
            GoateUtils.setEnvironment("runGroups", "" + run);
        } else {
            GoateUtils.setEnvironment("runGroups", "");
        }
    }

    @Test(groups = {"unit"})
    public void runGroups() {
        Object run = GoateUtils.getProperty("runGroups");
        GoateUtils.setEnvironment("runGroups", "do");
        Goate runData = new Goate()
                .put("run##", new StaticDL().add("groups", " do"))
                .put("run##", new StaticDL().add("groups", "do not"))
                .put("run##", new StaticDL().add("groups", "do not"))
                .put("run##", new StaticDL().add("groups", "maybe,    do , yes"))
                .put("run##", new StaticDL().add("groups", "do not"))
                .put("run##", new StaticDL().add("groups", "do not,do "))
                .put("run##", new StaticDL().add("nothing", "do"));
        Object[][] runs = TestNGRunFactory.loadRuns(runData, null, true, new String[0], new String[0]);
        String space = "-r- do    -r-";
        LOG.info("regex", space.replaceAll("\\s*-r-\\s*", "-r-"));
        expect(Expectation.build().actual(runs.length).isEqualTo(3));

        if (run != null) {
            GoateUtils.setEnvironment("runGroups", "" + run);
        } else {
            GoateUtils.setEnvironment("runGroups", "");
        }
    }

    @Test(groups = {"unit"})
    public void caseInsensitiveScenario() {
        Object run = GoateUtils.getProperty("run");
        GoateUtils.setEnvironment("run", "do");
        Goate runData = new Goate()
                .put("run##", new StaticDL().add("Scenario", "do"))
                .put("run##", new StaticDL().add("Scenario", "do not"))
                .put("run##", new StaticDL().add("scenario", "do not"))
                .put("run##", new StaticDL().add("scenario", "do"))
                .put("run##", new StaticDL().add("sCenArio", "do not"))
                .put("run##", new StaticDL().add("sCenArio", "do"));
        Object[][] runs = TestNGRunFactory.loadRuns(runData, null, true, new String[0], new String[0]);

        expect(Expectation.build().actual(runs.length).isEqualTo(3));

        if (run != null) {
            GoateUtils.setEnvironment("run", "" + run);
        } else {
            GoateUtils.setEnvironment("run", "");
        }
    }
    static Goate filterTheseRuns = new Goate();

    @GoateDLP(name = "filter multiple times")
    public Goate[] filterRuns() {
        Goate[] the = new Goate[2];
        for (int i = 0; i < 10000; i++) {
            filterTheseRuns.put("runf"+i, new StaticDL().add("Scenario", "do"))
                    .put("runa" + i, new StaticDL().add("Scenario", "do not"))
                    .put("runb"+i, new StaticDL().add("scenario", "do not"))
                    .put("runc"+i, new StaticDL().add("scenario", "do"))
                    .put("rund"+i, new StaticDL().add("sCenArio", "do not"))
                    .put("rune"+i, new StaticDL().add("sCenArio", "do"));

//            filterTheseRuns.put("run##", new StaticDL().add("Scenario", "do"))
//                    .put("run##", new StaticDL().add("Scenario", "do not"))
//                    .put("run##", new StaticDL().add("scenario", "do not"))
//                    .put("run##", new StaticDL().add("scenario", "do"))
//                    .put("run##", new StaticDL().add("sCenArio", "do not"))
//                    .put("run##", new StaticDL().add("sCenArio", "do"));
        }
        the[0] = new Goate()
                .put("run##", new StaticDL().add("ogFilter", true))//.add("filter me", runData))
                .put("run##", new StaticDL().add("ogFilter", true))//.add("filter me", runData))
                .put("run##", new StaticDL().add("ogFilter", true))//.add("filter me", runData))
                .put("run##", new StaticDL().add("ogFilter", true))//.add("filter me", runData))
                .put("run##", new StaticDL().add("ogFilter", true))//.add("filter me", runData))
                .put("run##", new StaticDL().add("ogFilter", false))//.add("filter me", runData))
                .put("run##", new StaticDL().add("ogFilter", false))//.add("filter me", runData))
                .put("run##", new StaticDL().add("ogFilter", false))//.add("filter me", runData))
                .put("run##", new StaticDL().add("ogFilter", false))//.add("filter me", runData))
                .put("run##", new StaticDL().add("ogFilter", false));//.add("filter me", runData));
        the[1] = null;
        return the;
    }

    @GoateProvider(name = "filter multiple times")
    @Test(groups = {"unit"}, dataProvider = "methodLoader")
    public void caseInsensitiveScenarioTimer(Goate testData) {
        Object run = GoateUtils.getProperty("run");
        Object filter = GoateUtils.getProperty("ORIGINAL_FILTER");
        GoateUtils.setEnvironment("run", "do");
        GoateUtils.setEnvironment("ORIGINAL_FILTER", "" + get("ogFilter", false));
//        Goate runData = get("filter me", new Goate(), Goate.class);
        LOG.debug("loading and filtering runs...");
        long startTime = System.currentTimeMillis();
        Object[][] runs = TestNGRunFactory.loadRuns(filterTheseRuns, null, true, new String[0], new String[0]);
        long endTime = System.currentTimeMillis();

        LOG.info("\n------------------\nfilter: " +  get("ogFilter", false) +
                "\nfilter time: \n" + (endTime - startTime) +
                "\noriginal size: \n" + filterTheseRuns.size() +
                "\nfiltered size: \n" + runs.length + "\n------------------");
//		expect(Expectation.build().actual(runs.length).isEqualTo(3));

        if (run != null) {
            GoateUtils.setEnvironment("run", "" + run);
        } else {
            GoateUtils.setEnvironment("run", "");
        }
        if (filter != null) {
            GoateUtils.setEnvironment("filter", "" + filter);
        } else {
            GoateUtils.setEnvironment("filter", "");
        }
    }

    @Test(groups = {"unit"})
    public void scenario() {
        Goate runData = new Goate()
                .put("run##", new StaticDL().add("Scenario", "do"))
                .put("run##", new StaticDL().add("Scenario", "do not"))
                .put("run##", new StaticDL().add("scenario", "do not"))
                .put("run##", new StaticDL().add("scenario", "do"))
                .put("run##", new StaticDL().add("sCenArio", "do not"))
                .put("run##", new StaticDL().add("sCenArio", "do"));
        Object[][] runs = TestNGRunFactory.loadRuns(runData, null, true, new String[0], new String[0]);
        expect(Expectation.build().actual(runs.length).isEqualTo(6));
//		evaluate();
    }

    @GoateProvider(name = "scenarios")
    @Test(groups = {"unit"}, dataProvider = "methodLoader")
    public void scenarioName(Goate testData) {
        expect(Expectation.build().actual(getScenario()).isEqualTo("do"));
    }

    @GoateDLP(name = "scenarios")
    public Goate[] theData() {
        Goate[] the = new Goate[2];
        the[0] = new Goate()
                .put("run##", new StaticDL().add("Scenario", "do"))
                .put("run##", new StaticDL().add("scenario", "do"))
                .put("run##", new StaticDL().add("SCENARIO", "do"))
                .put("run##", new StaticDL().add("sCenArIo", "do"));
        the[1] = null;
        return the;
    }
}
