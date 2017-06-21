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

import com.thegoate.annotations.IsDefault;

/**
 * Simple default BleatBox implementation that logs
 * to basic slf4j levels.
 * Created by Eric Angeli on 6/5/2017.
 */
@Bleat
@IsDefault
public class BleatBoxDefault extends BleatBoxBase{

    public BleatBoxDefault(Class logger) {
        super(logger);
    }

    @Override
    public void debug(String message) {
        debug(null, message);
    }

    @Override
    public void debug(String title, String message) {
        LOG.debug((title==null?"":title+":") + message);
    }

    @Override
    public void debug(String message, Throwable t) {
        debug(null, message, t);
    }

    @Override
    public void debug(String title, String message, Throwable t) {
        LOG.debug((title==null?"":title+":") + message, t);
    }

    @Override
    public void info(String message) {
        info(null, message);
    }

    @Override
    public void info(String title, String message) {
        LOG.info((title==null?"":title+":") + message);
    }

    @Override
    public void info(String message, Throwable t) {
        info(null, message, t);
    }

    @Override
    public void info(String title, String message, Throwable t) {
        LOG.info((title==null?"":title+":") + message, t);
    }

    @Override
    public void warn(String message) {
        warn(null, message);
    }

    @Override
    public void warn(String title, String message) {
        LOG.warn((title==null?"":title+":") + message);
    }

    @Override
    public void warn(String message, Throwable t) {
        warn(null, message, t);
    }

    @Override
    public void warn(String title, String message, Throwable t) {
        LOG.warn((title==null?"":title+":") + message, t);
    }

    @Override
    public void error(String message) {
        error(null, message);
    }

    @Override
    public void error(String title, String message) {
        LOG.error((title==null?"":title+":") + message);
    }

    @Override
    public void error(String message, Throwable t) {
        error(null, message, t);
    }

    @Override
    public void error(String title, String message, Throwable t) {
        LOG.error((title==null?"":title+":") + message, t);
    }

    @Override
    public void trace(String message) {
        trace(null, message);
    }

    @Override
    public void trace(String title, String message) {
        LOG.trace((title==null?"":title+":") + message);
    }

    @Override
    public void trace(String message, Throwable t) {
        trace(null, message, t);
    }

    @Override
    public void trace(String title, String message, Throwable t) {
        LOG.trace((title==null?"":title+":") + message, t);
    }

    @Override
    public void pass(String message) {
        pass(null, message);
    }

    @Override
    public void pass(String title, String message) {
        info(title, message);
    }

    @Override
    public void pass(String message, Throwable t) {
        pass(null, message, t);
    }

    @Override
    public void pass(String title, String message, Throwable t) {
        info(title, message, t);
    }

    @Override
    public void fail(String message) {
        fail(null, message);
    }

    @Override
    public void fail(String title, String message) {
        error(title, message);
    }

    @Override
    public void fail(String message, Throwable t) {
        fail(null, message, t);
    }

    @Override
    public void fail(String title, String message, Throwable t) {
        error(title, message, t);
    }

    @Override
    public void fatal(String message) {
        fatal(null, message);
    }

    @Override
    public void fatal(String title, String message) {
        error(null, message);
    }

    @Override
    public void fatal(String message, Throwable t) {
        fatal(message, t);
    }

    @Override
    public void fatal(String title, String message, Throwable t) {
        error(title, message, t);
    }

    @Override
    public void skip(String message) {
        skip(null, message);
    }

    @Override
    public void skip(String title, String message) {
        info(title, message);
    }

    @Override
    public void skip(String message, Throwable t) {
        skip(null, message, t);
    }

    @Override
    public void skip(String title, String message, Throwable t) {
        info(title, message, t);
    }

    @Override
    public void unknown(String message) {
        unknown(null, message);
    }

    @Override
    public void unknown(String title, String message) {
        info(title, message);
    }

    @Override
    public void unknown(String message, Throwable t) {
        unknown(null, message, t);
    }

    @Override
    public void unknown(String title, String message, Throwable t) {
        info(title, message, t);
    }

}
