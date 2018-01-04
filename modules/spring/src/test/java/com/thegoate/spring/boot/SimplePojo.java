package com.thegoate.spring.boot;

/**
 * Created by Eric Angeli on 11/1/2017.
 */
public class SimplePojo {
    String message = "";
    String id = "";

    public SimplePojo(String message, String id){
        this.message = message;
        this.id = id;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

}
