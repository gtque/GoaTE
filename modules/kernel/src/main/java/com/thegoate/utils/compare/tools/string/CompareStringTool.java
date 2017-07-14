package com.thegoate.utils.compare.tools.string;

import com.thegoate.utils.compare.CompareTool;

/**
 * Created by Eric Angeli on 5/9/2017.
 */
public abstract class CompareStringTool extends CompareTool {
    public CompareStringTool(Object actual) {
        super(actual);
    }

    @Override
    public boolean isType(Object check) {
        return check instanceof String;
    }
}
