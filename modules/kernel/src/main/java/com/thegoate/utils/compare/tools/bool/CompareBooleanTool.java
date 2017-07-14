package com.thegoate.utils.compare.tools.bool;

import com.thegoate.utils.compare.CompareTool;

/**
 * Created by Eric Angeli on 5/9/2017.
 */
public abstract class CompareBooleanTool extends CompareTool {
    public CompareBooleanTool(Object actual) {
        super(actual);
    }

    @Override
    public boolean isType(Object check) {
        return (check instanceof Boolean || (check != null && check.getClass().equals(Boolean.TYPE)));
    }

}
