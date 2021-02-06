package com.thegoate.reflection;

import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.reflection.test.SkipThread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Executioner<T> {
    BleatBox LOG = BleatFactory.getLogger(getClass());

    int threadPoolSize = 1;

    public Executioner(){

    }

    public Executioner(int threadPoolSize){
        this.threadPoolSize = threadPoolSize;
    }

    public boolean process(List<T> threads) {
        ExecutorService es = Executors.newFixedThreadPool(threadPoolSize);
        boolean running = true;
        int expected = threads.size();
        List<Future<?>> futures = new ArrayList<>();
        for (T thread:threads) {
            if(thread instanceof Thread && !(thread instanceof SkipThread)) {
                futures.add(es.submit((Thread)thread));
            }
        }
        int completed = 0;
        while(running){
            running = false;
            completed = 0;
            for(Future<?> f:futures){
                if(!f.isDone()){
                    running = true;
                } else {
                    completed++;
                }
            }
        }
        try {
            LOG.debug("shutting down executor");
            es.shutdown();
            es.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            LOG.error("interrupted: " + e.getMessage(), e);
        }
        finally {
            if (!es.isTerminated()) {
                LOG.error("force cancel non-finished tasks");
            }
            es.shutdownNow();
            LOG.debug("shutdown finished");
        }
        return expected==completed;
    }
}
