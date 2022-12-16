package com.thegoate.json.utils.type;

import com.thegoate.utils.type.IsNullRepresentation;
import com.thegoate.utils.type.NullRepresentation;
import org.json.JSONObject;

/**
 * Created by Eric Angeli on 7/6/2020.
 */
@IsNullRepresentation
public class IsJsonNull implements NullRepresentation {

	@Override
	public boolean isNull(Object check){
		return check == JSONObject.NULL;
	}

}
