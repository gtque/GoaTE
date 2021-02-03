package com.thegoate.expect.test;

import com.thegoate.utils.fill.serialize.Kid;

import java.math.BigDecimal;

public class SimpleObject2 extends Kid {

    private BigDecimal bd;

    public BigDecimal getBd() {
        return bd;
    }

    public SimpleObject2 setBd(BigDecimal bd) {
        this.bd = bd;
        return this;
    }

}
