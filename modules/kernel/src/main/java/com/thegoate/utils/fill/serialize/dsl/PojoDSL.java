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
package com.thegoate.utils.fill.serialize.dsl;

import com.thegoate.Goate;
import com.thegoate.annotations.AnnotationFactory;
import com.thegoate.annotations.GoateDescription;
import com.thegoate.dsl.DSL;
import com.thegoate.dsl.GoateDSL;
import com.thegoate.utils.fill.serialize.DeSerializer;
import com.thegoate.utils.fill.serialize.GoatePojo;
import com.thegoate.utils.fill.serialize.GoateSourceDef;

/**
 * Created by Eric Angeli on 6/27/2018.
 */
@GoateDSL(word = "pojo")
@GoateDescription(description = "Builds the given pojo from the data from the specified source.",
        parameters = {"the id of the pojo to be built, must be annotated with GoatePojo", "the id of the source the data comes from,(an empty object annotated with GoateSourceDef used in GoateSource annotation [source] on fields in the pojo), this parameter is optional, but recommended."})
public class PojoDSL extends DSL {

    public PojoDSL(Object value) {
        super(value);
    }

    @Override
    public Object evaluate(Goate data) {
        Class pojo = find("" + get(1,data), GoatePojo.class);
        DeSerializer deSerializer = new DeSerializer();
        deSerializer.data(data);
        Object so = get(2, data);
        if(so!=null){
            deSerializer.from(find(""+so, GoateSourceDef.class));
        }
        if(pojo==null){
            LOG.debug("Pojo DSL", "Failed to find the pojo: "+get(1, data)+". Make sure the id is correct.");
        }
        return pojo!=null?deSerializer.build(pojo):null;
    }

    protected Class find(String id, Class annotationType){
        Class found = null;
        try {
            AnnotationFactory af = new AnnotationFactory().using(annotationType.getMethod("id")).doDefault().annotatedWith(annotationType).buildDirectory();
            found = af.find(id).lookUp();
        } catch (NoSuchMethodException e) {
            LOG.error("Pojo DSL", "Problem looking up class: " + e.getMessage(), e);
        }
        return found;
    }
}
