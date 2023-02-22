package com.thegoate.utils.fill.serialize.pojos;

import com.thegoate.utils.fill.serialize.Kid;

public class Steve extends Kid {

    String theWord = "good";

    public Steve setTheWord(String theWord) {
        this.theWord = theWord;
        return this;
    }

    public String getTheWord() {
        return theWord;
    }

}
