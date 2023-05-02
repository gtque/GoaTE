package com.thegoate.expect.conditional;

import com.thegoate.Goate;
import com.thegoate.expect.Expectation;
import com.thegoate.expect.ExpectationThreadBuilder;
import com.thegoate.expect.builder.ExpectationBuilder;

import java.util.ArrayList;
import java.util.List;

public class ExpectIf extends Expectation {

    private boolean expectIf = true;
    private boolean addingIf = true;
    List<ExpectationThreadBuilder> etbIf = new ArrayList<>();
    List<ExpectationThreadBuilder> etb = new ArrayList<>();

    public ExpectIf(Goate data) {
        super(data);
    }

    public static ExpectIf thisIsTrue(Expectation expectation){
        return buildIf().add(expectation).expectIf(true);
    }

    public static ExpectIf thisIsTrue(){
        return buildIf().expectIf(true);
    }

    public static ExpectIf thisIsFalse(Expectation expectation){
        return buildIf().add(expectation).expectIf(false);
    }

    public static ExpectIf thisIsFalse(){
        return buildIf().expectIf(false);
    }

    private static ExpectIf buildIf() {
        return new ExpectIf(new Goate());
    }

    public ExpectIf add(Expectation expectation) {
        return and(expectation);
    }

    public ExpectIf and(Expectation expectation) {
        if(addingIf) {
            if(etbIf.size()==0){
                etbIf.add(new ExpectationThreadBuilder(data));
            }
            etbIf.get(etbIf.size()-1).expect(expectation);
        } else {
            if(etb.size()==0){
                etb.add(new ExpectationThreadBuilder(data));
            }
            etb.get(etb.size()-1).expect(expectation);
        }
        return this;
    }

    public ExpectIf or(Expectation expectation) {
        if(addingIf) {
            etbIf.add(new ExpectationThreadBuilder(data));
        } else {
            etb.add(new ExpectationThreadBuilder(data));
        }

        return and(expectation);
    }

    public ExpectIf expectIf(boolean expectIf) {
        this.expectIf = expectIf;
        return this;
    }


    public ExpectIf expect(Expectation expectation) {
        return this;
    }

    public ExpectIf expect(ExpectationBuilder expectationBuilder) {
        return expect(expectationBuilder.build());
    }

    public ExpectIf expect(List<Expectation> expectationList) {
        expectationList.forEach(this::expect);
        return this;
    }
}
