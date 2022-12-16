package com.thegoate.logging.volume.amp;

import com.thegoate.annotations.IsDefault;

/**
 * Created by Eric Angeli on 7/1/2020.
 */
@GoateAmplifier
@IsDefault
public class ObjectAmplifier extends BasicAmplifier{

	public ObjectAmplifier(Object message) {
		super(message);
	}

	@Override
	public String amplify(Object message) {
		return message!=null?(""+message):null;
	}

	@Override
	public boolean isType(Object check) {
		return false;
	}

}
