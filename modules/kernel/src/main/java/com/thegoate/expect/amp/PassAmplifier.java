package com.thegoate.expect.amp;

import com.thegoate.Goate;
import com.thegoate.annotations.IsDefault;
import com.thegoate.logging.volume.amp.GoateAmplifier;

/**
 * Created by Eric Angeli on 7/1/2020.
 */
@GoateAmplifier(type = PassChannel.class)
@IsDefault(forType = true)
public class PassAmplifier extends StatusAmplifier {

	public PassAmplifier(Object message) {
		super(message);
	}

	@Override
	protected void setStatus() {
		status = ev.passes();
	}

	@Override
	public boolean isType(Object check) {
		return false;
	}

}
