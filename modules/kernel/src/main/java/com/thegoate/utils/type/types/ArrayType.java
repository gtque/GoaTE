package com.thegoate.utils.type.types;

import com.thegoate.annotations.IsDefault;
import com.thegoate.utils.type.FindType;
import com.thegoate.utils.type.IsType;

import java.lang.reflect.Array;

/**
 * Created by Eric Angeli on 7/6/2020.
 */
@IsType
@IsDefault(forType = true)
public class ArrayType extends FindType {

	@Override
	public boolean isType(Object check){
		return check != null && check.getClass().isArray();
	}

	public Class type(Object check){
		return Array.class;
	}
}
