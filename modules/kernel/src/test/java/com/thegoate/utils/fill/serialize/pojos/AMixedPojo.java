package com.thegoate.utils.fill.serialize.pojos;

import com.thegoate.utils.fill.serialize.Kid;
import com.thegoate.utils.fill.serialize.collections.ListType;

import java.time.LocalDate;
import java.util.List;

public class AMixedPojo extends Kid {

    LocalDate theDate;

    APrimitivesPojo nestedPojo;

    @ListType(type = ACollectionsPojo.class)
    List<ACollectionsPojo> nestedList;
}
