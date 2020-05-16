package com.thegoate.logging;

import java.util.logging.Level;

import com.thegoate.Goate;

/**
 * Place holder for possible future support of getting/checking enabled log level.
 * Only partially thought out at this time. But still need something that stores the basic log level.
 * Need to revisit this in the future to full flesh out the idea.
 * Created by Eric Angeli on 6/6/2017.
 */
public class BleatLevel extends Level{

    protected BleatLevel(String name, int value) {
        super(name, value);
    }

    public static BleatLevel level(Level level, String name){
        BleatLevel volume = new BleatLevel(level.getName(), level.intValue());
        return volume.tune(name);
    }

    public static BleatLevel level(String name, int volume){
        return new BleatLevel(name, volume);
    }

    public boolean isLoudEnough(Level voice){
        return voice.intValue() >= intValue();
    }

    public BleatLevel tune(String name){
        //ToDo:Implement looking up volume adjustments for pass,fail,skip,unknown
        //new Goate().get("bleat.level", "bleat volume::"+loggingClass.getName(), Level.class)
        return this;
    }
}
