package com.thegoate.info;

import org.testng.annotations.Test;

/**
 * Created by Eric Angeli on 11/28/2018.
 */
public class CheckEncyclopedia {
    @Test(groups = {"unit"})
    public void simpleCheckEncyclopedia(){
        String[] args = {};
        GoateEncyclopedia.main(args);
    }
}
