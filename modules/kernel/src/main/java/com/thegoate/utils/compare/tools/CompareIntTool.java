package com.thegoate.utils.compare.tools;

import com.thegoate.utils.compare.CompareTool;

/**
 * Created by Eric Angeli on 5/9/2017.
 */
public abstract class CompareIntTool extends CompareTool {
    public CompareIntTool(Object actual) {
        super(actual);
    }

    @Override
    public boolean isType(Object check) {
        boolean istype = false;
        try{
            Integer.parseInt(""+actual);
            istype = true;
        }catch(Throwable e){
            LOG.debug(""+actual + " is not an int.");
        }
        return istype;
    }
}
