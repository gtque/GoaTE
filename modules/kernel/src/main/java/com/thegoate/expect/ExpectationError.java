package com.thegoate.expect;

public class ExpectationError extends AssertionError{

    public ExpectationError(String msg){
        super(msg);
    }
}
