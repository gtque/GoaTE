package com.thegoate.utils.fill.serialize.pojos.flat;

import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.Kid;

public class Child1 extends Kid {

	@GoateSource(source = Parent.class, key = "bobby")
	private String fieldA;

	public String getFieldA() {
		return fieldA;
	}

	public void setFieldA(String fieldA) {
		this.fieldA = fieldA;
	}
}
