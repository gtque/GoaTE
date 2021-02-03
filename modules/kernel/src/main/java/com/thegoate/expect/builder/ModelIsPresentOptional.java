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
package com.thegoate.expect.builder;

import com.thegoate.Goate;
import com.thegoate.expect.Expectation;
import com.thegoate.expect.validate.ValidatePresence;
import com.thegoate.locate.Locate;
import com.thegoate.utils.togoate.ToGoate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric Angeli on 3/26/2019.
 */
public class ModelIsPresentOptional extends ExpectationBuilder {

    //    protected Object requiredModel;
    protected List<String> optionalModel = new ArrayList<>();

    @Override
    public Object getExpected() {
        Goate model = new ToGoate(super.getExpected()).convert();
        if (model == null) {
            model = new Goate();
        }
        return model;
    }

    private boolean someParentIsOptional(String key) {
        boolean optional = false;
        String parent = key;
        while (parent.contains(".")) {
            if (isOptional(parent)) {
                optional = true;
            }
            parent = parent.substring(0, parent.lastIndexOf('.'));
        }
        return optional;
    }

    private boolean isOptional(String key) {
        return optionalModel.parallelStream().anyMatch(key::matches);
    }

    private Goate findParentsMatchingPattern(String key, Goate results) {
        String pattern = key.substring(0, key.lastIndexOf('.'))
                .replace(".", "\\.")
                .replaceAll("[0-9]+", "[0-9]*");
        return results.filterStrict(pattern);
    }

    @Override
    public List<Expectation> build() {
        Goate model = new ToGoate(getExpected()).convert();
        Goate results = new ToGoate(getActual()).convert();
        for (String key : model.keys()) {
            if (!isOptional(key)) {
                if (someParentIsOptional(key)) {
                    Goate parents = findParentsMatchingPattern(key, results);
                    for (String pKey : parents.keys()) {
                        addExpect(pKey + key.substring(key.lastIndexOf('.')));
                    }
                } else {
                    addExpect(key.replaceAll("\\.[0-9]+", ".+"));
                }
            }
        }
        return getExpectations();
    }

    private void addExpect(String key) {
        String actualKey = key;
        if(from!=null){
            if((""+actual).contains("*")||(""+actual).contains("+")) {
                actualKey = ""+actual + "." + actualKey;
            }
        }
        expect(Expectation.build()
                .actual(actualKey)
                .from(getActual())
                .isPresent(true)
                .validate(new ValidatePresence()));
    }

    public ModelIsPresentOptional addOptionalField(String field) {
        optionalModel.add(field);
        return this;
    }

    public ModelIsPresentOptional addOptionalField(Locate path) {
        return addOptionalField(path.toPath());
    }
}
