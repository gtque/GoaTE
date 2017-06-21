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


/**
 * Base Logging class.
 * This uses slf4j for basic logging purposes.
 * Created by Eric Angeli on 6/5/2017.
 */
public interface BleatBox {
    
    BleatLevel level();
    void debug(String message);
    void debug(String title, String message);
    void debug(String message, Throwable t);
    void debug(String title, String message, Throwable t);
    void infoBuffer(String message);
    void info(String message);
    void info(String title, String message);
    void info(String message, Throwable t);
    void info(String title, String message, Throwable t);
    void warn(String message);
    void warn(String title, String message);
    void warn(String message, Throwable t);
    void warn(String title, String message, Throwable t);
    void error(String message);
    void error(String title, String message);
    void error(String message, Throwable t);
    void error(String title, String message, Throwable t);
    void trace(String message);
    void trace(String title, String message);
    void trace(String message, Throwable t);
    void trace(String title, String message, Throwable t);
    void pass(String message);
    void pass(String title, String message);
    void pass(String message, Throwable t);
    void pass(String title, String message, Throwable t);
    void fail(String message);
    void fail(String title, String message);
    void fail(String message, Throwable t);
    void fail(String title, String message, Throwable t);
    void fatal(String message);
    void fatal(String title, String message);
    void fatal(String message, Throwable t);
    void fatal(String title, String message, Throwable t);
    void skip(String message);
    void skip(String title, String message);
    void skip(String message, Throwable t);
    void skip(String title, String message, Throwable t);
    void unknown(String message);
    void unknown(String title, String message);
    void unknown(String message, Throwable t);
    void unknown(String title, String message, Throwable t);
    void flush();
}
