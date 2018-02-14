package com.thegoate.metrics;

import com.thegoate.Goate;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Stopwatch class for measuring time.
 * This uses System.nanoTime() to track the time, where the total time is the difference between the start and end times.
 * Created by Eric Angeli on 2/12/2018.
 */
public class Stopwatch {

    public static Stopwatch global = new Stopwatch();

    private final BleatBox LOG = BleatFactory.getLogger(getClass());

    private volatile Goate timers = new Goate();
    private volatile String started = null;

    public Stopwatch(){
        lap(":overall:");
    }

    public Timer lap(String lap){
        LOG.debug("Stopwatch", "lap: " + lap);
        return timers.get("lap", new Timer(), Timer.class);
    }

    public Stopwatch start(){
        return start(":overall:");
    }

    public Stopwatch start(String lap) {
        return start(lap, System.nanoTime());
    }

    public Stopwatch start(String lap, long start){
        if(!lap.equals(":overall:")&&lap(":overall:").getStart()==0L){
            start(":overall:",start);
            started = lap;
        }
        LOG.debug("Stopwatch", "Lap("+lap+") start: " + start);
        lap(lap).start(start);
        return this;
    }

    public Stopwatch stop(){
        return stop(":overall:");
    }

    public Stopwatch stop(String lap) {
        return stop(lap, System.nanoTime());
    }

    public Stopwatch stop(String lap, long stop){
        if(lap.equals(started)){
            stop(":overall:",stop);
        }
        LOG.debug("Stopwatch", "Lap("+lap+") stop: " + stop);
        lap(lap).stop(stop);
        return this;
    }

    public Stopwatch split(String lap){
        if(lap!=null) {
            lap(lap).split();
        }
        return this;
    }

    public void stopAll() {
        for (String lap:timers.keys()) {
            Timer clock = timers.get(lap,null,Timer.class);
            if (clock.getStop() == 0L)
                clock.stop();
        }
    }

    public void clearAllLaps(){
        timers = new Goate();
    }

    public void clearLaps(String lapPattern) {
        for (String lap:timers.keys()) {
            if (lap.contains(lapPattern)){//change this to use regex pattern matcher?
                timers.drop(lap);
            }
        }
    }

    public Goate getTimers(){
        return timers;
    }

    public class Timer {
        long start = 0L;
        long stop = 0L;
        Map<String,Long> splits = new HashMap<>();

        public Map<String, Long> splits(){
            return this.splits;
        }

        void split(){
            split(""+splits.size());
        }

        void split(String split){
            splits.put(split, System.nanoTime());
        }

        void start(long time) {
            if (start == 0L) {
                if (time >= 0L) {
                    start = time;
                } else {
                    start();
                }
            }
        }

        void stop(long time) {
            if (stop == 0L) {
                if (time >= 0L) {
                    stop = time;
                } else {
                    stop();
                }
            }
        }

        void start() {
            start(System.nanoTime());
        }

        void stop() {
            stop(System.nanoTime());
        }

        public long getTime() {
            return stop - start;
        }

        public long getStart() {
            return start;
        }

        public long getStop() {
            return stop;
        }
    }
}
