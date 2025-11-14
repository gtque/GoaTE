package com.thegoate.rest.retrofit.models;

import com.thegoate.utils.fill.serialize.Kid;

public class HelloWorldTestGreeting extends Kid {

    public String greeting;
    public SimpleName nested;

    public String getGreeting() {
        return greeting;
    }

    public HelloWorldTestGreeting setGreeting(String greeting) {
        this.greeting = greeting;
        return this;
    }

    public SimpleName getNested() {
        return nested;
    }

    public void setNested(SimpleName nested) {
        this.nested = nested;
    }
}
