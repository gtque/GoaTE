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

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.config.HeaderConfig.headerConfig;
import static io.restassured.config.HttpClientConfig.httpClientConfig;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import com.github.dzieciou.testing.curl.CurlRestAssuredConfigFactory;
import com.github.dzieciou.testing.curl.Options;
import com.github.dzieciou.testing.curl.Platform;
import com.thegoate.Goate;
import com.thegoate.annotations.IsDefault;
import com.thegoate.logging.BleatBox;
import com.thegoate.rest.Rest;
import com.thegoate.rest.RestSpec;
import com.thegoate.rest.annotation.GoateRest;

import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * REST Assured implementation.
 * Created by Eric Angeli on 5/16/2017.
 */
@GoateRest
@IsDefault
public class RestAssured extends Rest implements RASpec {

	RequestSpecification specification = null;
	Response response = null;
	RestAssuredConfig config = null;

	public RestAssured() {
		this.specification = RestAssured.init(given(), this);
	}

	public RestAssured(RequestSpecification specification) {
		this.specification = RestAssured.init(specification, this);
	}

	public static RequestSpecification init(RequestSpecification specification, RASpec spec) {
		specification = specification == null ? given() : specification;
        RestAssuredConfig rac = spec.getConfig() == null ? new RestAssuredConfig() : (RestAssuredConfig) spec.getConfig();
        PrintStream streamer = getPrintStream(spec.getLog());
        LogConfig lc = new LogConfig(streamer, true);
        SSLConfig sslc = new SSLConfig().allowAllHostnames().relaxedHTTPSValidation();
        rac = rac.sslConfig(sslc);
        rac = rac.logConfig(lc);
        if (spec.getHeaders().keys().toArray().length > 0) {
            rac = rac.headerConfig(headerConfig()
				.overwriteHeadersWithName("Content-Type", spec.getHeaders().keysArray()));
		} else {
			rac = rac.headerConfig(headerConfig()
				.overwriteHeadersWithName("Content-Type"));
		}
		int timeout = spec.getTimeout();
		rac = rac.httpClient(httpClientConfig().setParam("CONNECTION_MANAGER_TIMEOUT", timeout * 1000));
		rac = rac.encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		Options options = Options.builder().targetPlatform(Platform.UNIX).build();
		rac = CurlRestAssuredConfigFactory.updateConfig(rac, options);
		//		if(spec.getHeaders().containsKey("Content-Type") && spec.getHeaders().get("Content-Type", "", String.class).equals("application/csp-report")) {
		//			rac.encoderConfig(encoderConfig().encodeContentTypeAs("application/csp-report", ContentType.TEXT));
		//		}
		spec.configure(rac);
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
			setCookies(mySpec, spec.getCookies());
			if (spec.doLog()) {
				mySpec.log().all();
				spec.getLog().flush();
			}
			//always set urlEncoding, the RASpec implementation should default to true.
			mySpec.urlEncodingEnabled(spec.urlEncode());
			//            keeping this as a reference in case I need to expose it for some reason.
			//            io.restassured.RestAssured.urlEncodingEnabled = false;
			if (spec.getHeaders().containsKey("Content-Type") && spec.getHeaders().get("Content-Type", "", String.class).equals("application/csp-report")) {
				mySpec.config(((RestAssuredConfig)spec.getConfig()).encoderConfig(encoderConfig().encodeContentTypeAs("application/csp-report", ContentType.TEXT)));
			}
		}
        return mySpec;
    }

    @Override
    public RestSpec config() {
        init(specification, this);
        return this;
    }

    @Override
    public BleatBox getLog() {
        return LOG;
    }

    /**
     * COPIED FROM STACKOVERFLOW.
     * This high-jacks the stream writer to write to our custom logger implementation.
     * This will always write to info. Writing the response to the logger is not controlled by the logging level,
     * but by a separate setting.
     *
     * @param log The instance of the log implementation to use.
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
                logger.infoBuffer("RestAssured Response", this.myStringBuilder.toString());
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

    protected static void setCookies(RequestSpecification spec, Goate cookies) {
        if (cookies != null && spec != null) {
            for (String key : cookies.keys()) {
                Object cookie = cookies.get(key);
                if (cookie instanceof Cookies) {
                    spec.cookies((Cookies) cookie);
                } else if (cookie instanceof Cookie) {
                    spec.cookie((Cookie) cookie);
                } else {
                    spec.cookie(key, cookies.get(key));
                }
            }
        }
    }

    protected static void setURLParameters(RequestSpecification spec, Goate params) {
        if (params != null && spec != null) {
//            spec.params(params.data());
            for (String key : params.keys()) {
                spec.param(key, params.get(key));
            }
        }
    }

    protected static void setQueryParameters(RequestSpecification spec, Goate params) {
        if (params != null && spec != null) {
//            spec.queryParams(params.data());
            for (String key : params.keys()) {
                spec.queryParam(key, params.get(key));
            }
        }
    }

    protected static void setPathParameters(RequestSpecification spec, Goate params) {
        if (params != null && spec != null) {
//            spec.pathParams(params.data());
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
                        String[] keyParts = id.split(typeSeparator);
                        String type = null;
                        String idkey = keyParts[0];
                        if (keyParts.length > 1) {
                            type = keyParts[1];
                        }
                        if (key.equals(BODY.urlencoded.name()) || key.equals(BODY.form.name())) {
                            spec.formParam(idkey, b.get(id));
                        } else if (key.startsWith(BODY.multipart.name())) {
                            if (idkey.equals(MP_ID_NOT_SET)) {
                                spec.multiPart((File) b.get(id));
                            } else {
                                if (b.get(id) instanceof File) {
                                    if (type != null) {
                                        spec.multiPart(idkey, (File) b.get(id), type);
                                    } else {
                                        spec.multiPart(idkey, (File) b.get(id));
                                    }
                                } else if (b.get(id) instanceof String) {
                                    if (type != null) {
                                        spec.multiPart(idkey, "" + b.get(id), type);
                                    } else {
                                        spec.multiPart(idkey, "" + b.get(id));
                                    }
                                } else {
                                    if (type != null) {
                                        spec.multiPart(idkey, b.get(id), type);
                                    } else {
                                        spec.multiPart(idkey, b.get(id));
                                    }
                                }
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
    public RestSpec logSpec() {
        if (specification != null) {
            specification.log().all();
        }
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

    @Override
    public Object head(String endpoint) {
        specification = RestAssured.build(this);
        response = specification.head(endpoint);
        log(response);
        return response;
    }

    protected void log(Response response) {
        if (doLog()) {
            LOG.debug("RestAssured", "response follows");
            response.then().log().all();
            LOG.flush();
        }
    }

    @Override
    public RequestSpecification getSpec() {
        specification = RestAssured.init(given(), this);
        return specification;
    }

}
