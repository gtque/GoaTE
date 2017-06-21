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

package com.thegoate.dsl;

import com.thegoate.Goate;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The base definition for a new word in the DSL.<br>
 * Words should extend DSL and be annotated with the {@literal @}GoateDSL annotation<br>
 * When referencing the word it should be terminated with double colons (::)<br>
 * Parameters for the word should come after :: and be comma (,) separated.
 * Created by gtque on 4/20/2017.
 */
public abstract class DSL {
    protected final BleatBox LOG = BleatFactory.getLogger(getClass());
    protected List<String> definition;
    protected Object value;

    public DSL(){}

    public DSL(Object value){
        define(value);
    }

    public String type(){
        String type = "undefined";

        if(definition!=null){
            if(definition.size()>0){
                type = definition.get(0);
            }
        }
        return type;
    }

    protected Object get(int index, Goate data){
        Object o = null;
        if(definition.size()>index) {
            o = data.processDSL(definition.get(index));
        }
        return o;
    }


    public List<String> define(Object value){
        this.value = value;
        definition = new ArrayList<>();
        String v = "" + value;
        definition.add(v.substring(0,v.indexOf("::")));
        String[] def = (v.substring(v.indexOf("::")+2)).split(",");
        for(String d:def){
            definition.add(d);
        }
        return definition;
    }

    public abstract Object evaluate(Goate data);
}
