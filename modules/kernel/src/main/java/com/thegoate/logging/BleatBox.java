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
package com.thegoate.logging;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base Logging class.
 * This uses slf4j for basic logging purposes.
 * Created by Eric Angeli on 6/5/2017.
 */
public abstract class BleatBox {
    protected Logger LOG;
    protected Class loggingClass;

    public BleatBox(Class logger){
        this.loggingClass = logger;
        LOG = LoggerFactory.getLogger(logger);
    }

    public Logger level(){
        return LOG;
    }

    public abstract void debug(String message);
    public abstract void debug(String title, String message);
    public abstract void debug(String message, Throwable t);
    public abstract void debug(String title, String message, Throwable t);
    public abstract void info(String message);
    public abstract void info(String title, String message);
    public abstract void info(String message, Throwable t);
    public abstract void info(String title, String message, Throwable t);
    public abstract void warn(String message);
    public abstract void warn(String title, String message);
    public abstract void warn(String message, Throwable t);
    public abstract void warn(String title, String message, Throwable t);
    public abstract void error(String message);
    public abstract void error(String title, String message);
    public abstract void error(String message, Throwable t);
    public abstract void error(String title, String message, Throwable t);
    public abstract void trace(String message);
    public abstract void trace(String title, String message);
    public abstract void trace(String message, Throwable t);
    public abstract void trace(String title, String message, Throwable t);
    public abstract void pass(String message);
    public abstract void pass(String title, String message);
    public abstract void pass(String message, Throwable t);
    public abstract void pass(String title, String message, Throwable t);
    public abstract void fail(String message);
    public abstract void fail(String title, String message);
    public abstract void fail(String message, Throwable t);
    public abstract void fail(String title, String message, Throwable t);
    public abstract void fatal(String message);
    public abstract void fatal(String title, String message);
    public abstract void fatal(String message, Throwable t);
    public abstract void fatal(String title, String message, Throwable t);
    public abstract void skip(String message);
    public abstract void skip(String title, String message);
    public abstract void skip(String message, Throwable t);
    public abstract void skip(String title, String message, Throwable t);
    public abstract void unknown(String message);
    public abstract void unknown(String title, String message);
    public abstract void unknown(String message, Throwable t);
    public abstract void unknown(String title, String message, Throwable t);

}
