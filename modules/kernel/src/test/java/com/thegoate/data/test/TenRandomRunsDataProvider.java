package com.thegoate.data.test;

import com.thegoate.Goate;
import com.thegoate.data.DataLoader;

import java.util.ArrayList;
import java.util.List;

public class TenRandomRunsDataProvider extends DataLoader {
    @Override
    public List<Goate> load() {
        List<Goate> runs = new ArrayList<>();
        for(int i = 0; i<10; i++){
            //create a goate collection that will contain the data from the run.
            Goate run = new Goate();
            //add the data to the goate run collection
            run.put("randomdata", (int)(100*Math.random()));
            //add the run to the collection of runs.
            runs.add(run);
        }
        //return the list of runs.
        return runs;
    }
}
