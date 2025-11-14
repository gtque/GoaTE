package com.thegoate.rest.retrofit.utils.type;

import com.thegoate.utils.type.FindType;
import com.thegoate.utils.type.IsType;
import retrofit2.Response;

@IsType
public class RetrofitResponseType extends FindType {

    @Override
    public boolean isType(Object check){
        return check instanceof Response;
    }

    @Override
    public Class type(Object check){
        return Response.class;
    }
}
