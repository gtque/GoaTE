package com.thegoate.utils.type.types;

import com.thegoate.Goate;
import com.thegoate.annotations.IsDefault;
import com.thegoate.utils.type.FindType;
import com.thegoate.utils.type.IsType;

/**
 * Created by Eric Angeli on 7/6/2020.
 */
@IsType
@IsDefault(forType = true)
public class GoateType extends FindType {

	@Override
	public boolean isType(Object check){
		return check instanceof Goate;
	}

	public Class type(Object check){
		return Goate.class;
	}
}
