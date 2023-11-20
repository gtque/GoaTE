package com.thegoate.utils.type;

import com.thegoate.Goate;
import com.thegoate.utils.UnknownUtilType;
import com.thegoate.utils.type.types.NullType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Eric Angeli on 7/6/2020.
 */
public class FindType extends UnknownUtilType implements TypeUtility {
	private static volatile Map<String, Class> typeList = new ConcurrentHashMap<>();
//	public FindType(){
//	}
//
//	public FindType(Object... arguments){}

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
		String key = key(IsType.class, check, null, null);
		if(!typeList.containsKey(key)) {
			TypeUtility tool = (TypeUtility) buildUtil(check, IsType.class);
			if(tool instanceof NullType) {
				check = null;
			} else {
				typeList.put(key, tool.type(check));
			}
		}
		return check == null? null:typeList.get(key);
	}

	@Override
	public boolean checkType(Class tool, Class type) {
		//		GoateAmplifier tu = (GoateAmplifier) tool.getAnnotation(GoateAmplifier.class);
		//		return tu.type() != null ? tu.equals(type) : type == null;
		return false;
	}
}
