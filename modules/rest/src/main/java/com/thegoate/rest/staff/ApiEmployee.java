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
package com.thegoate.rest.staff;

import com.thegoate.annotations.AnnotationFactory;
import com.thegoate.rest.RestSpec;
import com.thegoate.rest.annotation.GoateRest;
import com.thegoate.staff.Employee;

import java.lang.reflect.InvocationTargetException;

/**
 * Defines the base API employee.<br>
 * Adds the functionality for extracting and setting
 * the data and looking up the right Rest wrapper implementation.
 * Created by Eric Angeli on 5/17/2017.
 */
public abstract class ApiEmployee extends Employee {
    RestSpec rest = null;

    @Override
    public Employee init() {
        String security = "" + data.get("security", "none");
        try {
            rest = findAndBuildRestSpec(security);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            LOG.error("failed to find a valid rest implementation for : " + security +"\n"+e.getMessage());
        }
        return this;
    }

    protected RestSpec findAndBuildRestSpec(String security) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        RestSpec spec = null;
        AnnotationFactory af = new AnnotationFactory();
        spec = (RestSpec)af.annotatedWith(GoateRest.class).find(security).using("security").build();
        if(spec!=null) {
            spec.baseURL(data.get("base url", null, true, String.class));
            spec.headers(data.filterAndSplitKeyValuePairs("header"));
            spec.urlParams(data.filterAndSplitKeyValuePairs("url param"));
            spec.queryParams(data.filterAndSplitKeyValuePairs("query param"));
            spec.pathParams(data.filterAndSplitKeyValuePairs("path param"));
            if(data.get("body")!=null)
            spec.body(data.get("body"));
            spec.formData(data.filterAndSplitKeyValuePairs("form param"));
            spec.multipartData(data.filterAndSplitKeyValuePairs("multipart param"));
            spec.customData(data.filterAndSplitKeyValuePairs("custom param"));
        }
        return spec;
    }

}
