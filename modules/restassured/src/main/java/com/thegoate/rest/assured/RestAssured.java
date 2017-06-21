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
package com.thegoate.rest.assured;

import com.thegoate.Goate;
import com.thegoate.annotations.IsDefault;
import com.thegoate.logging.BleatBox;
import com.thegoate.rest.Rest;
import com.thegoate.rest.RestSpec;
import com.thegoate.rest.annotation.GoateRest;
import io.restassured.config.EncoderConfig;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.*;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.config.HeaderConfig.headerConfig;

/**
 * REST Assured implementation.
 * Created by Eric Angeli on 5/16/2017.
 */
@GoateRest
@IsDefault
public class RestAssured extends Rest implements RASpec {
    RequestSpecification specification = null;
    Response response = null;

    public RestAssured() {
        this.specification = RestAssured.init(given(), this);
    }

    public RestAssured(RequestSpecification specification) {
        this.specification = RestAssured.init(specification, this);
    }

    public static RequestSpecification init(RequestSpecification specification, RASpec spec) {
        specification = specification == null ? given() : specification;
        RestAssuredConfig rac = new RestAssuredConfig();
        PrintStream streamer = getPrintStream(spec.getLog());
        LogConfig lc = new LogConfig(streamer, true);
        SSLConfig sslc = new SSLConfig().allowAllHostnames().relaxedHTTPSValidation();
        rac = rac.sslConfig(sslc);
        rac = rac.logConfig(lc);
        rac = rac.headerConfig(headerConfig()
                .overwriteHeadersWithName("Authorization")
                .overwriteHeadersWithName("Content-Type"));
        rac = rac.encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
        return specification.config(rac);
    }

    public static RequestSpecification build(RASpec spec) {
        RequestSpecification mySpec = spec.getSpec();
        if (mySpec != null) {
            mySpec.baseUri(spec.getBaseURL());
            setHeaders(mySpec, spec.getHeaders());
            setURLParameters(mySpec, spec.getURLParameters());
            setQueryParameters(mySpec, spec.getQueryParameters());
            setPathParameters(mySpec, spec.getPathParameters());
            setBody(mySpec, spec.getBody());
            if (spec.doLog()) {
                mySpec.log().all();
                spec.getLog().flush();
            }
        }
        return mySpec;
    }

    @Override
    public BleatBox getLog(){
        return LOG;
    }

    /**
     * COPIED FROM STACKOVERFLOW.
     *
     * @return printStream
     */
    public static PrintStream getPrintStream(BleatBox log) {
        PrintStream stream;
        OutputStream output = new OutputStream() {
            BleatBox logger = log;
            private StringBuilder myStringBuilder = new StringBuilder();

            @Override
            public void write(int b) throws IOException {
                this.myStringBuilder.append((char) b);
            }

            /**
             * @see java.io.OutputStream#flush()
             */
            @Override
            public void flush() {
                logger.infoBuffer(this.myStringBuilder.toString());
                myStringBuilder = new StringBuilder();
            }
        };
        stream = new PrintStream(output, true);  // true: autoflush must be set!
        return stream;
    }

    protected static void setHeaders(RequestSpecification spec, Goate headers) {
        if (headers != null && spec != null) {
            for (String key : headers.keys()) {
                spec.header(key, headers.get(key));
            }
        }
    }

    protected static void setURLParameters(RequestSpecification spec, Goate params) {
        if (params != null && spec != null) {
            for (String key : params.keys()) {
                spec.param(key, params.get(key));
            }
        }
    }

    protected static void setQueryParameters(RequestSpecification spec, Goate params) {
        if (params != null && spec != null) {
            for (String key : params.keys()) {
                spec.queryParam(key, params.get(key));
            }
        }
    }

    protected static void setPathParameters(RequestSpecification spec, Goate params) {
        if (params != null && spec != null) {
            for (String key : params.keys()) {
                spec.pathParam(key, params.get(key));
            }
        }
    }

    protected static void setBody(RequestSpecification spec, Goate body) {
        if (spec != null && body != null) {
            for (String key : body.keys()) {
                Goate b = (Goate) body.get(key);
                if (b != null) {
                    for (String id : b.keys()) {
                        if (key.equals(BODY.urlencoded.name()) || key.equals(BODY.form.name())) {
                            spec.formParam(id, b.get(id));
                        } else if (key.equals(BODY.multipart.name())) {
                            if (id.startsWith(MP_ID_NOT_SET)) {
                                spec.multiPart((File) b.get(id));
                            } else {
                                spec.multiPart(id, b.get(id));
                            }
                        } else {
                            spec.body(b.get(id));
                        }
                    }
                }
            }
        }
    }

    @Override
    public Object response() {
        return response;
    }

    @Override
    public RestSpec processCustomData(String key, Object value) {
        return this;
    }

    @Override
    /**
     * This is a null operation for the base RestAssured class.
     */
    public RestSpec processCustomData(Enum key, Object value) {
        return this;
    }

    @Override
    public Object get(String endpoint) {
        specification = RestAssured.build(this);
        response = specification.get(endpoint);
        log(response);
        return response;
    }

    @Override
    public Object put(String endpoint) {
        specification = RestAssured.build(this);
        response = specification.put(endpoint);
        log(response);
        return response;
    }

    @Override
    public Object post(String endpoint) {
        specification = RestAssured.build(this);
        response = specification.post(endpoint);
        log(response);
        return response;
    }

    @Override
    public Object delete(String endpoint) {
        specification = RestAssured.build(this);
        response = specification.delete(endpoint);
        log(response);
        return response;
    }

    @Override
    public Object patch(String endpoint) {
        specification = RestAssured.build(this);
        response = specification.patch(endpoint);
        log(response);
        return response;
    }

    protected void log(Response response) {
        if (doLog()) {
            LOG.info("response follows");
            response.then().log().all();
            LOG.flush();
        }
    }

    @Override
    public RequestSpecification getSpec() {
        return specification;
    }
}
