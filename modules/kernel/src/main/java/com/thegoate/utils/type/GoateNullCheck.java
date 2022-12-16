package com.thegoate.utils.type;

import com.thegoate.utils.UnknownUtilType;
import com.thegoate.utils.get.NotFound;

public class GoateNullCheck extends UnknownUtilType {

    public static boolean isNull(Object check){
        return new GoateNullCheck().checkNull(check);
    }

    protected boolean checkNull(Object check){
        Object[] checkArgs = {check};
        Object tool = uncached(IsNullRepresentation.class, checkArgs, checkArgs, "", null, "isNull", NotFound.class);
        return tool!=null;
    }

    @Override
    public boolean checkType(Class tool, Class type) {
        return true;
    }
}
