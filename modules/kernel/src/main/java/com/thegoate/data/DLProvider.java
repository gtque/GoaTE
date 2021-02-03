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
package com.thegoate.data;

import com.thegoate.Goate;

/**
 * Simple data loader container.
 * This can be used to predefine a provider and then reuse it using.
 * Created by Eric Angeli on 5/5/2017.
 */
public abstract class DLProvider {
    protected Goate runData = new Goate();
    protected Goate constantData = new Goate();
    public static final int size = 2;
    public static final int runs = 0;
    public static final int constants = 1;
    public DLProvider data(Goate data){
        this.runData = data;
        return this;
    }
    public DLProvider constants(Goate data){
        this.constantData = data;
        return this;
    }

    public Goate getRunDataLoaders(){
        return this.runData;
    }

    public Goate getConstantDataLoaders(){
        return this.constantData;
    }

    /**
     * Override init to define the run data and the constant data.
     */
    public abstract void init();
}
