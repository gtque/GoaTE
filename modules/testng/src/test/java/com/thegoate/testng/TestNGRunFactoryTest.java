package com.thegoate.testng;

import org.testng.annotations.Test;

import com.thegoate.Goate;
import com.thegoate.data.GoateDLP;
import com.thegoate.data.GoateProvider;
import com.thegoate.data.StaticDL;
import com.thegoate.expect.Expectation;
import com.thegoate.utils.GoateUtils;

/**
 * Created by Eric Angeli on 2/17/2020.
 */
public class TestNGRunFactoryTest extends TestNGEngineMethodDL {

	@Test(groups = {"unit"})
	public void caseInsensitiveScenario(){
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

		if(run!=null){
			GoateUtils.setEnvironment("run", ""+run);
		} else {
			GoateUtils.setEnvironment("run", "");
		}
	}

	@Test(groups = {"unit"})
	public void scenario(){
		Goate runData = new Goate()
			.put("run##", new StaticDL().add("Scenario", "do"))
			.put("run##", new StaticDL().add("Scenario", "do not"))
			.put("run##", new StaticDL().add("scenario", "do not"))
			.put("run##", new StaticDL().add("scenario", "do"))
			.put("run##", new StaticDL().add("sCenArio", "do not"))
			.put("run##", new StaticDL().add("sCenArio", "do"));
		Object[][] runs = TestNGRunFactory.loadRuns(runData, null, true, new String[0], new String[0]);
		expect(Expectation.build().actual(runs.length).isEqualTo(6));
	}

	@GoateProvider(name = "scenario")
	@Test(groups = {"unit"}, dataProvider = "methodLoader")
	public void scenarioName(Goate testData){
		expect(Expectation.build().actual(getScenario()).isEqualTo("do"));
	}

	@GoateDLP(name = "scenario")
	public Goate[] theData(){
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
