package com.thegoate.testng.test;

import com.thegoate.Goate;
import com.thegoate.data.GoateDLP;
import com.thegoate.data.StaticDL;

import static com.thegoate.data.DLProvider.*;

/**
 * Created by Eric Angeli on 6/25/2020.
 */
public class DataContainer {

	@GoateDLP(name="sample4")
	public Goate[] mydata(){
		Goate[] d = new Goate[size];
		d[runs] = new Goate().put("dl##", new StaticDL().add("a","x").add("Scenario", "use method provider 3."))
			.put("dl##", new StaticDL().add("a","x").add("Scenario", "use method provider 3."));
		d[constants] = new Goate().put("dl##", new StaticDL().add("b","y"));
		return d;
	}

//	@GoateDLP(name="sample5")
	public Goate[] mydata2(){
		Goate[] d = new Goate[size];
		d[runs] = new Goate().put("dl##", new StaticDL().add("a","y").add("Scenario", "use method provider 3."))
			.put("dl##", new StaticDL().add("a","y").add("Scenario", "use method provider 3."));
		d[constants] = new Goate().put("dl##", new StaticDL().add("b","x"));
		return d;
	}
}
