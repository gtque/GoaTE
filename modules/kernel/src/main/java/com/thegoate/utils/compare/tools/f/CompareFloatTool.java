package com.thegoate.utils.compare.tools.f;

import com.thegoate.utils.compare.CompareTool;

/**
 * Created by Eric Angeli on 7/14/2017.
 */
public abstract class CompareFloatTool extends CompareTool {
    public CompareFloatTool(Object actual) {
        super(actual);
    }

    @Override
    public boolean isType(Object check) {
        boolean istype = false;
        try{
            Float.parseFloat(""+actual);
            istype = true;
        }catch(Throwable e){
//            LOG.debug(""+actual + " is not a float.");
        }
        return istype;
    }
}
