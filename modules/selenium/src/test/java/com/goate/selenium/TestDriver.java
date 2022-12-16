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

    public TestDriver() {
        super();
    }

    @Factory(dataProvider = "dataLoader")
    public TestDriver(Goate data) {
        super(data);
    }

    @Test(groups = {"webui"})
    public void webdriver() {
        WebDriver driver = (WebDriver) get("browser", "webdriver::chrome");
        assertNotNull(driver);
        if (driver != null) {
            try {
                driver.close();
            } catch (Exception e) {
                LOG.warn("Problem closing browser: " + e.getMessage());
            } finally {
                try {
                    driver.quit();
                } catch (Exception e) {
                    LOG.warn("Problem quitting browser. It may still be open.\n" + e.getMessage());
                }
            }
        }
    }

    @Override
    public void defineDataLoaders() {
//        runData.put("dl##",new StaticDL().add("browser","webdriver::edge").add("Scenario","Open Edge."))
        runData.put("dl##", new StaticDL().add("browser", "webdriver::chrome").add("Scenario", "Open chrome."));
    }
}
