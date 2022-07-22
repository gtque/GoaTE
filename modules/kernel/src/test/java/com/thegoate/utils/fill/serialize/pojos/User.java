package com.thegoate.utils.fill.serialize.pojos;

import com.thegoate.utils.fill.serialize.Kid;

public class User extends Kid {
     private String userName;

    public String getUserName() {
        return userName;
    }

    public User setUserName(String userName) {
        this.userName = userName;
        return this;
    }
}
