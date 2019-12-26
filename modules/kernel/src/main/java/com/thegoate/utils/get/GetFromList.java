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

package com.thegoate.utils.get;

import com.thegoate.Goate;

import java.util.List;

/**
 * Loads the file specified in from into a string and returns it.
 * Created by Eric Angeli on 5/5/2017.
 */
@GetUtil
public class GetFromList extends GetTool {

    public GetFromList() {
        super(new Goate());
    }

    public GetFromList(Object selector) {
        super(selector);
    }

    @Override
    public boolean isType(Object check) {
        return check instanceof List;
    }

    @Override
    public Object from(Object container) {
        Object result = new NotFound("" + selector);
        if (container instanceof List) {
            String select = "" + selector;
            int index = -42;
            int dot = -42;
            try {
                dot = select.indexOf(".");
                if (dot < 0) {
                    dot = select.length();
                }
                index = Integer.parseInt(select.substring(0, dot));
            } catch (Exception e) {
                LOG.error("Index not found: " + select);
            }
            if (index >= 0 && index <((List) container).size()) {
                result = ((List)container).get(index);
                if(result!=null) {
                    if (dot < select.length()) {
                        select = select.substring(dot + 1);
                        if (!select.isEmpty()) {
                            result = new Get(select).from(result);
                        }
                    }
                }
            }
        }
        result = processNested(result);//process nested gets.
        return result;
    }
}
