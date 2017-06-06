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


import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;

/**
 * Base class for Get Utilities. Adds logger, and class level variables.
 * It is recommend that Get utilities extend this class, but they really only
 * need to implement the GetUtility interface.
 * Created by Eric Angeli on 5/5/2017.
 */
public abstract class GetTool implements GetUtility{
    protected final BleatBox LOG = BleatFactory.getLogger(getClass());
    protected Object selector = null;
    protected Object nested = null;
    protected Object container = null;
    public GetTool(Object selector){
        if(selector instanceof String){
            String select = ""+selector;
            if((select).contains(">")){
                nested = select.substring(select.indexOf(">")+1);
                selector = select.substring(0,select.indexOf(">"));
            }
        }
        this.selector = selector;
    }

    /**
     * The from implementation should, in most cases,
     * call processNested on the result and return that value.
     * In some cases this may not make sense and no processing of nested gets can be performed.
     * @param subContainer The container to perform the action on.
     * @return The result of processing the nested operation.
     */
    protected Object processNested(Object subContainer){
        Object result = subContainer;
        if(nested!=null){
            result = new Get(nested).from(subContainer);
        }
        return result;
    }
}
