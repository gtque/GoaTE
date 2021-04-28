package com.thegoate.expect.test;

import com.thegoate.utils.fill.serialize.IsTypeT;
import com.thegoate.utils.fill.serialize.Kid;

public class SimplePojoT<T,U> extends Kid {

	@IsTypeT
	private T field;
	@IsTypeT(index = 1)
	private U field2;

	public T getField() {
		return field;
	}

	public SimplePojoT setField(T field) {
		this.field = field;
		return this;
	}

	public U getField2() {
		return field2;
	}

	public SimplePojoT setField2(U field2) {
		this.field2 = field2;
		return this;
	}
}
