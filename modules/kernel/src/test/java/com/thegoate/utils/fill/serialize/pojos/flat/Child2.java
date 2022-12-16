package com.thegoate.utils.fill.serialize.pojos.flat;

import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.Kid;

public class Child2 extends Kid {

	@GoateSource(source = Parent.class, key = "susy")
	private String fieldA;

	public String getFieldA() {
		return fieldA;
	}

	public void setFieldA(String fieldA) {
		this.fieldA = fieldA;
	}
}
