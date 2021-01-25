package com.thegoate.utils.fill.serialize.pojos;

import com.thegoate.utils.fill.serialize.GoateSource;
import com.thegoate.utils.fill.serialize.Kid;

public class CastOnSerliazing extends Kid {

    @GoateSource(source = Cheese.class, key="i", serializeTo = Integer.class)
    private boolean b = true;

    public void setB(boolean b){
        this.b = b;
    }

    public boolean isB(){
        return this.b;
    }

}
