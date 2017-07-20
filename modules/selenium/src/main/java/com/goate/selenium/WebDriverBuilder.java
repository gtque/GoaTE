package com.goate.selenium;

import com.goate.selenium.annotations.Driver;
import com.goate.selenium.staff.GoateDriver;
import com.thegoate.Goate;
import com.thegoate.annotations.AnnotationFactory;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import org.openqa.selenium.WebDriver;

/**
 * Builds and returns a web driver of the given type.
 * Created by Eric Angeli on 6/28/2017.
 */
public class WebDriverBuilder {
    BleatBox LOG = BleatFactory.getLogger(getClass());
    String browser = "";

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

    public WebDriver build(Goate data){
        AnnotationFactory af = new AnnotationFactory();
        GoateDriver driver = null;
        try {
            driver = (GoateDriver) af.annotatedWith(Driver.class).find(browser).using("type").build();
            for(String name:data.keys()){
                driver.addCapability(name,data.get(name));
            }
        }catch(Exception e){
            LOG.error("Failed to load the webdriver for: " + browser + "\n" + e.getMessage(), e);
        }
        return driver!=null?driver.build():null;
    }
}
