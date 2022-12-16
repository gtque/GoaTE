package com.thegoate.utils.fill.serialize.pojos.nullable;

import com.thegoate.utils.fill.serialize.GoatePojo;
import com.thegoate.utils.fill.serialize.Kid;

@GoatePojo
public class WrapperPrimitiveDouble extends Kid {

	private double d;

	public double getD() {
		return d;
	}

	public void setD(double d) {
		this.d = d;
	}
}
