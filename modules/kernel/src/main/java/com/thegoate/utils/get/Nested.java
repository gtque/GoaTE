package com.thegoate.utils.get;

/**
 * Created by Eric Angeli on 12/5/2018.
 */
public class Nested {
    public static String nested(String... args){
        StringBuilder nested = new StringBuilder(args[0]);
        for(int index = 1; index<args.length; index++){
            nested.append(">").append(args[index]);
        }
        return nested.toString();
    }
}
