package com.thegoate.utils.type;

import com.thegoate.Goate;
import com.thegoate.reflection.GoateReflection;
import com.thegoate.utils.UnknownUtilType;
import com.thegoate.utils.type.types.NullType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Eric Angeli on 7/6/2020.
 */
public class FindType extends UnknownUtilType implements TypeUtility {
    private static volatile Map<String, Class> typeList = new ConcurrentHashMap<>();
    private static volatile List<String> shadows = new ArrayList<>();

    public static void ban() {
        synchronized (shadows) {
            for (String key : shadows) {
                typeList.remove(key);
            }
            shadows = new ArrayList<>();
        }
    }

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
        synchronized (shadows) {
            String key = key(IsType.class, check, null, new GoateReflection().primitiveType(check));
            if (!typeList.containsKey(key)) {
                TypeUtility tool = (TypeUtility) buildUtil(check, IsType.class);
                if (tool instanceof NullType) {
                    check = null;
                } else {
                    typeList.put(key, tool.type(check));
                    if (key.matches(".*_shadow_ban_.*")) {
                        LOG.debug("Shadow ban", "this was not a primitive type, marking for shadow ban.");
                        shadows.add(key);
                    }
                }
            }
            return check == null ? null : typeList.get(key);
        }
    }

    @Override
    public boolean checkType(Class tool, Class type) {
        //		GoateAmplifier tu = (GoateAmplifier) tool.getAnnotation(GoateAmplifier.class);
        //		return tu.type() != null ? tu.equals(type) : type == null;
        return false;
    }
}
