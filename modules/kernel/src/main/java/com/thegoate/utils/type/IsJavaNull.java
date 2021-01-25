package com.thegoate.utils.type;

@IsNullRepresentation
public class IsJavaNull implements NullRepresentation {
    @Override
    public boolean isNull(Object check) {
        return check == null;
    }
}
