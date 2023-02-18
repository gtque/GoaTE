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

package com.thegoate.dsl.words;

import com.thegoate.Goate;
import com.thegoate.annotations.GoateDescription;
import com.thegoate.dsl.DSL;
import com.thegoate.dsl.GoateDSL;
import com.thegoate.utils.fill.serialize.DeSerializer;

/**
 * Builds and fills a pojo
 * Created by gtque on 4/28/2021.
 */
@GoateDSL(word = "pojo")
@GoateDescription(description = "Returns the filled object filled with corresponding data from the goate collection." +
        "\n The pojo is filled out using the current goate collection.",
    parameters = {"the class to be filled, needs to be the fully qualified package and class name, or use the class dsl or o dsl to reference a Class object",
    "the rest of the parameters are a comma separated list of generic type declarations if the pojo includes generics."})
public class PojoDSL extends DSL {
    Goate fillData;

    public PojoDSL(Object value) {
        super(value);
        fillData = new Goate();
    }

    public static PojoDSL pojo(Object value){
        return new PojoDSL("pojo::"+value);
    }

    public PojoDSL put(Goate data){
        fillData.merge(data,false);
        return this;
    }

    public PojoDSL put(String key, Object value){
        fillData.put(key, value);
        return this;
    }

    public Object evaluate(){
        return evaluate(fillData);
    }

    @Override
    public Object evaluate(Goate data) {
        Class klass = findClass(get(1, data));
        DeSerializer ds = new DeSerializer().data(data);
        for(int i = 2; i<definition.size(); i++) {
            ds.T(findClass(get(i, data)));
        }
        return ds.build((Class) klass);
    }

    private Class findClass(Object klass) {
        Class theClass = null;
        if(klass instanceof Class) {
            theClass = (Class) klass;
        } else {
            try {
                theClass = Class.forName("" + klass);
            } catch (ClassNotFoundException e) {
                LOG.warn("Failed to find the class: " + klass);
            }
        }
        return theClass;
    }
}
