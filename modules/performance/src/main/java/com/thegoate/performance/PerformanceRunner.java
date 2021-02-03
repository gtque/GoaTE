package com.thegoate.performance;

import com.thegoate.Goate;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.metrics.Stopwatch;
import com.thegoate.performance.threading.LoadPoolManager;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Eric Angeli on 5/17/2018.
 */
public class PerformanceRunner {
    private final BleatBox LOG = BleatFactory.getLogger(getClass());
    Goate initialData = null;
    Goate flow;
    Goate runData;
    Goate constantData;
    LoadPoolManager payload;
    Stopwatch timer = null;
    final ArrayList<CyclicBarrier> gates = new ArrayList<>();//= new CyclicBarrier(load*provinces.length+1);
    Thread timeChecker;
    boolean timesUp = false;
    int failCount = 0;
}
