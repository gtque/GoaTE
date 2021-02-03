package com.thegoate.map;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Requires the enum class in to() to have a method called map that
 * returns a boolean if the given value maps to that enum value.
 * Created by Eric Angeli on 12/6/2018.
 */
public class EnumMap {

    String key = "";

    public EnumMap(String key){
        this.key = key;
    }

    public static EnumMap map(String key){
        return new EnumMap(key);
    }

    public Enum to(Class map){
        Object mapped = null;
        try {
            if (map != null) {
                if (map.getEnumConstants() != null) {
                    for (Object eEnum : map.getEnumConstants()) {
                        Class[] args = {String.class};
                        Method m = ((Enum) eEnum).getClass().getMethod("map", args);
                        Object isMapped = m.invoke(eEnum, key);
                        if (("" + isMapped).equalsIgnoreCase("true")) {
                            mapped = eEnum;
                            break;
                        }
                    }
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return (Enum) mapped;
    }
}
