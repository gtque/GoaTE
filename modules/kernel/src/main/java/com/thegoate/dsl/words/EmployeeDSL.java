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

package com.thegoate.dsl.words;

import com.thegoate.Goate;
import com.thegoate.annotations.GoateDescription;
import com.thegoate.dsl.DSL;
import com.thegoate.dsl.GoateDSL;
import com.thegoate.staff.Employee;

/**
 * Builds and returns the specified employee.
 * Returns the employee.
 * Created by gtque on 9/27/2022.
 */
@GoateDSL(word = "employee")
@GoateDescription(description = "Returns the specified employee, syntactic DSL sugar for 'hire'. This does not 'build()' the employee, it only constructs it and returns it.",
    parameters = {"The name of the employee to hire, either the job name or the cull class path and name of the employee class."})
public class EmployeeDSL extends HireDSL {
    Goate parameters = new Goate();

    public EmployeeDSL(Object value) {
        super(value);
    }

    public static String employee(String task){
        return "employee::"+task;
    }

    public EmployeeDSL parameter(String key, Object value){
        parameters.put(key, value);
        return this;
    }

}
