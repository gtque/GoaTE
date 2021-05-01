package com.thegoate.utils.fill.serialize.pojos.nullable;

import com.thegoate.utils.fill.serialize.GoatePojo;
import com.thegoate.utils.fill.serialize.Kid;

@GoatePojo
public class WrapperDouble extends Kid {

	private Double d = 42d;

	public Double getD() {
		return d;
	}

	public void setD(Double d) {
		this.d = d;
	}
}
