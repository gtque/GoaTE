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

package com.thegoate.reflection.test;

/**
 * Created by gtque on 5/4/2017.
 */
public class TestConstructors {
    public String v1 = "";
    public String v2 = "";
    public int v3 = 0;
    public byte b = 0;
    public long l = 0L;
    public double d = 0d;
    public float f = 0f;
    public boolean t = false;
    public char c = 'c';

    public TestConstructors(){}
    public TestConstructors(String v1, String v2){
        this.v1 = v1;
        this.v2 = v2;
    }
    public TestConstructors(String v1, int v3){
        this.v1 = v1;
        this.v3 = v3;
    }
    public TestConstructors(byte o){
        this.b = o;
    }
    public TestConstructors(long o){
        this.l = o;
    }
    public TestConstructors(double o){
        this.d = o;
    }
    public TestConstructors(float o){
        this.f = o;
    }
    public TestConstructors(boolean o){
        this.t = o;
    }
    public TestConstructors(char o){
        this.c = o;
    }
}
