package com.thegoate.expect;

import com.thegoate.Goate;
import com.thegoate.annotations.GoateDescription;
import com.thegoate.dsl.DSL;
import com.thegoate.dsl.GoateDSL;

/**
 * Useful for building paths in expectations with a wildcard index, such as json arrays.
 * Created by Eric Angeli on 11/29/2018.
 */
@GoateDSL(word = "matchWildcardIndex")
@GoateDescription(description = "uses the same index for the corresponding wildCardIndex, must be used in conjunction with wildcardIndex. Typically used in the expected path of an expectation.",
        parameters = {"matchIndex:=the index value to be matched in the 'actual' key path."})
public class ExpectMatchWildcardIndexPath extends DSL {

    public ExpectMatchWildcardIndexPath(Object value) {
        super(value);
    }

    public static String matchWildcardIndex() {
        return "%";
    }

    public static String matchWildcardIndex(int matchIndex) {
        StringBuilder wc = new StringBuilder("");
        for (; matchIndex > 0; matchIndex--) {
            wc.append("\\");
        }
        return wc.append("%").toString();
    }

    @Override
    public Object evaluate(Goate data) {
        Object first = get(1, data);
        return matchWildcardIndex(Integer.parseInt("" + first));
    }
}
