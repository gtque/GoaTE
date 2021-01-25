package com.thegoate.logging.volume.amp;

import com.thegoate.annotations.IsDefault;

import java.util.List;

/**
 * Created by Eric Angeli on 7/1/2020.
 */
@GoateAmplifier(type = List.class)
@IsDefault(forType = true)
public class ListAmplifier extends BasicAmplifier{

	public ListAmplifier(Object message) {
		super(message);
	}

	@Override
	public String amplify(Object message) {
		return "(list)" + (message!=null?(""+message):null);
	}

	@Override
	public boolean isType(Object check) {
		return false;
	}

}
