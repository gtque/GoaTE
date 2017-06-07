package com.thegoate.logging;

/**
 * Place holder for possible future support of getting/checking enabled log level.
 * Created by Eric Angeli on 6/6/2017.
 */
public interface BleatLevel {
    boolean isDebugEnabled();
    boolean isInfoEnabled();
    boolean isWarnEnabled();
    boolean isErrorEnabled();
    boolean isFatalEnabled();
    boolean isPassEnabled();
    boolean isFailEnabled();
    boolean isSkipEnabled();
    boolean isUnknownEnabled();
    boolean isTraceEnabled();
}
