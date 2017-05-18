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

import java.util.Base64;

/**
 * Base class for rest using a basic auth header.
 * Created by Eric Angeli on 5/16/2017.
 */
public abstract class RestAuthBasicHeader extends RestAuthBasicUserPW {
    public enum Settings{
        user,password
    }

    @Override
    public RestSpec processCustomData(Enum key, Object value){
        return processCustomData(key.name(), value);
    }

    @Override
    public RestSpec processCustomData(String key, Object value){
        if (key.equals(Settings.user.name())) {
            this.user = "" + value;
        }else if (key.equals(Settings.password.name())) {
            this.password = "" + value;
        }
        header("Authorization", "Basic "+ getEncodedAuth(user,password));
        return this;
    }

    public String getEncodedAuth(String user, String password){
        String auth = user +":"+password;
        auth = Base64.getEncoder().encodeToString(auth.getBytes());
        return auth;
    }
}
