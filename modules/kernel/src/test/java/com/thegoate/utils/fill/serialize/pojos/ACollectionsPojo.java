package com.thegoate.utils.fill.serialize.pojos;

import com.thegoate.utils.fill.serialize.Kid;
import com.thegoate.utils.fill.serialize.collections.ListType;
import com.thegoate.utils.fill.serialize.collections.MapKeyType;
import com.thegoate.utils.fill.serialize.collections.MapType;

import java.util.List;
import java.util.Map;

public class ACollectionsPojo extends Kid {
    @ListType(type = String.class)
    List<String> aList;
    Integer[] aIntArray;
    @MapType(type = String.class)
    Map<?, String> aMap;
    @MapType(type = Boolean.class)
    @MapKeyType(type = String.class)
    Map<String, Boolean> bMap;
}
