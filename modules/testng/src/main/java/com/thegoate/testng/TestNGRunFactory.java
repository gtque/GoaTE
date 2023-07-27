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
import com.thegoate.data.GoateProvider;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.utils.GoateUtils;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.thegoate.dsl.words.EutConfigDSL.eut;

/**
 * Loads the run data and builds the runs.
 * Created by Eric Angeli on 5/11/2017.
 */
public class TestNGRunFactory {
    static final BleatBox LOG = BleatFactory.getLogger(TestNGRunFactory.class);
    public static final boolean runCacheEnabled = eut("run.cache.enabled", true, Boolean.class);
    public static final Map<String, Object[][]> providerCache = new ConcurrentHashMap<>();

    public static String providerCacheDefaultId(GoateProvider gp) {
        return gp != null ? (gp.name() + ":") : ("" + System.nanoTime());
    }

    public static Object[][] loadRuns(Goate runData, Goate constantData, boolean atLeastOneRun, String[] include, String[] exclude) {
        return loadRuns(null, runData, constantData, atLeastOneRun, include, exclude);
    }

    public static Object[][] loadRuns(Method method, Goate runData, Goate constantData, boolean atLeastOneRun, String[] include, String[] exclude) {
        return loadRuns(method, null, runData, constantData, atLeastOneRun, include, exclude);
    }

    public static Object[][] loadRuns(Method method, GoateProvider provider, Goate runData, Goate constantData, boolean atLeastOneRun, String[] include, String[] exclude) {
        boolean included = true;
        String excludeMessage = "";
        if (method != null) {
            String[] groups = method.getAnnotation(Test.class).groups();
            for (String group : groups) {
                if (group.contains("::")) {
                    included = Boolean.parseBoolean("" + new Goate().get("group", group));
                    excludeMessage = method.getName() + ">" + group;
                }
            }
        }
        String constantGroups = null;
        StringBuilder constantGroupsBuilder = new StringBuilder("");
        List<Goate> runs = new ArrayList<>();
        Goate constants = new Goate();
        Object[][] rawData = {};
        boolean alreadyCached = true;
        if (!included) {
            LOG.debug("test is being excluded: " + excludeMessage);
        } else {
            String providerCacheId = provider != null ? (provider.name() + ":" + provider.nickName()) : "";
            if (!runCacheEnabled || (provider != null && !providerCache.containsKey(providerCacheId)) || provider == null) {
                if (runData != null) {
                    for (String key : runData.keys()) {
                        List<Goate> list = ((DataLoader) runData.get(key)).load();
                        runs.addAll(list);
                    }
                }
                if (constantData != null) {
                    for (String key : constantData.keys()) {
//                        if (key.equals("_goate:method")) {
//                            constants.put(key, constantData.get(key));
//                        } else {
                        Goate cd = ((DataLoader) constantData.get(key)).load().get(0);
                        constants.merge(cd, true);
                        String tempGroups = cd.get("groups", null, String.class);
                        if (tempGroups != null) {
                            if (constantGroupsBuilder.length() == 0) {
                                constantGroupsBuilder.append(tempGroups);
                            } else {
                                constantGroupsBuilder.append(",").append(tempGroups);
                            }
                        }
                        //the last loaded value of the constant wins.
//                        }
                    }
                    constantGroups = constantGroupsBuilder.toString();
                }
                if (runs.size() == 0) {
                    runs.add(null);
                }
                //if (constants.size() > 0) {
                boolean ogFilter = Boolean.parseBoolean("" + GoateUtils.getProperty("ORIGINAL_FILTER", false));
                if (ogFilter) {
                    if (constants.size() > 0) {
                        for (Goate data : runs) {
                            int i = -42;
                            if (data == null) {
                                i = runs.indexOf(data);
                                data = new Goate();
                            }
                            data.merge(constants, false);
                            if(data != null) {
                                String theRunGroups = data.get("groups", null, String.class);
                                if (constantGroups != null  && !constantGroups.isEmpty()) {
                                    if (theRunGroups == null) {
                                        data.put("groups", constantGroups);
                                    } else {
                                        data.put("groups", theRunGroups + "," + constantGroups);
                                    }
                                }
                            }
                            if (i != -42) {
                                runs.set(i, data);
                            }
                            //a constant can be overloaded by setting it in the run data.
                        }
                    }
                    runs = filter(runs);
                } else {
                    int constantsSize = constants.size();
                    List<Goate> filteredRuns = new ArrayList<>();
                    String runGroup = "" + GoateUtils.getProperty("runGroups", "empty::");
                    boolean doFilterRunGroups = !(runGroup.equals("null") || runGroup.isEmpty());
                    String[] runGroups = ("-r-" + runGroup.replace(",", "-r-,-r-") + "-r-").replaceAll("\\s*-r-\\s*", "-r-").split(",");
                    String runId = "" + GoateUtils.getProperty("run", "empty::");
                    boolean doFilterRunIds = !(runId.equals("null") || runId.isEmpty());
                    String runDelimiter = "" + GoateUtils.getProperty("runDelimiter", ",");
                    runId = "-r-" + runId.replace(runDelimiter, "-r-" + runDelimiter + "-r-") + "-r-";
                    String[] runIds = runId.split(runDelimiter);
                    int count = 0;
                    for (Goate data : runs) {
//                    int i = -42;
                        count++;
                        if (data == null && constantsSize > 0) {
//                        i = runs.indexOf(data);
                            data = new Goate();
                        }
                        if(data != null) {
                            String theRunGroups = data.get("groups", null, String.class);
                            if (constantGroups != null && !constantGroups.isEmpty()) {
                                if (theRunGroups == null) {
                                    data.put("groups", constantGroups);
                                } else {
                                    data.put("groups", theRunGroups + "," + constantGroups);
                                }
                            }
                        }
                        if (constantsSize > 0) {
                            data.merge(constants, false);
//                        if (i != -42) {
//                            runs.set(i, data);
//                        }
                        }
                        boolean doRun = true;
                        if (data != null) {
                            doRun = runEnabled(data);
                            if (doRun) {
                                if (doFilterRunGroups) {
                                    doRun = filterRun(data, runGroups);
                                }
                                if (doRun) {
                                    if (doFilterRunIds) {
                                        doRun = filterRunByIds(data, runIds, count);
                                    }
                                }
                                if (doRun) {
                                    filteredRuns.add(data);
                                }
                            }
                        }
                        //a constant can be overloaded by setting it in the run data.
                    }
                    //}
                    //runs = filter(runs);
                    runs = filteredRuns;
                }
                if (runs.size() == 1 && runs.get(0) == null) {
                    runs = new ArrayList<>();
                }
                rawData = new Object[runs.size()][runs.size() > 0 ? 1 : 0];
                Object[][] rawClone = new Object[runs.size()][runs.size() > 0 ? 1 : 0];
                for (int i = 0; i < rawData.length; i++) {
                    rawData[i][0] = runs.get(i);
                    if (!providerCacheId.isEmpty()) {
                        try {
                            rawClone[i][0] = runs.get(i).clone();
                        } catch (CloneNotSupportedException e) {
                            LOG.debug("Run Factory", "problem clone raw run data: " + e.getMessage());
                        }
                    }
                }
                if (!providerCacheId.isEmpty()) {
                    providerCache.put(providerCacheId, rawClone);
                    alreadyCached = false;
                }
            }
            if (runCacheEnabled && provider != null && alreadyCached) {
                if (provider.nickName().isEmpty()) {// !providerCache.containsKey(providerCacheId)){
                    Object[][] oData = providerCache.get(providerCacheId);
                    rawData = cloneRun(oData);
                } else if (!providerCache.containsKey(providerCacheId)) {
                    Object[][] oData = providerCache.get(provider.name() + ":");
                    rawData = cloneRun(oData);
                    providerCache.put(providerCacheId, rawData);
                } else {
                    rawData = providerCache.get(providerCacheId);
                }
            }
        }
        Object[][] empty = {};
        return rawData.length > 0 ? rawData : empty;
    }

