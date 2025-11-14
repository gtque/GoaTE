package com.thegoate.rest.retrofit.impl;

public class RetrofitException extends RuntimeException {

    public RetrofitException(String message) {
        super(message);
    }

    public RetrofitException(String message, Throwable e) {
        super(message, e);
    }

}
