package com.thegoate.utils.type;

import com.thegoate.Goate;
import com.thegoate.utils.UnknownUtilType;

/**
 * Created by Eric Angeli on 7/6/2020.
 */
public class FindType extends UnknownUtilType implements TypeUtility {

	@Override
	public boolean isType(Object check) {
		return false;
	}

	@Override
	public Goate healthCheck() {
		return null;
	}

	@Override
	public Class type(Object check) {
		TypeUtility tool = (TypeUtility) buildUtil(check, IsType.class);
		return tool.type(check);
	}

	@Override
	public boolean checkType(Class tool, Class type) {
		//		GoateAmplifier tu = (GoateAmplifier) tool.getAnnotation(GoateAmplifier.class);
		//		return tu.type() != null ? tu.equals(type) : type == null;
		return false;
	}
}
