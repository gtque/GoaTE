package com.thegoate.rest.assured.utils.type;

import com.thegoate.annotations.IsDefault;
import com.thegoate.utils.type.FindType;
import com.thegoate.utils.type.IsType;
import io.restassured.response.Response;

@IsType
@IsDefault(forType = true)
public class RAResponseType extends FindType {

    @Override
    public boolean isType(Object check){
        return check instanceof Response;
    }

    @Override
    public Class type(Object check){
        return Response.class;
    }
}
