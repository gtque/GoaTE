package com.thegoate.utils.fill.serialize.pojos.flat;

import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.Kid;

public class Parent extends Kid {

	@GoateSource(source = Parent.class, key = "")
	private Child1 little_bobby;
	@GoateSource(source = Parent.class, key = "")
	private Child2 little_susy;

	public Child1 getLittle_bobby() {
		return little_bobby;
	}

	public void setLittle_bobby(Child1 little_bobby) {
		this.little_bobby = little_bobby;
	}

	public Child2 getLittle_susy() {
		return little_susy;
	}

	public void setLittle_susy(Child2 little_susy) {
		this.little_susy = little_susy;
	}
}
