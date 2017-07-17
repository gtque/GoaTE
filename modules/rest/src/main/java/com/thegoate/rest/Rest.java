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
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;


/**
 * Base RestSpec class for doing restful api calls.
 * Created by Eric Angeli on 5/16/2017.
 */
public abstract class Rest implements RestSpec {
    protected final BleatBox LOG = BleatFactory.getLogger(getClass());

    protected Goate headers = new Goate();
    protected Goate queryParams = new Goate();
    protected Goate urlParams = new Goate();
    protected Goate pathParams = new Goate();
    protected Goate body = new Goate();
    protected Goate custom = new Goate();
    protected String baseURL = "";//this should include the port if different from default.
    protected int timeout = 15;
    protected boolean logAll = true;
    public static final String MP_ID_NOT_SET = "_mp_id_not_set";
    public enum BODY {
        form, urlencoded, raw, binary, multipart
    }

    BODY bodyFormat = BODY.raw;

    @Override
    public boolean doLog(){
        return logAll;
    }

    @Override
    public String getBaseURL(){
        return baseURL;
    }

    @Override
    public RestSpec baseUri(String uri){
        return baseURL(uri);
    }

    @Override
    public RestSpec baseURL(String url){
        this.baseURL = url;
        return this;
    }

    @Override
    public RestSpec enableLog() {
        this.logAll = true;
        return this;
    }

    @Override
    public RestSpec disableLog() {
        this.logAll = false;
        return this;
    }

    @Override
    public RestSpec headers(Goate data) {
        if(data!=null) {
            for (String key : data.keys()) {
                header(key, data.get(key));
            }
        }
        return this;
    }

    @Override
    public RestSpec header(String key, Object value) {
        headers.put(key, value);
        return this;
    }

    @Override
    public RestSpec queryParams(Goate data) {
        if(data!=null) {
            for (String key : data.keys()) {
                queryParam(key, data.get(key));
            }
        }
        return this;
    }

    @Override
    public RestSpec queryParam(String key, Object value) {
        queryParams.put(key, value);
        return this;
    }

    @Override
    public RestSpec urlParams(Goate data) {
        if(data!=null) {
            for (String key : data.keys()) {
                urlParam(key, data.get(key));
            }
        }
        return this;
    }

    @Override
    public RestSpec urlParam(String key, Object value) {
        urlParams.put(key, value);
        return this;
    }

    @Override
    public RestSpec pathParams(Goate data) {
        if(data!=null) {
            for (String key : data.keys()) {
                pathParam(key, data.get(key));
            }
        }
        return this;
    }

    @Override
    public RestSpec pathParam(String key, Object value) {
        pathParams.put(key, value);
        return this;
    }

    @Override
    public RestSpec bodyAsFormData() {
        bodyFormat = BODY.form;
        return this;
    }

    @Override
    public RestSpec bodyAsFormUrlencodedData() {
        bodyFormat = BODY.urlencoded;
        return this;
    }

    @Override
    public RestSpec bodyAsRawData() {
        bodyFormat = BODY.raw;
        return this;
    }

    @Override
    public RestSpec bodyAsMultipartFormData() {
        bodyFormat = BODY.multipart;
        return this;
    }

    @Override
    public RestSpec bodyAsBinaryData() {
        bodyFormat = BODY.binary;
        return this;
    }

    @Override
    public RestSpec formData(Goate data) {
        if(data!=null) {
            for (String key : data.keys()) {
                formData(key, data.get(key));
            }
        }
        return this;
    }

    @Override
    public RestSpec formData(String key, Object value) {
        bodyFormat = BODY.form;
        return body(bodyFormat, key, value);
    }

    @Override
    public RestSpec formUrlencodedData(String key, Object value) {
        bodyFormat = BODY.urlencoded;
        return body(bodyFormat, key, value);
    }

    @Override
    public RestSpec binaryBody(Object value) {
        bodyFormat = BODY.binary;
        return body(value);
    }

    @Override
    public RestSpec rawBody(Object value) {
        bodyFormat = BODY.raw;
        return body(value);
    }

    @Override
    public RestSpec multipartData(Goate data) {
        if(data!=null) {
            for (String key : data.keys()) {
                multipartFormData(key.isEmpty()?(MP_ID_NOT_SET+"##"):key, data.get(key));
            }
        }
        return this;
    }

    @Override
    public RestSpec multipartFormData(Object value) {
        return multipartFormData(MP_ID_NOT_SET+"##", value);
    }

    @Override
    public RestSpec multipartFormData(String key, Object value) {
        bodyFormat = BODY.multipart;
        return body(bodyFormat, key, value);
    }

    @Override
    public RestSpec customData(Goate data){
        if(data!=null) {
            for (String key : data.keys()) {
                custom.put(key,data.get(key));
                processCustomData(key, data.get(key));
            }
        }
        return this;
    }

    @Override
    public RestSpec customData(Enum key, Object value) {
        custom.put(key.name(), value);
        return processCustomData(key, value);
    }

    @Override
    public RestSpec binaryBodyData(Object value) {
        bodyFormat = BODY.binary;
        return body(value);
    }

    @Override
    public RestSpec body(Object value) {
        return body(bodyFormat, "body", value);
    }

    @Override
    public RestSpec body(Enum type, String key, Object value) {
        body.get(type.name(), new Goate(), false, Goate.class).put(key, value);
        return this;
    }

    @Override
    public Goate getQueryParameters(){
        return queryParams;
    }

    @Override
    public Goate getURLParameters(){
        return urlParams;
    }

    @Override
    public Goate getPathParameters(){
        return pathParams;
    }

    @Override
    public Goate getHeaders(){
        return headers;
    }

    @Override
    public Goate getBody(){
        return body;
    }

    @Override
    public RestSpec timeout(int timeoutSeconds){
        this.timeout = timeoutSeconds;
        return this;
    }

    @Override
    public int getTimeout(){
        return timeout;
    }
}
