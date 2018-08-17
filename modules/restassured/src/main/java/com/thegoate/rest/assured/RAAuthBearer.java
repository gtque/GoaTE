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

import com.thegoate.logging.BleatBox;
import com.thegoate.rest.RestAuthBearer;
import com.thegoate.rest.RestSpec;
import com.thegoate.rest.annotation.GoateRest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * REST Assured implementation.
 * Created by Eric Angeli on 5/16/2017.
 */
@GoateRest(security = "bearer token")
public class RAAuthBearer extends RestAuthBearer implements RASpec{
    RequestSpecification specification = null;
    Response response = null;

    public RAAuthBearer(){
        this.specification = RestAssured.init(given(), this);
    }

    public RAAuthBearer(RequestSpecification specification){
        this.specification = RestAssured.init(specification, this);
    }

    @Override
    public RestSpec config(){
        RestAssured.init(specification, this);
        return this;
    }

    @Override
    public BleatBox getLog(){
        return LOG;
    }

    @Override
    public Object response(){
        return response;
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

    protected void log(Response response){
        if(doLog()){
            LOG.debug("RAAuthBearer","response follows");
            response.then().log().all();
            LOG.flush();
        }
    }

    @Override
    public RequestSpecification getSpec() {
        return specification;
    }
}
