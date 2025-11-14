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
package com.thegoate.rest.retrofit.expect;

import com.thegoate.staff.Employee;
import com.thegoate.staff.GoateJob;
import retrofit2.Response;

/**
 * Does the work of validating rest assured responses.
 * Created by Eric Angeli on 11/7/2025.
 */
@GoateJob(jobs = {"retrofit api response"})
public class RetrofitResponseExpectation extends Employee {

    Response response;

    @Override
    public Employee init() {
        response = (Response)data.get("response");
        return this;
    }

    @Override
    protected Object doWork() {
        if(response==null){
            LOG.warn("The response was null, make sure it was set correctly in the data collection by putting in with a key of \"response\".");
        }
        return response;
    }

    @Override
    public String[] detailedScrub(){
        String[] scrub = {"response"};
        return scrub;
    }
}
