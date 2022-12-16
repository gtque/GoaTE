package com.thegoate.utils.fill.serialize.pojos;

import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.Kid;
import com.thegoate.utils.fill.serialize.model.ModelBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;

public class APrimitivesPojo extends Kid {

    byte aByte;
    Byte bByte;
    double aDouble;
    Double bDouble;
    float aFloat;
    Float bFloat;
    short aShort;
    Short bShort;
    int aInt;
    Integer bInt;
    boolean aBoolean;
    Boolean bBoolean;
    String aString;
    char aChar;
    Character bChar;
    BigInteger aBigInteger;
    @GoateSource(key = "Trashbinye", source = ModelBuilder.class)
    BigDecimal aBigDecimal;

}
