/*
 * Copyright (c) 2017. Eric Angeli
 *
 *  Permission is hereby granted, free of charge,
 *  to any person obtaining a copy of this software
 *  and associated documentation files (the "Software"),
 *  to deal in the Software without restriction,
 *  including without limitation the rights to use, copy,
 *  modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit
 *  persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission
 *  notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 *  AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 */
package com.thegoate.utils.fill.serialize.pojos;

import com.thegoate.utils.fill.serialize.GoatePojo;
import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.IgnoreOnCompare;
import com.thegoate.utils.fill.serialize.Kid;

import java.math.BigDecimal;

/**
 * Created by Eric Angeli on 6/26/2018.
 */
@GoatePojo(id = "simple pojo")
public class SimplePojo extends Kid {

    private String fieldName = "what?";

    @GoateSource(source = SimpleSource.class, key = "expanded name")
    private int someInt = 0;

    @GoateSource(source = SimpleSource.class, key = "big decimal")
    private BigDecimal bigD = null;

    @GoateSource(source = SecondSource.class, key = "double")
    @GoateSource(source = SimpleSource.class, key = "double D")
    private Double d = null;

    @GoateSource(source = SimpleSource.class, key = "long l")
    @GoateSource(source = SecondSource.class, key = "l")
    private long l = 0L;

    private char c = 'c';

    private Byte aByte = new Byte("1");

    private boolean bool = true;

    //    @GoateSource(expected = SimpleSource.class, key = "floaty mcfloatface")
    private float f = 4f;

    @IgnoreOnCompare
    private String ignoreMe = "found them";

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getSomeInt() {
        return someInt;
    }

    public void setSomeString(int someInt) {
        this.someInt = someInt;
    }

    public BigDecimal getBigD() {
        return bigD;
    }

    public void setBigD(BigDecimal bigD) {
        this.bigD = bigD;
    }

    public Double getD() {
        return d;
    }

    public void setD(Double d) {
        this.d = d;
    }

    public long getL() {
        return l;
    }

    public void setL(long l) {
        this.l = l;
    }

    public char getC() {
        return c;
    }

    public void setC(char c) {
        this.c = c;
    }

    public Byte getaByte() {
        return aByte;
    }

    public void setaByte(Byte aByte) {
        this.aByte = aByte;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public float getF() {
        return f;
    }

    public void setF(float f) {
        this.f = f;
    }

    public String getIgnoreMe() {
        return ignoreMe;
    }

    public void setIgnoreMe(String ignoreMe) {
        this.ignoreMe = ignoreMe;
    }
}
