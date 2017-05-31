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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Defines the base data loader class.
 * Created by Eric Angeli on 5/5/2017.
 */
public abstract class DataLoader {

    protected Logger LOG = LoggerFactory.getLogger(getClass());

    protected Goate parameters = new Goate();

    public abstract List<Goate> load();//loads the data into separate goate collections, each data set/row is a new collection.
    public DataLoader setParameter(String parameter, Object value){
        if(parameters == null){
            parameters = new Goate();
        }
        parameters.put(parameter, value);
        return this;
    }
    public DataLoader setParameters(Goate params){
        this.parameters = params;
        return this;
    }

}
