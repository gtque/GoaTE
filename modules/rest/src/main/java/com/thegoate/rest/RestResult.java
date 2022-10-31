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
    public static final String statusCode = "status code";
    public static final String bodyAsAString = "body as a string";
    public static final String body = "body";
    public static final String responseTime = "response time";
    public static final String sessionId = "session id";
    public static final String statusLine = "status line";
    public static final String json = "json";
    public static final String xml = "xml";
    public static final String html = "html";
    public static final String inputStream = "input stream";
    public static final String content = "_goate rest result content";
    public static final String byteArray = "_goate rest result asByteArray";
    public static final String detailedCookies = "detailedCookies";
    public static final String header(String header) {
        return "header " + header;
    }

    public static final String cookie(String cookie) {
        return "cookie " + cookie;
    }

    public static final String detailedCookie(String cookie) {
        return "detailedCookie " + cookie;
    }

    public static final String getField(String field){
        return field;
    }
}
