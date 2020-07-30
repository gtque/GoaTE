package com.thegoate.expect.amp;

import com.thegoate.annotations.IsDefault;
import com.thegoate.logging.volume.amp.GoateAmplifier;

/**
 * Created by Eric Angeli on 7/1/2020.
 */
@GoateAmplifier(type = FailedChannel.class)
@IsDefault(forType = true)
public class FailAmplifier extends StatusAmplifier {

	public FailAmplifier(Object message) {
		super(message);
	}

	@Override
	protected void setStatus() {
		status = ev.fails();
	}

	@Override
	public boolean isType(Object check) {
		return false;
	}

}
