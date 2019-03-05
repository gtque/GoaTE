package com.thegoate.performance;

/**
 * Created by Eric Angeli on 5/17/2018.
 */
public class PerformanceRunnerSettings {
    /**
     * The run type:
     * METERED - start the number of steps specified at the
     *  given interval, regardless of the number currently
     *  executing, but only up to the maximum allowed.
     * SIMPLE_MAX - only allow the number of steps specified
     *  to be run at any given time, starting the next when
     *  a thread is available.
     */
    enum RunType{
        METERED,SIMPLE_MAX
    }
    enum TimeUnit{
        n(1L),ms(1000L),s(1000000000L),m(60000000000L),h(3600000000000L);
        long multiplier = 1L;
        TimeUnit(long multiplier){
            this.multiplier = multiplier;
        }
        long getTime(long time){
            return time*multiplier;
        }
    }
    RunType type = RunType.SIMPLE_MAX;
    TimeUnit timeUnit = TimeUnit.s;
    long interval = 0L;
    float duration = 1f;
    int stepsAtATime = 1;
    int maxThreadCount = 1;//configurable per agent.
    final int minThreadCount = 1;
    int maximumAtATime = Integer.MAX_VALUE;
    int periods = 0;
    int numberOfRuns = 0;
    int uniqueRuns = -1;
    boolean beforeMethod = false;
    boolean afterMethod = false;
}
