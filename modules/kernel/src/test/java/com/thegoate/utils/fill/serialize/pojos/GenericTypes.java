package com.thegoate.utils.fill.serialize.pojos;

import com.thegoate.utils.fill.serialize.Generic;
import com.thegoate.utils.fill.serialize.Kid;

public class GenericTypes<T,U,V> extends Kid {

    @Generic
    private T a;
    @Generic(index = 1)
    private U b;
    @Generic(index = 2)
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
