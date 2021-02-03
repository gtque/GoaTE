package com.thegoate.logging.dsl;

import java.util.logging.Level;

import com.thegoate.Goate;
import com.thegoate.dsl.DSL;
import com.thegoate.dsl.GoateDSL;

/**
 * Place Holder
 * Returns the configured level for the bleat logger set in the properties file.
 * This can be configured for specific classes or packages by adding a property in the properties file:
 * bleat.level.package.name.class=LEVEL
 * Supported levels: WARN,ERROR,TRACE,DEBUG,INFO
 * but really this should be controlled by your configuration for the SLF4J implementation you are using.
 * additionaly you may mute specific channels by adding a property called:
 * bleat.mute.package.name.class=comma,separated,list,of,enabled,levels
 * Mutable: FATAL,PASS,FAIL,SKIPPED,UNKNOWN
 * fatal, pass, fail, skipped, and unknown are not muted by default.
 * NOTE: Not yet fully implemented or supported, currently, will always return an empty list of muted levels.
 * Created by Eric Angeli on 5/5/2020.
 */
@GoateDSL(word = "bleat volume")
public class BleatVolume extends DSL {

	@Override
	public Object evaluate(Goate data) {
		return "";
	}
}
