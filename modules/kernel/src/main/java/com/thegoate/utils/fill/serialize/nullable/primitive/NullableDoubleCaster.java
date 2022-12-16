package com.thegoate.utils.fill.serialize.nullable.primitive;

import com.thegoate.utils.fill.serialize.CastUtil;
import com.thegoate.utils.fill.serialize.GoateCastUtility;

@CastUtil
public class NullableDoubleCaster extends GoateCastUtility {

	public NullableDoubleCaster(Object value) {
		super(value);
	}

	@Override
	public boolean isType(Object check) {
		return check == NullableDouble.class;
	}

	@Override
	public <T> T cast(Class<T> type) {
		T t = null;
		if(value!=null){
			t = (T)new NullableDouble(""+value);
		}
		return t;
	}
}
