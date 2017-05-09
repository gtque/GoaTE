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

import com.thegoate.Goate;
import com.thegoate.staff.GoateTask;
import com.thegoate.staff.GoateTaskContainer;

/**
 * Created by gtque on 4/24/2017.
 */
@GoateTaskContainer
public class AnnotatedMethodStub {

    @GoateTask(task = "say hello")
    public String hello(){
        return "hello";
    }

    @GoateTask(task = "say ${var} and print ${var}")
    public String say(String word, Goate data){
        return word + "\n" + data.toString();
    }

    @GoateTask(task = "void ${var}")
    public void checkBoolean(boolean pass) throws Exception{
        if(!pass){
            throw new Exception("FAIL");
        }
    }
}
