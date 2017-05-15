/*
 * Copyright (c) 2017. Eric Angeli
 *
 *  Permission is hereby granted, free of charge,
 *  to any person obtaining a copy of this software
 *  and associated documentation files (the "Software"),
 *  to deal in the Software without restriction,
 *  including without limitation the rights to use, copy,
 *  modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit
 *  persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission
 *  notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 *  AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 */
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

    public static Object[][] loadRuns(Goate runData, Goate constantData, boolean atLeastOneRun) {
        List<Goate> runs = new ArrayList<>();
        Goate constants = new Goate();
        if (runData != null) {
            for (String key : runData.keys()) {
                List<Goate> list = ((DataLoader) runData.get(key)).load();
                runs.addAll(list);
            }
        }
        if (constantData != null) {
            for (String key : constantData.keys()) {
                constants.merge(((DataLoader) constantData.get(key)).load().get(0), true);
                //the last loaded value of the constant wins.
            }
        }
        if (runs.size() == 0) {
            runs.add(new Goate());
        }
        runs = filter(runs);
        if (constants.size() > 0) {
            for (Goate data : runs) {
                data.merge(constants, false);
                //a constant can be overloaded by setting it in the run data.
            }
        }
        Object[][] rawData = new Object[runs.size()][1];
        for (int i = 0; i < rawData.length; i++) {
            rawData[i][0] = runs.get(i);
        }
        return rawData;
    }

    protected static List<Goate> filter(List<Goate> runs) {
        List<Goate> filtered = new ArrayList<>();
        String runId = "" + GoateUtils.getProperty("run", "empty::");
        if (!runId.equals("null") && !runId.isEmpty()) {
            String[] runIds = runId.split(",");
            for (String run : runIds) {
                int runNum = -42;
                try {
                    runNum = Integer.parseInt(run);
                    LOG.debug("Filtering by run number: " + runNum);
                } catch (Throwable t) {
                    LOG.debug("Filtering by scenario: " + run);
                }
                int count = 0;
                for (Goate rd : runs) {
                    count++;
                    if (runNum >= 0) {
                        if (runNum == count) {
                            filtered.add(rd);
                        }
                    } else {
                        String scene = "" + rd.get("Scenario");
                        if (scene.equals(run)) {
                            filtered.add(rd);
                        }
                    }
                }
            }
        } else {
            filtered = runs;
        }
        return filtered;
    }
}
