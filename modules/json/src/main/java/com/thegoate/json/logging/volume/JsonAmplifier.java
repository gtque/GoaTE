package com.thegoate.json.logging.volume;

import java.util.logging.Level;

import org.json.JSONObject;

import com.thegoate.annotations.IsDefault;
import com.thegoate.json.JsonUtil;
import com.thegoate.logging.BleatLevel;
import com.thegoate.logging.volume.amp.Amplifier;
import com.thegoate.logging.volume.amp.GoateAmplifier;

/**
 * Created by Eric Angeli on 7/2/2020.
 */
@GoateAmplifier(type = JSONObject.class)
@IsDefault(forType = true)
public class JsonAmplifier extends JsonUtil implements Amplifier {

	public JsonAmplifier(Object message) {
		super(message);
	}

	@Override
	public String amplify(Object message) {
		String amplified = "" + message;
		BleatLevel level = LOG.level();
		if(level.isLoudEnough(Level.SEVERE)&&message!=JSONObject.NULL){
			amplified = amplified.trim();
			amplified = amplified.substring(0,1) +"..."+ amplified.substring(amplified.length()-1);
		}
		return amplified;
	}

	@Override
	protected Object processNested(Object subContainer) {
		return null;
	}
}
