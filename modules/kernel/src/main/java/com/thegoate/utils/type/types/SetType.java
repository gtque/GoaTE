package com.thegoate.utils.type.types;

import com.thegoate.utils.type.FindType;
import com.thegoate.utils.type.IsType;

import java.util.List;
import java.util.Set;

/**
 * Created by Eric Angeli on 7/6/2020.
 */
@IsType
public class SetType extends FindType {

	@Override
	public boolean isType(Object check){
		return check instanceof Set;
	}

	public Class type(Object check){
		return Set.class;
	}
}
