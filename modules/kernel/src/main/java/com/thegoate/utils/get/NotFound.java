package com.thegoate.utils.get;

/**
 * Simple POJO for use when something is not found.
 * Created by Eric Angeli on 6/15/2017.
 */
public class NotFound {
    String id = "";

    public NotFound(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }
}
