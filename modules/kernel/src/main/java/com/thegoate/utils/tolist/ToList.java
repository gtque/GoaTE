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

package com.thegoate.utils.tolist;

import com.thegoate.utils.UnknownUtilType;
import com.thegoate.utils.to.ToUtil;
import com.thegoate.utils.to.ToUtility;

import java.util.List;

/**
 * The generic list class.
 * This will attempt to look up the specific to list utility for the type detected.
 * Created by Eric Angeli on 5/5/2017.
 */
@ToUtil(type = List.class)
public class ToList extends UnknownUtilType implements ToListUtility, ToUtility<List> {
    ToListUtility tool = null;
    protected Object original = null;

    public ToList(Object o){
        this.original = o;
    }

    @Override
    public boolean isType(Object check) {
        return false;
    }

    @Override
    public List list() {
        List result = null;
        if(original instanceof List){
            result = (List)original;
        } else {
            tool = (ToListUtility) buildUtil(original, ToListUtil.class);
            if (tool != null) {
                result = tool.list();
            }
            if (result == null) {
                LOG.debug("Failed to convert: " + original);
            }
        }
        return result;
    }

    @Override
    public boolean checkType(Class tool, Class type) {
        //        CastUtil tu = (CastUtil) tool.getAnnotation(CastUtil.class);
        //        return tu.type()!=null?(tu.type() == type):(type == null);
        return List.class.equals(type);
    }

    @Override
    public List convert(){
        return list();
    }
}
