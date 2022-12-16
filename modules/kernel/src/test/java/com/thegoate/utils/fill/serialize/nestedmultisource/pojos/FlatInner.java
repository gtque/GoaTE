package com.thegoate.utils.fill.serialize.nestedmultisource.pojos;

import com.thegoate.utils.fill.serialize.GoatePojo;
import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.Kid;
import com.thegoate.utils.fill.serialize.pojos.Cheese;

/**
 * Created by Eric Angeli on 6/18/2020.
 */
@GoatePojo
public class FlatInner extends Kid {

	@GoateSource(source = Cheese.class, key = "inner_name")
	@GoateSource(source = FlatNest.class, key = "inner_name")
	private String name;
	@GoateSource(source = Cheese.class, key = "inner_job")
	@GoateSource(source = FlatNest.class, key = "inner_job")
	private String job;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}
}
