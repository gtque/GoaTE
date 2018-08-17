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

package com.thegoate.annotations.test;

import com.thegoate.staff.GoateTask;
import com.thegoate.staff.GoateTaskContainer;

/**
 * Created by gtque on 7/26/2017.
 */
@GoateTaskContainer
public class AnnotatedMethodStubWithConstructor {

    String name = "Alex";
    int age = 42;
    public AnnotatedMethodStubWithConstructor(String name){
        this.name = name;
    }

    public AnnotatedMethodStubWithConstructor(int age){
        this.age = age;
    }

    @GoateTask(task = "say howdy")
    public String hello(){
        return "howdy " + name;
    }

    @GoateTask(task = "multiply age by ${var}")
    public int multiply(int multiplier){
        return age * multiplier;
    }

    @GoateTask(task = "void with constructor ${var}")
    public void checkBoolean(boolean pass) throws Exception{
        if(!pass){
            throw new Exception("FAIL");
        }
    }
}
