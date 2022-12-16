package com.thegoate.utils.type.types;

import com.thegoate.utils.type.FindType;
import com.thegoate.utils.type.IsType;

/**
 * Created by Eric Angeli on 7/6/2020.
 */
@IsType
public class NullType extends FindType {

	@Override
	public boolean isType(Object check){
		return check == null;
	}

	public Class type(Object check){
		return null;
	}
}
