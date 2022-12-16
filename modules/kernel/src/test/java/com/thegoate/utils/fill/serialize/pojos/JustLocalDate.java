package com.thegoate.utils.fill.serialize.pojos;

import com.thegoate.utils.fill.serialize.Kid;

import java.time.LocalDate;

public class JustLocalDate extends Kid {

    private LocalDate theDate;

    public LocalDate getTheDate() {
        return theDate;
    }

    public JustLocalDate setTheDate(LocalDate theDate) {
        this.theDate = theDate;
        return this;
    }
}
