package com.thegoate.spreadsheets.utils.type;

import com.thegoate.annotations.IsDefault;
import com.thegoate.spreadsheets.utils.SheetUtils;
import com.thegoate.utils.type.FindType;
import com.thegoate.utils.type.IsType;

@IsType
@IsDefault(forType = true)
public class SheetUtilsType extends FindType {

    @Override
    public boolean isType(Object check){
        return check instanceof SheetUtils;
    }

    @Override
    public Class type(Object check){
        return SheetUtils.class;
    }
}
