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
package com.thegoate.expect.builder;

import com.thegoate.locate.Locate;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;

/**
 * Simple pojo to better couple the value and the from, aka containing object or employee.
 * Introduced to improve readability in defining expectations.
 */
public class Value {
    BleatBox LOG = BleatFactory.getLogger(getClass());
    Object locator;
    Object container;
    boolean or = false;

    public Value(){}

    public Value(Object value){
        value(value);
    }

    /**
     * The "locator" or value to store.
     * @param value the locator or value.
     * @return self
     */
    public Value value(Object value){
        return find(value);
    }

    /**
     * Syntatic sugar for "find".
     * @param locator the path/locator for the value to get from the containing object.
     * @return this, more syntactic sugar to string calls together.
     */
    public Value get(Object locator){
        return find(locator);
    }

    public Value orGet(Object locator){
        or = true;
        return find(locator);
    }

    public Value orFind(Object locator){
        or = true;
        return find(locator);
    }

    /**
     * The "locator" or value to store.
     * @param locator the locator or value
     * @return self
     */
    public Value find(Object locator){
        Object locate = locator;
        if(locator != null && locator instanceof Locate){
            locate = ((Locate)locator).toPath();
        }
        boolean appendLocate = true;
        if(or){
            if(this.locator != null && this.locator instanceof String && !((String)this.locator).isEmpty()) {
                if(locate != null && locate instanceof String) {
                    this.locator = ""+this.locator + "||";
                } else {
                    LOG.debug("You can't use or for non-String locators: " + locate.getClass());
                    appendLocate = false;
                }
            } else {
                if(this.locator == null) {
                    this.locator = "";
                } else if(!(this.locator instanceof String)) {
                    LOG.debug("locator is already not a string: " + this.locator.getClass() + ", you can't use or on non-String locators.");
                    appendLocate = false;
                }
            }
        }
        if(or){
            if(appendLocate) {
                this.locator = ""+this.locator+locate;
            }
        } else {
            this.locator = locate;
        }
        return this;
    }

    /**
     * Syntactic sugar, simply calls in(container), but some people may find from(container) more comfortable.
     * @param container The object containing the value.
     * @return self
     */
    public Value from(Object container){
        return in(container);
    }

    /**
     * sets the container object that has the value in it.
     * @param container the object containing the value.
     * @return self
     */
    public Value in(Object container){
        this.container = container;
        return this;
    }

    /**
     * Gets the value/locator
     * @return the value/locator
     */
    public Object getLocator(){
        return this.locator;
    }

    /**
     * Gets the container/employee/object containing the value to be found using the locator.
     * @return the container.
     */
    public Object getContainer(){
        return this.container;
    }

}
