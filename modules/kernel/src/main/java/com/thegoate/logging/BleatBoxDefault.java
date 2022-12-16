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

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

import org.slf4j.Marker;

import com.thegoate.annotations.IsDefault;

/**
 * Simple default BleatBox implementation that logs
 * to basic slf4j levels.
 * Created by Eric Angeli on 6/5/2017.
 */
@Bleat
@IsDefault
public class BleatBoxDefault extends BleatBoxBase {

	public BleatBoxDefault(Class logger) {
		super(logger);
	}

	protected String formatted(String title, String message) {
		return format("%s:%s", ofNullable(title).orElse(""), message);
	}

	@Override
	public void debug(String message) {
		debug((String) null, message);
	}

	@Override
	public void debug(String format, Object arg) {
		LOG.debug(format, arg);
	}

	@Override
	public void debug(String format, Object arg1, Object arg2) {
		LOG.debug(format, arg1, arg2);
	}

	@Override
	public void debug(String format, Object... arguments) {
		LOG.debug(format, arguments);
	}

	@Override
	public void debug(String title, String message) {
		LOG.debug(formatted(title, message));
	}

	@Override
	public void debug(String message, Throwable t) {
		debug((String) null, message, t);
	}

	@Override
	public boolean isDebugEnabled(Marker marker) {
		return false;
	}

	@Override
	public void debug(Marker marker, String msg) {
		LOG.debug(marker, msg);
	}

	@Override
	public void debug(Marker marker, String format, Object arg) {
		LOG.debug(marker, format, arg);
	}

	@Override
	public void debug(Marker marker, String format, Object arg1, Object arg2) {
		LOG.debug(marker, format, arg1, arg2);
	}

	@Override
	public void debug(Marker marker, String format, Object... arguments) {
		LOG.debug(marker, format, arguments);
	}

	@Override
	public void debug(Marker marker, String msg, Throwable t) {
		LOG.debug(marker, msg, t);
	}

	@Override
	public boolean isInfoEnabled() {
		return false;
	}

	@Override
	public void debug(String title, String message, Throwable t) {
		LOG.debug(formatted(title, message), t);
	}

	@Override
	public void info(String message) {
		info((String) null, message);
	}

	@Override
	public void info(String format, Object arg) {
		LOG.info(format, arg);
	}

	@Override
	public void info(String format, Object arg1, Object arg2) {
		LOG.info(format, arg1, arg2);
	}

	@Override
	public void info(String format, Object... arguments) {
		LOG.info(format, arguments);
	}

	@Override
	public void info(String title, String message) {
		LOG.info(formatted(title, message));
	}

	@Override
	public void info(String message, Throwable t) {
		info((String) null, message, t);
	}

	@Override
	public boolean isInfoEnabled(Marker marker) {
		return false;
	}

	@Override
	public void info(Marker marker, String msg) {
		LOG.info(marker, msg);
	}

	@Override
	public void info(Marker marker, String format, Object arg) {
		LOG.info(marker, format, arg);
	}

	@Override
	public void info(Marker marker, String format, Object arg1, Object arg2) {
		LOG.info(marker, format, arg1, arg2);
	}

	@Override
	public void info(Marker marker, String format, Object... arguments) {
		LOG.info(marker, format, arguments);
	}

	@Override
	public void info(Marker marker, String msg, Throwable t) {
		LOG.info(marker, msg, t);
	}

	@Override
	public boolean isWarnEnabled() {
		return false;
	}

	@Override
	public void info(String title, String message, Throwable t) {
		LOG.info(formatted(title, message), t);
	}

	@Override
	public void warn(String message) {
		warn((String) null, message);
	}

	@Override
	public void warn(String format, Object arg) {
		LOG.warn(format, arg);
	}

	@Override
	public void warn(String format, Object... arguments) {
		LOG.warn(format, arguments);
	}

	@Override
	public void warn(String format, Object arg1, Object arg2) {
		LOG.warn(format, arg1, arg2);
	}

	@Override
	public void warn(String title, String message) {
		LOG.warn(formatted(title, message));
	}

	@Override
	public void warn(String message, Throwable t) {
		warn((String) null, message, t);
	}

	@Override
	public boolean isWarnEnabled(Marker marker) {
		return false;
	}

	@Override
	public void warn(Marker marker, String msg) {
		LOG.warn(marker, msg);
	}

	@Override
	public void warn(Marker marker, String format, Object arg) {
		LOG.warn(marker, format, arg);
	}

	@Override
	public void warn(Marker marker, String format, Object arg1, Object arg2) {
		LOG.warn(marker, format, arg1, arg2);
	}

	@Override
	public void warn(Marker marker, String format, Object... arguments) {
		LOG.warn(marker, format, arguments);
	}

	@Override
	public void warn(Marker marker, String msg, Throwable t) {
		LOG.warn(marker, msg, t);
	}

