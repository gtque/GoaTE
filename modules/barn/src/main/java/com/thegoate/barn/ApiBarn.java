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
package com.thegoate.barn;

import com.thegoate.Goate;

/**
 * Base class for implementing Barn api tests.
 * Created by Eric Angeli on 5/22/2017.
 */
public abstract class ApiBarn extends Barn {

    public ApiBarn(){
        super();
        label = "api";
        baseTestCaseDir += "/apis";
    }

    /**
     * Make sure you add the factor annotation to the subclasses implementation of this constructor.<br>
     * The data provider can be set to "dataLoader".
     * @param data the run data
     */
    public ApiBarn(Goate data){
        super(data);
    }

    /**
     * You need to implement this class and annotate it with<br>
     * {@literal @}Test and you should probably add a group called "api" to the annotation.<br>
     * inside the method simply call execute();
     */
    public abstract void apiTest();
}
