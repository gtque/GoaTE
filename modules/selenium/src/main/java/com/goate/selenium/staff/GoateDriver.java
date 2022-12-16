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
package com.goate.selenium.staff;

import com.goate.selenium.annotations.Driver;
import com.thegoate.Goate;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.utils.GoateUtils;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;

/**
 * Base class for defining a driver builder object.
 * <br>
 * The paths are relative to the resource directory.<br>
 * By default they are looked for in webdrivers/{browser}/{os}/[architecture]<br>
 * The location may also be set in an eut/{eut}.properties file by setting selenium.{browser}.path<br>
 * The path should be relative to the resources directory. The driver(s) still need to be in the correct {os}/[architecture] folder.<br>
 * supported OSs and the os folder name to use:
 * <ul>
 *     <li>windows</li>
 *     <li>linux</li>
 *     <li>macos</li>
 *     <li>android</li>
 *     <li>ios</li>
 * </ul>
 * Created by Eric Angeli on 6/28/2017.
 */
public abstract class GoateDriver {
    BleatBox LOG = BleatFactory.getLogger(getClass());
    enum OS{
        WIN32("windows/32", true),WIN64("windows/64", true),LIN32("linux/32", false),LIN64("linux/64", false),ANDROID("android/64", false),IOS("ios/64", false),MACOS("macos/64", false);
        String path;
        boolean exe;

        OS(String path, boolean exe){
            this.path = path;
            this.exe = exe;
        }
        public String path(){
            return path;
        }
        public String ext(String extension){
            return exe?extension:extension.replace(".exe","");
        }
    }
    OS os;
    MutableCapabilities dc;
    boolean driverInstalled = false;
    String driverPath = "";

    /**
     * if the driver exe does not end with driver.exe, over load the simple constructor and set it what it should be after the call to super().
     */
    String extension = "driver.exe";
    public GoateDriver(){
        os = detectOS();
        dc = loadCapabilities();
        setPathToDriver();
    }

    /**
     * Override this method to return the capabilities for the implemented web driver.
     * @return The desired capabilities for the given driver.
     */
    protected abstract MutableCapabilities loadCapabilities();

    public MutableCapabilities getDc(){
        return dc;
    }

    /**
     * Override this method and return an instance of the webdriver.<br>
     * When instantiating, make sure to set the desired capabilities.
     * @return The new web driver.
     */
    public abstract WebDriver build();

    public GoateDriver addCapability(String name, Object value){
        if(dc!=null){
            dc.setCapability(name, value);
        }else{
            LOG.warn("Capabilities have not been loaded for the driver.");
        }
        return this;
    }

    protected OS detectOS(){
        OS type = null;
        String sys = System.getProperty("os.name");
        String architecture = System.getProperty("os.arch", System.getenv("ProgramFiles(x86)"));
        if(sys.contains("Windows")){
            if(architecture==null){
                type = OS.WIN32;
            }else{
                if(architecture.contains("32")){
                    type = OS.WIN32;
                }else{
                    type = OS.WIN64;
                }
            }
        } else if(sys.contains("Linux")){
            if(architecture==null){
                type = OS.LIN32;
            }else{
                if(architecture.contains("32")){
                    type = OS.LIN32;
                }else{
                    type = OS.LIN64;
                }
            }
        } else if(sys.contains("Mac")){
            type = OS.MACOS;//assumes only one architecture type.
        }
        return type;
    }

    protected void setPathToDriver(){
        String browser = "";
        String path = "";
        Driver d = getClass().getAnnotation(Driver.class);
        if(d!=null) {
            browser = d.property().isEmpty()?d.type():d.property();
            driverPath = new Goate().get("driverpath","eut::selenium."+browser+".path,empty::",String.class);
            if(driverPath.isEmpty()){
                driverPath = "webdrivers/"+browser;
            }
            path = driverPath +"/"+os.path() + "/" + browser + os.ext(extension);
            path = GoateUtils.getFilePath(path);
            if (!browser.isEmpty()) {
                System.setProperty("webdriver." + browser + ".driver", path);
            }
        }
    }
}
