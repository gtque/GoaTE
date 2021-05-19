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
public class StackTraceElementArrayType extends FindType {

	@Override
	public boolean isType(Object check){
		return check instanceof StackTraceElement[];
	}

	public Class type(Object check){
		return StackTraceElement[].class;
	}
}
