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

import java.util.logging.Level;

/**
 * Base Logging class.
 * This uses slf4j for basic logging purposes.
 * Created by Eric Angeli on 6/5/2017.
 */
public abstract class BleatBoxBase implements BleatBox{
    protected Logger LOG;
    protected Class loggingClass;
    protected BleatLevel volume;
    StringBuilder buffer = new StringBuilder();
    String bufferTitle = "";

    public BleatBoxBase(Class logger){
        this.loggingClass = logger;
        LOG = LoggerFactory.getLogger(logger);
        setVolume(LOG.isDebugEnabled(),
            LOG.isErrorEnabled(),
            LOG.isInfoEnabled(),
            LOG.isTraceEnabled(),
            LOG.isWarnEnabled());
    }

    protected void setVolume(boolean debug, boolean error, boolean info, boolean trace, boolean warn) {
        if (trace) {
            setVolume(Level.FINE);
        } else if (debug) {
            setVolume(Level.CONFIG);
        } else if (info) {
            setVolume(Level.INFO);
        } else if (warn) {
            setVolume(Level.WARNING);
        } else if(error){
            setVolume(Level.SEVERE);
        } else {
            setVolume(Level.INFO);
        }
    }

    public BleatBox setVolume(Level volume) {
        this.volume = BleatLevel.level(volume, loggingClass.getName());
        return this;
    }

    /**
     * Always returns null right now.<br>
     * This is a place holder for possible future functionality not yet implemented.
     * @return The level information.
     */
    @Override
    public BleatLevel level(){
        return volume;
    }

    @Override
    public void infoBuffer(String message) {
        infoBuffer(null, message);
    }

    public void infoBuffer(String title, String message){
        bufferTitle = title;
        if(buffer == null){
            buffer = new StringBuilder();
        }
        buffer.append(message);
    }

    @Override
    public void flush() {
        if(buffer.length()>0) {
            info(bufferTitle, buffer.toString());
            buffer = new StringBuilder();
        }
    }
}
