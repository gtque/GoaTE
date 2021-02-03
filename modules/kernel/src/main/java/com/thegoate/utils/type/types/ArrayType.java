package com.thegoate.utils.type.types;

import com.thegoate.annotations.IsDefault;
import com.thegoate.utils.type.FindType;
import com.thegoate.utils.type.IsType;

import java.util.List;

/**
 * Created by Eric Angeli on 7/6/2020.
 */
@IsType
@IsDefault(forType = true)
public class ArrayType extends FindType {

	@Override
	public boolean isType(Object check){
		return check instanceof List;
	}

	public Class type(Object check){
		return List.class;
	}
}
