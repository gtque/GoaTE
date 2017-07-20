package com.goate.selenium;

import com.thegoate.Goate;
import com.thegoate.data.StaticDL;
import com.thegoate.testng.TestNGEngine;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

/**
 * Tests loading web drivers included in selenium-java package.
 * Created by Eric Angeli on 6/28/2017.
 */
public class TestDriver extends TestNGEngine {

    public TestDriver(){
        super();
    }

    @Factory(dataProvider = "dataLoader")
    public TestDriver(Goate data){
        super(data);
    }

    @Test(groups = {"webui"})
    public void webdriver(){
        WebDriver driver = (WebDriver)get("browser","webdriver::chrome");
        assertNotNull(driver);
        if(driver!=null){
            try{
                driver.close();
            }catch (Exception e){
                LOG.warn("Problem closing browser: " + e.getMessage());
            }finally {
                try {
                    driver.quit();
                }catch(Exception e){
                    LOG.warn("Problem quitting browser. It may still be open.\n"+e.getMessage());
                }
            }
        }
    }

    @Override
    public void defineDataLoaders() {
        runData.put("dl##",new StaticDL().add("browser","webdriver::edge").add("Scenario","Open Edge."))
                .put("dl##",new StaticDL().add("browser","webdriver::chrome").add("Scenario","Open chrome."));
    }
}
