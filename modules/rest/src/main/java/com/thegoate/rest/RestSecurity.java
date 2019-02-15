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
 * If there are any required (or optional) values listed for the auth type, you must
 * use customParam to set those values.
 * example: new RestCall().customParam(RestSecurity.BasicAuth.requiredUser, "userid").
 * Created by Eric Angeli on 11/28/2018.
 */
public class RestSecurity {

    public static class BasicAuth{
        public static final String id = "basic auth";
        public static final String requiredUser = RestAuthBasicUserPW.Settings.user.name();
        public static final String requiredPassword = RestAuthBasicUserPW.Settings.password.name();
    }

    public static class BasicAuthHeader{
        public static final String id = "basic auth header";
        public static final String requiredUser = RestAuthBasicUserPW.Settings.user.name();
        public static final String requiredPassword = RestAuthBasicUserPW.Settings.password.name();
    }

    public static class BearerToken{
        public static final String id = "bearer token";
        public static final String requiredToken = RestAuthBearer.Settings.bearer.name();
        public static final String optionalAuthLabel = RestAuthBearer.Settings.auth_label.name();
    }
}
