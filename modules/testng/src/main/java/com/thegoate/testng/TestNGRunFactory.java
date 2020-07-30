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
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.utils.GoateUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Loads the run data and builds the runs.
 * Created by Eric Angeli on 5/11/2017.
 */
public class TestNGRunFactory {
    static final BleatBox LOG = BleatFactory.getLogger(TestNGRunFactory.class);

    public static Object[][] loadRuns(Goate runData, Goate constantData, boolean atLeastOneRun, String[] include, String[] exclude) {
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
                if (key.equals("_goate:method")) {
                    constants.put(key, constantData.get(key));
                } else {
                    constants.merge(((DataLoader) constantData.get(key)).load().get(0), true);
                    //the last loaded value of the constant wins.
                }
            }
        }
        if (runs.size() == 0) {
            runs.add(null);
        }
        if (constants.size() > 0) {
            for (Goate data : runs) {
                int i = -42;
                if (data == null) {
                    i = runs.indexOf(data);
                    data = new Goate();
                }
                data.merge(constants, false);
                if (i != -42) {
                    runs.set(i, data);
                }
                //a constant can be overloaded by setting it in the run data.
            }
        }
        runs = filter(runs);
        if (runs.size() == 1 && runs.get(0) == null) {
            runs = new ArrayList<>();
        }
        Object[][] rawData = new Object[runs.size()][runs.size() > 0 ? 1 : 0];
        for (int i = 0; i < rawData.length; i++) {
            rawData[i][0] = runs.get(i);
        }
        Object[][] empty = {};
        return rawData.length > 0 ? rawData : empty;
    }

    protected static List<Goate> filterGroups(List<Goate> runs, String[] include, String[] exclude) {
        List<Goate> filteredInc = new ArrayList<>();
        List<Goate> filtered = new ArrayList<>();
        if (include != null && include.length > 0) {
            for (Goate run : runs) {
                String groups = run.get("groups", "", String.class);
                if (!groups.isEmpty()) {

                }
            }
        }

        return filtered;
    }

    protected static boolean checkRunGroups(Object runData, String[] runGroups) {
        boolean check = true;
        if (runGroups != null && runGroups.length > 0) {
            Object run = runData;
            if (runData != null && runData instanceof Goate) {
                run = ((Goate) runData).get("groups");
            }
            if (run != null) {
                if (run instanceof String) {
                    String runsIn = ("-r-" + run + "-r-").replace(",", "-r-,-r-").replaceAll("\\s*-r-\\s*", "-r-");
                    check = Arrays.stream(runGroups).parallel().anyMatch(runsIn::contains);
                } else {
                    check = false;
                }
            } else {
                check = false;
            }
        }
        return check;
    }

    protected static List<Goate> filterRunGroups(List<Goate> runs) {
        List<Goate> filtered = runs;
        String runId = "" + GoateUtils.getProperty("runGroups", "empty::");
        if (!runId.equals("null") && !runId.isEmpty()) {
            String[] runIds = ("-r-" + runId.replace(",", "-r-,-r-") + "-r-").replaceAll("\\s*-r-\\s*", "-r-").split(",");
            filtered = runs.stream().filter(run -> checkRunGroups(run, runIds)).collect(Collectors.toList());
        }

        return filtered;
    }

    protected static List<Goate> filter(List<Goate> runs) {
        List<Goate> filtered = new ArrayList<>();
        String runId = "" + GoateUtils.getProperty("run", "empty::");
        if (!runId.equals("null") && !runId.isEmpty()) {
            String[] runIds = runId.split("" + GoateUtils.getProperty("runDelimiter", ","));
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
                    if (rd != null) {
                        boolean runEnabled = Boolean.parseBoolean("" + rd.get("runEnabled", "true"));
                        if (runEnabled) {
                            if (runNum >= 0) {
                                if (runNum == count) {
                                    filtered.add(rd.drop("runEnabled"));
                                }
                            } else {
                                String sKey = rd.findKeyIgnoreCase("Scenario");
                                String scene = "" + rd.get(sKey);
                                if (scene.equals(run)) {
                                    filtered.add(rd.drop("runEnabled"));
                                }
                            }
                        }
                    }
                }
            }
        } else {
            for (Goate rd : runs) {
                Object b = "true";
                if (rd != null) {
                    b = rd.get("runEnabled", "true");
                    rd.drop("runEnabled");
                }
                boolean runEnabled = Boolean.parseBoolean("" + b);
                if (runEnabled) {
                    filtered.add(rd);
                }
            }
        }
        return filterRunGroups(filtered);
    }
}
