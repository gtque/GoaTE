package com.thegoate.utils;

/**
 * Exception to be thrown by Utility classes.
 * Created by Eric Angeli on 3/5/2018.
 */
public class UtilException extends Exception {
    public UtilException(){
        super();
    }
    public UtilException(String message){
        super(message);
    }
    public UtilException(String message, Throwable cause){
        super(message, cause);
    }
    public UtilException(Throwable cause){
        super(cause);
    }
}
