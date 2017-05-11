package com.thegoate.testng;

import com.thegoate.Goate;
import com.thegoate.data.DataLoader;
import com.thegoate.utils.GoateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Loads the run data and builds the runs.
 * Created by Eric Angeli on 5/11/2017.
 */
public class TestNGRunFactory {
    static final Logger LOG = LoggerFactory.getLogger(TestNGRunFactory.class);
    public static Object[][] loadRuns(Goate runData, Goate constantData){
        List<Goate> runs = new ArrayList<>();
        Goate constants = new Goate();
        if(runData!=null) {
            for (String key : runData.keys()) {
                runs.addAll(((DataLoader)runData.get(key)).load());
            }
        }
        if(constantData!=null) {
            for (String key : runData.keys()) {
                constants.merge(((DataLoader)runData.get(key)).load().get(0), true);
                //the last loaded value of the constant wins.
            }
        }
        runs = filter(runs);
        if(constants.size()>0) {
            for (Goate data : runs) {
                data.merge(constants, false);
                //a constant can be overloaded by setting it in the run data.
            }
        }
        Object[][] rawData = new Object[runs.size()][1];
        for(int i = 0; i<rawData.length; i++){
            rawData[i][0] = runs.get(i);
        }
        return rawData;
    }

    protected static List<Goate> filter(List<Goate> runs){
        List<Goate> filtered = new ArrayList<>();
        String runId = ""+GoateUtils.getProperty("run","empty::");
        if(!runId.equals("null")&&!runId.isEmpty()){
            String[] runIds = runId.split(",");
            for(String run:runIds) {
                int runNum = -42;
                try {
                    runNum = Integer.parseInt(run);
                    LOG.debug("Filtering by run number: " + runNum);
                } catch (Throwable t) {
                    LOG.debug("Filtering by scenario: " + run);
                }
                int count = 0;
                for(Goate rd:runs){
                    if(runNum>=0){
                        if(runNum == count){
                            filtered.add(rd);
                        }else{
                            String scene = "" + rd.get("Scenario");
                            if(scene.equals(run)){
                                filtered.add(rd);
                            }
                        }
                    }
                    count++;
                }
            }
        }else{
            filtered = runs;
        }
        return filtered;
    }
}
