package com.thegoate.logging.volume.amp;

import com.thegoate.Goate;
import com.thegoate.utils.Utility;

/**
 * Created by Eric Angeli on 7/1/2020.
 */
public abstract class BasicAmplifier implements Amplifier{

	Object message;
	Goate data;
	Goate health = new Goate();

	public BasicAmplifier(Object message){
		this.message = message;
	}

	@Override
	public Utility setData(Goate data) {
		return this;
	}

	@Override
	public Goate healthCheck() {
		return health;
	}
}
