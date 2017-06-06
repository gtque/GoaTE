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
import com.thegoate.annotations.AnnotationFactory;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Builds the dictionary and translates the words.
 * The dictionary is only built one time, the first time an interpreter is needed, and cannot be dynamically modified during test execution.
 * Created by gtque on 4/19/2017.
 */
public class Interpreter {
    BleatBox LOG = BleatFactory.getLogger(getClass());
    Goate data;
    Map<String, Class> dictionary = null;

    public Interpreter(Goate data) {
        this.data = data;
        if (this.data == null) {
            this.data = new Goate();
        }
        initDictionary();
    }

    public Object translate(Object value) {
        String check = "" + value;
        if (check.contains("::")) {
            check = check.substring(0, check.indexOf("::"));
            return translate(check, value);
        }
        return value;
    }
    /**
     * Translates the value based on the given dsl word.
     *
     * @param dsl   The dsl word to be translated, if not found the value is simply returned unprocessed.
     * @param value The value, aka parameters, to be translated.
     * @return The translated value.
     */
    public Object translate(String dsl, Object value) {
        DSL word = build(dsl, value);
        if (word != null) {
            value = word.evaluate(data);
        }
        return value;
    }

    /**
     * Builds the instance of the dsl to be used for translating.
     *
     * @param word  The word to be translated
     * @param value The information to be translated.
     * @return DSL The instance of the word that can be used for translating.
     */
    protected DSL build(String word, Object value) {
        DSL dsl = null;
        if (dictionary != null) {
            if (dictionary.containsKey(word)) {
                try {
                    dsl = (DSL) dictionary.get(word).getConstructor(Object.class).newInstance(value);
                } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                    LOG.error("Problem translating: " + e.getMessage(), e);
                }
            }
        }
        return dsl;
    }

    /**
     * Builds the dictionary.
     * Looks for anything annoted as a GoateDSL and adds it to the dictionary.
     * The last instance a word wins, ie there will only be one version of a word in the dictionary
     * which may produce unexpected results if more than one uses the same word.
     */
    protected void initDictionary() {
        try {
            if (dictionary == null) {
                new AnnotationFactory().using(GoateDSL.class.getMethod("word")).doDefault().annotatedWith(GoateDSL.class).buildDirectory();
            }
            dictionary = AnnotationFactory.directory.get(GoateDSL.class.getCanonicalName());
        } catch (Throwable e) {
            LOG.error("Problem initializing dsl: " + e.getMessage(), e);
        }
    }
}
