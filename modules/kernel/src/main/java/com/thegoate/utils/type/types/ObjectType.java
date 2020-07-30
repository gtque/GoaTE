package com.thegoate.utils.type.types;

import com.thegoate.annotations.IsDefault;
import com.thegoate.utils.type.FindType;
import com.thegoate.utils.type.IsType;

/**
 * Created by Eric Angeli on 7/6/2020.
 */
@IsType
@IsDefault
public class ObjectType extends FindType {

	@Override
	public boolean isType(Object check){
		return check != null;
	}

	public Class type(Object check){
		return check.getClass();
	}
}
