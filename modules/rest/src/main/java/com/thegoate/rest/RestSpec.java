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

/**
 * Base Rest interface for doing restful api calls because JAVA doesn't allow
 * you to extend more than one class so this is a a little hack to get around some issues.
 * Created by Eric Angeli on 5/16/2017.
 */
public interface RestSpec {

    Object response();

    RestSpec enableLog();

    RestSpec disableLog();

    RestSpec headers(Goate data);

    RestSpec header(String key, Object value);

    RestSpec cookies(Goate data);

    RestSpec cookie(String key, Object value);

    RestSpec queryParams(Goate data);

    RestSpec queryParam(String key, Object value);

    RestSpec urlParams(Goate data);

    RestSpec urlParam(String key, Object value);

    RestSpec pathParams(Goate data);

    RestSpec pathParam(String key, Object value);

    RestSpec bodyAsFormData();

    RestSpec bodyAsFormUrlencodedData();

    RestSpec bodyAsRawData();

    RestSpec bodyAsMultipartFormData();

    RestSpec bodyAsBinaryData();

    RestSpec formData(Goate data);

    RestSpec formData(String key, Object value);

    RestSpec formUrlencodedData(String key, Object value);

    RestSpec binaryBody(Object value);

    RestSpec rawBody(Object value);

    RestSpec multipartData(Goate data);

    RestSpec multipartFormData(Object value);

    RestSpec multipartFormData(String key, Object value);

    RestSpec multipartFormData(String key, Object value, String contentType);

    RestSpec customData(Goate data);

    RestSpec customData(Enum key, Object value);

    RestSpec processCustomData(Enum key, Object value);

    RestSpec processCustomData(String key, Object value);

    RestSpec binaryBodyData(Object value);

    RestSpec body(Object value);

    RestSpec body(Enum type, String key, Object value);

    RestSpec baseUri(String uri);

    RestSpec baseURL(String url);

    RestSpec logSpec();

    RestSpec urlEncode(boolean encode);

    boolean urlEncode();

    Goate getHeaders();

    Goate getCookies();

    Goate getQueryParameters();

    Goate getPathParameters();

    Goate getURLParameters();

    Goate getBody();

    int getTimeout();

    RestSpec timeout(int timeoutSeconds);

    boolean doLog();

    String getBaseURL();

    RestSpec configure(Object config);

    Object getConfig();

    RestSpec config();

    Object get(String endpoint);

    Object put(String endpoint);

    Object post(String endpoint);

    Object delete(String endpoint);

    Object patch(String endpoint);

    Object head(String endpoint);
}
