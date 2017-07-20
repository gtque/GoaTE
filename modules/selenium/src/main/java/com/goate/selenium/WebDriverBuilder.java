package com.goate.selenium;

import com.goate.selenium.annotations.Driver;
import com.goate.selenium.staff.GoateDriver;
import com.goate.selenium.staff.RemoteWebDriver;
import com.thegoate.Goate;
import com.thegoate.annotations.AnnotationFactory;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.InvocationTargetException;

/**
 * Builds and returns a web driver of the given type.
 * Created by Eric Angeli on 6/28/2017.
 */
public class WebDriverBuilder {
    BleatBox LOG = BleatFactory.getLogger(getClass());
    String browser = "";
    String remote = "";

    Goate dc = new Goate();

    public WebDriverBuilder(String browser){
        this.browser = browser;
    }

    public WebDriverBuilder addCapability(String name, Object value){
        dc.put(name, value);
        return this;
    }

    public WebDriver build(){
        return build(dc);
    }

    public GoateDriver buildGoateDriver() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        AnnotationFactory af = new AnnotationFactory();
        return (GoateDriver) af.annotatedWith(Driver.class).find(browser).using("type").build();
    }

    public WebDriver build(Goate data){
        GoateDriver driver = null;
        try {
            if(browser.startsWith("remote:")){
                remote = browser.substring("remote:".length());
                browser = "remote";
            }
            driver = buildGoateDriver();
            if(!remote.isEmpty()){
                ((RemoteWebDriver)driver).remote(remote);
            }
            for(String name:data.keys()){
                driver.addCapability(name,data.get(name));
            }
        }catch(Exception e){
            LOG.error("Failed to load the webdriver for: " + browser + "\n" + e.getMessage(), e);
        }
        return driver!=null?driver.build():null;
    }
}
