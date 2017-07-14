package com.thegoate.utils.compare.tools.l;

import com.thegoate.utils.compare.CompareTool;

/**
 * Created by Eric Angeli on 7/14/2017.
 */
public abstract class CompareLongTool extends CompareTool {
    public CompareLongTool(Object actual) {
        super(actual);
    }

    @Override
    public boolean isType(Object check) {
        boolean istype = false;
        try{
            Long.parseLong(""+actual);
            istype = true;
        }catch(Throwable e){
//            LOG.debug(""+actual + " is not a long.");
        }
        return istype;
    }
}
