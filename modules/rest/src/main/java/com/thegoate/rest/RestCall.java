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
package com.thegoate.rest;

import com.thegoate.Goate;
import com.thegoate.annotations.AnnotationFactory;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.rest.staff.ApiEmployee;
import com.thegoate.staff.GoateJob;

import java.lang.reflect.InvocationTargetException;

import static com.thegoate.rest.Rest.typeSeparator;

/**
 * Wrapper for programmatically using the REST framework in GoaTE.
 * Created by Eric Angeli on 11/8/2018.
 */
public class RestCall<T> {
    BleatBox LOG = BleatFactory.getLogger(getClass());
    public static final String ENABLE_LOG = "_goate_rest_log_enabled_";
    protected Goate definition = new Goate();

    public RestCall<T> disableLog(){
        definition.put(ENABLE_LOG, false);
        return this;
    }

    public RestCall<T> enableLog(){
        definition.put(RestCall.ENABLE_LOG, true);
        return this;
    }

    public RestCall<T> clear(){
        definition = new Goate();
        return this;
    }

    public RestCall<T> baseURL(String url) {
        definition.put("base url", url);
        return this;
    }

    public RestCall<T> security(String security) {
        definition.put("security", security);
        return this;
    }

    public RestCall<T> timeout(int timeout) {
        definition.put("rest.timeout", timeout);
        return this;
    }

    public RestCall<T> customParam(String key, String param) {
        definition.put("custom params.##", key + ":=" + param);
        return this;
    }

    public RestCall<T> multipart(String key, Object param, String contentType){
        if(contentType!=null){
            key += typeSeparator + contentType;
        }
        return multipart(key, param);
    }

    public RestCall<T> multipart(String key, Object param) {
        definition.put("multipart##", key + ":=o::_goate_mp-" + paramObjectName(param));
        definition.put("_goate_mp-"+paramObjectName(param), param);
        return this;
    }

    public RestCall<T> formParam(String key, String param) {
        definition.put("form params.##", key + ":=" + param);
        return this;
    }

    public RestCall<T> pathParam(String key, String param) {
        definition.put("path params.##", key + ":=" + param);
        return this;
    }

    public RestCall<T> queryParam(String key, String param) {
        definition.put("query params.##", key + ":=" + param);
        return this;
    }

    public RestCall<T> queryParam(String key, Object param) {
        definition.put("query params.##", key + ":=o::_goate_qp-"+paramObjectName(param));
        definition.put("_goate_qp-" + paramObjectName(param), param);
        return this;
    }

    public RestCall<T> urlParam(String key, String param) {
        definition.put("url params.##", key + ":=" + param);
        return this;
    }

    public RestCall<T> header(String key, String param) {
        definition.put("headers.##", key + ":=" + param);
        return this;
    }

    public RestCall<T> body(Object body) {
        definition.put("body", body);
        return this;
    }

    public RestCall<T> configure(Object config) {
        definition.put("config", config);
        return this;
    }

    public T get(String endpoint) {
        definition.put("end point", endpoint);
        return execute("get");
    }

    public T put(String endpoint) {
        definition.put("end point", endpoint);
        return execute("put");
    }

    public T post(String endpoint) {
        definition.put("end point", endpoint);
        return execute("post");
    }

    public T patch(String endpoint) {
        definition.put("end point", endpoint);
        return execute("patch");
    }

    public T delete(String endpoint) {
        definition.put("end point", endpoint);
        return execute("delete");
    }

    public T head(String endpoint) {
        definition.put("end point", endpoint);
        return execute("head");
    }

    public RestCall<T> urlEncode(boolean encode) {
        definition.put("urlEncode", encode);
        return this;
    }

    protected String paramObjectName(Object param){
        String name = "" + param;
        return name.replace(",",";");
    }

    private T execute(String method) {
        definition.put("method", method);
        AnnotationFactory af = new AnnotationFactory();
        T result = null;
        try {
            ApiEmployee<T> worker = (ApiEmployee) af.annotatedWith(GoateJob.class).find(method).using("jobs").build();
            worker.init(definition);
            result = worker.work();
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            LOG.error("Barn API Init", "problem finding something to execute a " + method + "\nmake sure you have an implementation library included.", e);
        }
        return result;
    }

}
