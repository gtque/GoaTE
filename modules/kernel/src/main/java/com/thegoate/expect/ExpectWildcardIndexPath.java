package com.thegoate.expect;

import com.thegoate.Goate;
import com.thegoate.annotations.GoateDescription;
import com.thegoate.dsl.DSL;
import com.thegoate.dsl.GoateDSL;

/**
 * Useful for building paths in expectations with a wildcard index, such as json arrays.
 * Created by Eric Angeli on 11/29/2018.
 */
@GoateDSL(word = "wildcardIndex")
@GoateDescription(description = "returns the pattern for a wild card index.",
parameters = {"expectedSize:=the expected size of the array, optional","startingIndex:=the index to start at, optional, if specified than expectedSize must also be set."})
public class ExpectWildcardIndexPath extends DSL {

    public ExpectWildcardIndexPath(Object value) {
        super(value);
    }

    public static String wildcardIndex(){
        return "*";
    }

    public static String wildcardIndex(int expectedSize){
        return "[*"+expectedSize+"]";
    }

    public static String wildcardIndex(int expectedSize, int startingIndex){
        return "[*"+startingIndex+","+expectedSize+"]";
    }

    @Override
    public Object evaluate(Goate data) {
        String wildcard = "*";
        Object first = get(1,data);
        Object second = get(2, data);
        if(second!=null){
            wildcard = "[*"+second+","+first+"]";
        } else {
            if(first!=null){
                wildcard = "[*"+first+"]";
            }
        }
        return wildcard;
    }
}
