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

import java.math.BigDecimal;

/**
 * Returns the sum (as a string) calculated using BigDecimal.
 * Created by gtque on 9/20/2017.
 */
@GoateDSL(word = "sum")
@GoateDescription(description = "Sums all the entries in the goate collection, returned as a string.",
        parameters = {"The filter to apply to the goate data, optional - if omitted will sum every numeric from the goate collection."})
public class SumFieldsDSL extends DSL {
    Goate sumFields = new Goate();
    public SumFieldsDSL(Object value) {
        super(value);
    }

    public static SumFieldsDSL sum(){
        return new SumFieldsDSL("sum::");
    }

    public static SumFieldsDSL sum(String fieldPattern){
        return new SumFieldsDSL("sum::"+fieldPattern);
    }

    public SumFieldsDSL add(Object value){
        sumFields.put(""+sumFields.size(), value);
        return this;
    }

    public String calculate() {
        return calculate(new Goate());
    }

    public String calculate(Goate data){
        return ""+evaluate(data);
    }

    @Override
    public Object evaluate(Goate data) {
        BigDecimal sum = new BigDecimal("0");

        String filter = "" + get(1,data);
        Goate filtered = data;
        if(!filter.equalsIgnoreCase("null")&&!filter.isEmpty()){
            filtered = filtered.filter(filter);
        }
        sumFields.merge(filtered,false);
        for(String key:sumFields.keys()){
            try {
                sum = sum.add(new BigDecimal("" + sumFields.get(key)));
            }catch(Exception e){
                LOG.debug("Sum","Problem adding: " + sumFields.get(key), e);
            }
        }
        return sum.toString();
    }
}
