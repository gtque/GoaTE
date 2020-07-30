package com.thegoate.logging.volume;

import com.thegoate.logging.volume.amp.Amplifier;
import com.thegoate.logging.volume.amp.GoateAmplifier;
import com.thegoate.utils.UnknownUtilType;
import com.thegoate.utils.UtilCache;
import com.thegoate.utils.get.NotFound;
import com.thegoate.utils.type.FindType;

/**
 * Created by Eric Angeli on 7/1/2020.
 */
//@UtilCache(useCache = true, name = "volume")
public class VolumeKnob extends UnknownUtilType {

	public static String volume(Object message) {
		Class type = new FindType().type(message);
		String output = null;
		Amplifier tool = (Amplifier) new VolumeKnob().buildUtil(message, GoateAmplifier.class, message, null, null, type);
		Object result = new NotFound("Get tool not found, make sure a proper get util is implemented or on the class path.");
		if (tool != null) {
			output = tool.amplify(message);
		} else {
			if (message != null) {
				output = message.toString();
			}
		}
		return output;
	}

	@Override
	public boolean checkType(Class tool, Class type) {
		GoateAmplifier tu = (GoateAmplifier) tool.getAnnotation(GoateAmplifier.class);
		return tu.type() != null ? (tu.type() == type) : type == null;
		//		return false;
	}
}
