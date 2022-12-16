package com.thegoate.utils.cmd;

import com.thegoate.Goate;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Command {

    BleatBox LOG = BleatFactory.getLogger(getClass());

    List<String> cmd = new ArrayList<>();

    public Command(String command){
        if(command!=null && !command.isEmpty()) {
            this.cmd.add(command);
        }
    }

    public Command arg(String arg){
        cmd.add(arg);
        return this;
    }

    //int exitCode = -42;
    //        try{
    //            Process process = builder.start();
    //            StreamGobbler streamGobbler =
    //                    new StreamGobbler(process.getInputStream(), System.out::println);
    //            Executors.newSingleThreadExecutor().submit(streamGobbler);
    //            exitCode = process.waitFor();
    //        } catch (InterruptedException | IOException e) {
    //            LOG.error("kubectl", "problem clearing redis: " + e.getMessage(), e);
    //        }
    public Goate execute() {
        String commandOutput = "";
        int exitCode = -42;
        if(cmd!=null && cmd.size()>0) {
            ProcessBuilder builder = new ProcessBuilder(cmd);
            try {
                Process process = builder.start();
                Trough bucket = new Trough();
                StreamGobbler streamGobbler =
                        new StreamGobbler(process.getInputStream(), bucket::eat);
                Executors.newSingleThreadExecutor().submit(streamGobbler);
                exitCode = process.waitFor();
                commandOutput = bucket.getFeed();
            } catch (InterruptedException | IOException e) {
                LOG.error("command", "problem running command: " + e.getMessage(), e);
                commandOutput = e.getMessage();
            }
        } else {
            commandOutput = "no command defined";
        }
        return new Goate().put("exit code", exitCode).put("output", commandOutput);
    }

    private static class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(consumer);
        }
    }

    private class Trough {
        StringBuilder feed = new StringBuilder();

        String getFeed(){
            return feed.toString();
        }

        void eat(String food){
            System.out.println(food);
            feed.append(food).append("\n");
        }
    }
}