    private static Object[][] cloneRun(Object[][] oData) {
        Object[][] rData = null;
        if (oData != null) {
            rData = new Object[oData.length][1];
            for (int i = 0; i < rData.length; i++) {
                if (oData[i][0] != null) {
                    Goate gData = (Goate) oData[i][0];
                    try {
                        rData[i][0] = gData.clone();
                    } catch (CloneNotSupportedException e) {
                        LOG.debug("Run Factory", "Unable to do a deep clone of the the run data, hopefully a shallow copy is good enough.");
                        rData[i][0] = gData;
                    }
                }
            }
        }
        return rData;
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

    protected static boolean checkRunGroups(Goate runData, String[] runGroups) {
        boolean check = true;
        if (runGroups != null && runGroups.length > 0) {
            Object run = runData;
            if (runData != null) {
                run = runData.get("groups");
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

    protected static boolean filterRun(Goate run, String[] runIds) {
        if (runIds != null) {
            return checkRunGroups(run, runIds);
        }
        return true;
    }

    protected static boolean filterRunByIds(Goate run, String[] runIds, int runNumber) {
        if (runIds != null) {
            return checkRunIds(run, runIds, runNumber);
        }
        return true;
    }

    protected static boolean checkRunIds(Goate runData, String[] runIds, int runNumber) {
        boolean check = true;
        if (runIds != null && runIds.length > 0) {
            String run = "";
            String sKey = runData.findKeyIgnoreCase("Scenario");
            run = "" + runData.get(sKey);
            //String runsIn = ("-r-" + run + "-r-").replace(",", "-r-,-r-").replaceAll("\\s*-r-\\s*", "-r-");
            String runsIn = "-r-" + run + "-r-,-r-" + runNumber + "-r-";
            check = Arrays.stream(runIds).parallel().anyMatch(runsIn::contains);

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

    protected static boolean runEnabled(Goate run) {
        Object b = "true";
        if (run != null) {
            b = run.get("runEnabled", "true");
            run.drop("runEnabled");
        }
        return Boolean.parseBoolean("" + b);
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
