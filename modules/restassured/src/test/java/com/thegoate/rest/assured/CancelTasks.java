package com.thegoate.rest.assured;

import org.testng.annotations.Test;

import com.thegoate.Goate;
import com.thegoate.data.GoateDLP;
import com.thegoate.data.GoateProvider;
import com.thegoate.data.StaticDL;
import com.thegoate.expect.Expectation;
import com.thegoate.rest.RestCall;
import com.thegoate.rest.RestResult;
import com.thegoate.testng.TestNGEngineMethodDL;

public class CancelTasks extends TestNGEngineMethodDL {

	@GoateProvider(name = "server tasks")
	@Test(dataProvider = "methodLoader")
	public void cancel(Goate data) {
		Object o = new RestCall()
			.header("X-Octopus-Csrf-Token",
				"o2OFZqTeNqHPmnT7sfuWzwpFvm3QH1r2PkEQGXfRyfeWKL9NevOz12djUWiqEtjiQJYphyK%2bk97fTg9NCwyOLGsnoHW6Vc9RlPkPaCzJpzqGUrS9cAzc6jTJQctyAOTqglkGE7G3GtYAWLHpkD64f9ZPYLQdGO8IQFcTFJ6kPyn3GEj7fpWbtM6mvotueBikNTacFffkWxVb%2f4yZz8WTg99RHXHqCo7p7Vx5vUo1HD8%3d%7ceEO1gPyOupvXIDwUL0Fj8w%3d%3d")
			.header("cookie",
				"OctopusIdentificationToken_33480efd-06f5-4d62-b53d-cdc4f2655847=L7VW4pYT2buxbF91SDscbx8jXUnvQ5F%2fr7xKQ9cl0NKCXMFWOetOEFBCeU%2f%2f%2fDNfpjSypQIl%2fq8%2bKgqec23UZDRp6kv3wcxIDmP0ZYQ6lVgSBlodnQCCkV81tag28LV52IAU%2fuYNqUVVaMTu%2fjMfy9f%2bk%2b5%2fTjxFG9BUgiOzy%2f3xZOkzi0%2f6rCpF5bZVBS6FnDkHiGY81aYnMifMLBlMduT%2felpTZ%2fYDGeXqmQga7cQ%3d%7c6QkUcZxyrOARxi5IGlYkxg%3d%3d; Octopus-Csrf-Token_33480efd-06f5-4d62-b53d-cdc4f2655847=o2OFZqTeNqHPmnT7sfuWzwpFvm3QH1r2PkEQGXfRyfeWKL9NevOz12djUWiqEtjiQJYphyK%2bk97fTg9NCwyOLGsnoHW6Vc9RlPkPaCzJpzqGUrS9cAzc6jTJQctyAOTqglkGE7G3GtYAWLHpkD64f9ZPYLQdGO8IQFcTFJ6kPyn3GEj7fpWbtM6mvotueBikNTacFffkWxVb%2f4yZz8WTg99RHXHqCo7p7Vx5vUo1HD8%3d%7ceEO1gPyOupvXIDwUL0Fj8w%3d%3d; _ga=GA1.2.1274348661.1614983283; _rdt_uuid=1614983282928.c2c7f665-463c-463c-8b33-d74dc972f133; _fbp=fb.1.1614983283115.2109616146; ROUTEID=.1")
			.baseURL("https://lab-octopus.wi.onedatascan.io/")
			.pathParam("task-id", get("server task", "", String.class))
			.post("api/tasks/{task-id}/cancel");
		expect(Expectation.build()
			.actual(RestResult.statusCode)
			.from(o)
			.isEqualTo(200));
	}

	@GoateDLP(name = "server tasks")
	public Goate[] tasks() {
		Goate[] dl = new Goate[2];
		dl[0] = new Goate();
		for (int id = 253547; id > 252710; id--) {//253312
			dl[0].put("run##", new StaticDL().add("server task", "ServerTasks-" + id));
		}
		return dl;
	}
}
