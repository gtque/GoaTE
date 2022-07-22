package com.thegoate.utils.fill.serialize.pojos;

import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.Kid;

public class FunkyKey extends Kid {

    @GoateSource(source = FunkyKey.class, key = "urn:ietf:params:scim:schemas:extension:enterprise:2\\.0:User\\.manager\\.value")
    String id = "well hello there...";
}
