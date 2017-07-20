package com.goate.selenium.staff;

import com.goate.selenium.annotations.Driver;
import com.thegoate.Goate;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.utils.GoateUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

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
        WIN32("windows/32"),WIN64("windows/64"),LIN32("linux/32"),LIN64("linux/64"),ANDROID("android/64"),IOS("ios/64"),MACOS("macos/64");
        String path = "";
        OS(String path){
            this.path = path;
        }
        public String path(){
            return path;
        }
    }
    OS os;
    DesiredCapabilities dc;
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
    protected abstract DesiredCapabilities loadCapabilities();

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
            browser = d.type();
            driverPath = new Goate().get("driverpath","eut::selenium."+browser+".path,empty::",String.class);
            if(driverPath.isEmpty()){
                driverPath = "webdrivers/"+browser;
            }
            path = driverPath +"/"+os.path() + "/" + browser + extension;
            path = GoateUtils.getFilePath(path);
            if (!browser.isEmpty()) {
                System.setProperty("webdriver." + browser + ".driver", path);
            }
        }
    }
}
