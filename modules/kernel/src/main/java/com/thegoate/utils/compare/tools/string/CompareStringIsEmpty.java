package com.thegoate.utils.compare.tools.string;

import com.thegoate.annotations.IsDefault;
import com.thegoate.utils.compare.CompareUtil;

/**
 * Checks if a string is empty or not.
 * Returns true only if actual is not null and is empty and if expected is true.
 * Returns true if actual is not empty or null and if expected is false.
 * Use == null or != null to check for null.
 *  example: field1,==,null::
 * Created by Eric Angeli on 7/7/2017.
 */
@CompareUtil(operator = "isEmpty", type="String")
@IsDefault
public class CompareStringIsEmpty extends CompareStringTool {
    public CompareStringIsEmpty(Object actual) {
        super(actual);
    }

    @Override
    public boolean evaluate() {
        boolean check = Boolean.parseBoolean(""+expected);
        String act = "" + actual;
        return check?act.isEmpty():!act.isEmpty();
    }
}
