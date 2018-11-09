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

package com.thegoate.utils.insert;

import com.thegoate.Goate;
import com.thegoate.utils.UnknownUtilType;

/**
 * The generic get class.
 * This will attempt to look up the specific get utility for the type detected.
 * Created by Eric Angeli on 5/5/2017.
 */
public class Insert extends UnknownUtilType implements InsertUtility {
    InsertUtility tool = null;
    Object fill = null;


    public Insert(Object o){
        this.fill = o;
    }

    @Override
    public boolean isType(Object check) {
        return false;
    }

    @Override
    public InsertUtility value(String id, Object value) {
        return null;
    }

    @Override
    public InsertUtility into(String original) {
        return null;
    }

    @Override
    public InsertUtility after(String location) throws Exception {
        return null;
    }

    @Override
    public InsertUtility before(String location) throws Exception {
        return null;
    }

    @Override
    public InsertUtility in(String location) throws Exception {
        return null;
    }

    @Override
    public InsertUtility append() throws Exception {
        return null;
    }

    @Override
    public InsertUtility replaceExisting(boolean replace) {
        return null;
    }

    @Override
    public InsertUtility resetLocation() {
        return null;
    }

    @Override
    public InsertUtility resetInsertValue() {
        return null;
    }

    @Override
    public Object insert(boolean setAsOriginal) throws Exception {
        return null;
    }

    @Override
    public Object insert() throws Exception {
        return null;
    }

    @Override
    public Object insert(Goate data) throws Exception {
        return null;
    }
}
