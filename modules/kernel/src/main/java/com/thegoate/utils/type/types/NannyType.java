package com.thegoate.utils.type.types;

import com.thegoate.Goate;
import com.thegoate.utils.fill.serialize.Nanny;
import com.thegoate.utils.type.FindType;
import com.thegoate.utils.type.IsType;

/**
 * Created by Eric Angeli on 7/6/2020.
 */
@IsType
public class NannyType extends FindType {

	@Override
	public boolean isType(Object check){
		return check instanceof Nanny;
	}

	public Class type(Object check){
		return Nanny.class;
	}
}
