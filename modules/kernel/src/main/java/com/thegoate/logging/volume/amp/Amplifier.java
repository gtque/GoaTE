package com.thegoate.logging.volume.amp;

import com.thegoate.utils.Utility;

/**
 * Created by Eric Angeli on 7/1/2020.
 */
public interface Amplifier extends Utility {
	String amplify(Object message);
}
