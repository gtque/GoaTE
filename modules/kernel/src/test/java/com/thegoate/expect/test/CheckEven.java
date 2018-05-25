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
package com.thegoate.expect.test;

import com.thegoate.staff.Employee;
import com.thegoate.staff.GoateJob;

/**
 * Simple employee that checks if the parameter in CheckEven##.value is even or not.
 * Created by Eric Angeli on 5/10/2017.
 */
@GoateJob(jobs = {"check if even"})
public class CheckEven extends Employee {
    int value = 0;
    @Override
    public Employee init() {
        value = Integer.parseInt(""+data.get(getName()+".value"));
        return this;
    }

    @Override
    protected Object doWork() {
        if(value==42){
            Integer.parseInt("fail");
        }
        return value%2==0;
    }

    @Override
    public String[] detailedScrub(){
        return null;
    }
}
