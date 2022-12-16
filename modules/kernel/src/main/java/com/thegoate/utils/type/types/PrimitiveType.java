package com.thegoate.utils.type.types;

import com.thegoate.reflection.GoateReflection;
import com.thegoate.utils.type.FindType;
import com.thegoate.utils.type.IsType;

/**
 * Created by Eric Angeli on 7/6/2020.
 */
@IsType
public class PrimitiveType extends FindType {

	@Override
	public boolean isType(Object check){
		return new GoateReflection().isPrimitiveOrNumerical(check);
	}

	public Class type(Object check){
		return new GoateReflection().primitiveType(check);
	}
}
