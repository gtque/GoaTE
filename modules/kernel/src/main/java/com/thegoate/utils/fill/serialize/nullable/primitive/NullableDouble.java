package com.thegoate.utils.fill.serialize.nullable.primitive;

import com.thegoate.utils.fill.serialize.GoatePojo;
import com.thegoate.utils.fill.serialize.Nanny;

@GoatePojo(forceCast = true)
public class NullableDouble extends Nanny {

	private double value;

	public NullableDouble(){
	}
	public NullableDouble(Object value){
		if(value != null){
			this.value = Double.parseDouble(""+value);
		}
	}

	public static NullableDouble build(Object value){
		return new NullableDouble(value);
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

}