	@Override
	public boolean isErrorEnabled() {
		return false;
	}

	@Override
	public void warn(String title, String message, Throwable t) {
		LOG.warn(formatted(title, message), t);
	}

	@Override
	public void error(String message) {
		error((String) null, message);
	}

	@Override
	public void error(String format, Object arg) {
		LOG.error(format, arg);
	}

	@Override
	public void error(String format, Object arg1, Object arg2) {
		LOG.error(format, arg1, arg2);
	}

	@Override
	public void error(String format, Object... arguments) {
		LOG.error(format, arguments);
	}

	@Override
	public void error(String title, String message) {
		LOG.error(formatted(title, message));
	}

	@Override
	public void error(String message, Throwable t) {
		error((String) null, message, t);
	}

	@Override
	public boolean isErrorEnabled(Marker marker) {
		return false;
	}

	@Override
	public void error(Marker marker, String msg) {
		LOG.error(marker, msg);
	}

	@Override
	public void error(Marker marker, String format, Object arg) {
		LOG.error(marker, format, arg);
	}

	@Override
	public void error(Marker marker, String format, Object arg1, Object arg2) {
		LOG.error(marker, format, arg1, arg2);
	}

	@Override
	public void error(Marker marker, String format, Object... arguments) {
		LOG.error(marker, format, arguments);
	}

	@Override
	public void error(Marker marker, String msg, Throwable t) {
		LOG.error(marker, msg, t);
	}

	@Override
	public void error(String title, String message, Throwable t) {
		LOG.error(formatted(title, message), t);
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean isTraceEnabled() {
		return false;
	}

	@Override
	public void trace(String message) {
		trace((String) null, message);
	}

	@Override
	public void trace(String format, Object arg) {
		LOG.trace(format, arg);
	}

	@Override
	public void trace(String format, Object arg1, Object arg2) {
		LOG.trace(format, arg1, arg2);
	}

	@Override
	public void trace(String format, Object... arguments) {
		LOG.trace(format, arguments);
	}

	@Override
	public void trace(String title, String message) {
		LOG.trace(formatted(title, message));
	}

	@Override
	public void trace(String message, Throwable t) {
		trace((String) null, message, t);
	}

	@Override
	public boolean isTraceEnabled(Marker marker) {
		return false;
	}

	@Override
	public void trace(Marker marker, String msg) {
		LOG.trace(marker, msg);
	}

	@Override
	public void trace(Marker marker, String format, Object arg) {
		LOG.trace(marker, format, arg);
	}

	@Override
	public void trace(Marker marker, String format, Object arg1, Object arg2) {
		LOG.trace(marker, format, arg1, arg2);
	}

	@Override
	public void trace(Marker marker, String format, Object... argArray) {
		LOG.trace(marker, format, argArray);
	}

	@Override
	public void trace(Marker marker, String msg, Throwable t) {
		LOG.trace(marker, msg, t);
	}

	@Override
	public boolean isDebugEnabled() {
		return false;
	}

	@Override
	public void trace(String title, String message, Throwable t) {
		LOG.trace(formatted(title, message), t);
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
	public void pass(String format, Object arg) {
		info(format, arg);
	}

	@Override
	public void pass(String format, Object arg1, Object arg2) {
		info(format, arg1, arg2);
	}

	@Override
	public void pass(String format, Object... arguments) {
		info(format, arguments);
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
	public void fail(String format, Object arg) {
		error(format, arg);
	}

	@Override
	public void fail(String format, Object arg1, Object arg2) {
		error(format, arg1, arg2);
	}

	@Override
	public void fail(String format, Object... arguments) {
		error(format, arguments);
	}

	@Override
	public void fatal(String message) {
		fatal(null, message);
	}

	@Override
	public void fatal(String title, String message) {
		error((String) null, message);
	}

	@Override
	public void fatal(String message, Throwable t) {
		fatal(null, message, t);
	}

	@Override
	public void fatal(String title, String message, Throwable t) {
		error(title, message, t);
	}

	@Override
	public void fatal(String format, Object arg) {
		error(format, arg);
	}

	@Override
	public void fatal(String format, Object arg1, Object arg2) {
		error(format, arg1, arg2);
	}

	@Override
	public void fatal(String format, Object... arguments) {
		error(format, arguments);
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
	public void skip(String format, Object arg) {
		info(format, arg);
	}

	@Override
	public void skip(String format, Object arg1, Object arg2) {
		info(format, arg1, arg2);
	}

	@Override
	public void skip(String format, Object... arguments) {
		info(format, arguments);
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

	@Override
	public void unknown(String format, Object arg) {
		info(format, arg);
	}

	@Override
	public void unknown(String format, Object arg1, Object arg2) {
		info(format, arg1, arg2);
	}

	@Override
	public void unknown(String format, Object... arguments) {
		info(format, arguments);
	}
}
