package com.thegoate.utils.fill.serialize.pojos;

import com.thegoate.utils.fill.serialize.Kid;

public class Doug extends Kid {

    int theAnswer = 42;

    Steve theClone = new Steve();

    public Doug setTheAnswer(int theAnswer) {
        this.theAnswer = theAnswer;
        return this;
    }

    public int getTheAnswer() {
        return theAnswer;
    }

    public Doug setTheClone(Steve theClone) {
        this.theClone = theClone;
        return this;
    }

    public Steve getTheClone() {
        return theClone;
    }
}
