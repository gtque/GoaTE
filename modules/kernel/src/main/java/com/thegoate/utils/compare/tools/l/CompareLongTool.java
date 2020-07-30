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
package com.thegoate.utils.compare.tools.l;

import com.thegoate.reflection.GoateReflection;
import com.thegoate.utils.ParseDetector;
import com.thegoate.utils.compare.CompareTool;

/**
 * Created by Eric Angeli on 7/14/2017.
 */
public abstract class CompareLongTool extends CompareTool implements ParseDetector {
    public CompareLongTool(Object actual) {
        super(actual);
    }

    @Override
    public boolean isType(Object check) {
        boolean istype = false;
        try{
            GoateReflection gr = new GoateReflection();
            if (gr.isLongType(check.getClass())) {
                LOG.debug("Check Type", "detected primitive long");
                istype = true;
            } else {
                LOG.debug("Check Type", "not a primitive long.");
            }
        }catch(Throwable e){
//            LOG.debug(""+actual + " is not a long.");
        }
        return istype;
    }

    @Override
    public boolean parseDetector(Object object){
        boolean result = false;
        try{
            parseLong(object);
            result = true;
        } catch (Exception e){
            LOG.debug("Parse Detector", "Not a parsable long.");
        }
        return result;
    }

    protected long parseLong(Object o){
        return new GoateReflection().parseLong(o);
    }
}
