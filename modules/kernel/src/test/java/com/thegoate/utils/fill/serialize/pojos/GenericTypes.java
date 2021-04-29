package com.thegoate.utils.fill.serialize.pojos;

import com.thegoate.utils.fill.serialize.IsTypeT;
import com.thegoate.utils.fill.serialize.Kid;

public class GenericTypes<T,U,V> extends Kid {

    @IsTypeT
    private T a;
    @IsTypeT(index = 1)
    private U b;
    @IsTypeT(index = 2)
    private V c;

    public T getA() {
        return a;
    }

    public GenericTypes setA(T a) {
        this.a = a;
        return this;
    }

    public U getB() {
        return b;
    }

    public GenericTypes setB(U b) {
        this.b = b;
        return this;
    }

    public V getC() {
        return c;
    }

    public GenericTypes setC(V c) {
        this.c = c;
        return this;
    }
}
