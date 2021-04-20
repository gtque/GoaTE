package com.thegoate.utils.fill.serialize.pojos.nullable;

import com.thegoate.utils.fill.serialize.GoatePojo;
import com.thegoate.utils.fill.serialize.Kid;
import com.thegoate.utils.fill.serialize.nullable.primitive.NullableDouble;

@GoatePojo
public class Wrapper extends Kid {

	private NullableDouble d;

	public NullableDouble getD() {
		return d;
	}

	public void setD(NullableDouble d) {
		this.d = d;
	}
}
