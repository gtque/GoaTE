package com.thegoate.utils.cmd;

import com.thegoate.Goate;
import com.thegoate.expect.Expectation;
import com.thegoate.testng.TestNGEngineMethodDL;
import org.testng.annotations.Test;

public class CommandTest extends TestNGEngineMethodDL {

    @Test(groups = {"unit"})
    public void kubectl(){
        Goate cmd = new Command("kubectl").arg("version").execute();

        expect(Expectation.build().actual(cmd.get("output")).isEqualTo("Client Version: version.Info{Major:\"1\", Minor:\"15\", GitVersion:\"v1.15.4\", GitCommit:\"67d2fcf276fcd9cf743ad4be9a9ef5828adc082f\", GitTreeState:\"clean\", BuildDate:\"2019-09-18T14:51:13Z\", GoVersion:\"go1.12.9\", Compiler:\"gc\", Platform:\"windows/amd64\"}\n" +
                "Server Version: version.Info{Major:\"1\", Minor:\"15\", GitVersion:\"v1.15.12\", GitCommit:\"e2a822d9f3c2fdb5c9bfbe64313cf9f657f0a725\", GitTreeState:\"clean\", BuildDate:\"2020-05-06T05:09:48Z\", GoVersion:\"go1.12.17\", Compiler:\"gc\", Platform:\"linux/amd64\"}\n"));
    }

    @Test(groups = {"unit"})
    public void noCommand(){
        Goate cmd = new Command("").execute();

        expect(Expectation.build().actual(cmd.get("output")).isEqualTo("no command defined"));
    }

    @Test(groups = {"unit"})
    public void badCommand(){
        Goate cmd = new Command("booger").execute();

        expect(Expectation.build().actual(cmd.get("output")).isEqualTo("Cannot run program \"booger\": CreateProcess error=2, The system cannot find the file specified"));
    }
}
