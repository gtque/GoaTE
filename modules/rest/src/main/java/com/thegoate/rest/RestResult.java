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

/**
 * Created by Eric Angeli on 11/26/2018.
 */
public class RestResult {
    public static final String statusCode = "get_from_rest_response::status code";
    public static final String bodyAsAString = "get_from_rest_response::body as a string";
    public static final String body = "get_from_rest_response::body";
    public static final String responseTime = "get_from_rest_response::response time";
    public static final String sessionId = "get_from_rest_response::session id";
    public static final String statusLine = "get_from_rest_response::status line";
    public static final String json = "get_from_rest_response::json";
    public static final String xml = "get_from_rest_response::xml";
    public static final String html = "get_from_rest_response::html";

    public static final String header(String header) {
        return "get_from_rest_response::header " + header;
    }

    public static final String cookie(String cookie) {
        return "get_from_rest_response::cookie " + cookie;
    }

    public static final String detailedCookie(String cookie) {
        return "get_from_rest_response::detailedCookie " + cookie;
    }

    public static final String inputStream = "get_from_rest_response::input stream";

    public static final String getField(String field){
        return field;
    }
}
