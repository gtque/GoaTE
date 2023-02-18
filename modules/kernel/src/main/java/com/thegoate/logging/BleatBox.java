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

import java.util.logging.Level;

/**
 * Base Logging class.
 * This uses slf4j for basic logging purposes.
 * Created by Eric Angeli on 6/5/2017.
 */
public interface BleatBox extends Logger {
    
    BleatLevel level();
    BleatBox setVolume(Level volume);
    void debug(String title, String message);
    void debug(String title, String message, Throwable t);
    void infoBuffer(String message);
    void infoBuffer(String title, String message);
    void info(String title, String message);
    void info(String title, String message, Throwable t);
    void warn(String title, String message);
    void warn(String title, String message, Throwable t);
    void error(String title, String message);
    void error(String title, String message, Throwable t);
    void trace(String title, String message);
    void trace(String title, String message, Throwable t);
    void pass(String message);
    void pass(String title, String message);
    void pass(String message, Throwable t);
    void pass(String title, String message, Throwable t);
    void pass(String format, Object arg);
    void pass(String format, Object arg1, Object arg2);
    void pass(String format, Object... arguments);
    void fail(String message);
    void fail(String title, String message);
    void fail(String message, Throwable t);
    void fail(String title, String message, Throwable t);
    void fail(String format, Object arg);
    void fail(String format, Object arg1, Object arg2);
    void fail(String format, Object... arguments);
    void fatal(String message);
    void fatal(String title, String message);
    void fatal(String message, Throwable t);
    void fatal(String title, String message, Throwable t);
    void fatal(String format, Object arg);
    void fatal(String format, Object arg1, Object arg2);
    void fatal(String format, Object... arguments);
    void skip(String message);
    void skip(String title, String message);
    void skip(String message, Throwable t);
    void skip(String title, String message, Throwable t);
    void skip(String format, Object arg);
    void skip(String format, Object arg1, Object arg2);
    void skip(String format, Object... arguments);
    void unknown(String message);
    void unknown(String title, String message);
    void unknown(String message, Throwable t);
    void unknown(String title, String message, Throwable t);
    void unknown(String format, Object arg);
    void unknown(String format, Object arg1, Object arg2);
    void unknown(String format, Object... arguments);
    void flush();
}
