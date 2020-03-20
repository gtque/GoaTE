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

import static com.thegoate.dsl.words.DateCurrentDSL.date;
import static com.thegoate.utils.GoateUtils.sleep;
import static com.thegoate.utils.file.Copy.copy;
import static org.testng.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import com.thegoate.Goate;
import com.thegoate.data.StaticDL;
import com.thegoate.testng.TestNGEngine;

/**
 * Tests loading web drivers included in selenium-java package.
 * Created by Eric Angeli on 6/28/2017.
 */
public class TestDriver2 extends TestNGEngine {

    public TestDriver2() {
        super();
    }

    public TestDriver2(Goate data) {
        super(data);
    }

    @Test(groups = {"webui"})
    public void webdriver() {
        for(int i = 0; i<20; i++) {
            WebDriver driver = (WebDriver) get("browser", "webdriver::chrome,args.1==--incognito");
            if (driver != null) {
                driver.navigate().to("http://qa-qaoctopus101.wi.onedatascan.io/dst");
                WebDriverWait wait = new WebDriverWait(driver, 30000);
                wait.until(ExpectedConditions.elementToBeClickable(By.id("IDToken1")));
                WebElement id = driver.findElement(By.id("IDToken1"));
                id.clear();
                id.sendKeys("wi01");
                driver.findElement(By.id("IDToken2")).sendKeys("wi01");
                driver.findElement(By.id("submitButton")).click();
                sleep(10000);
                String screenShotName = "landing_r4_"+date("yyyyMMddHHmmss") + ".jpg";
                String reportPath = "";//((ExtentLogger) LOGGER).getReportDir();
                String screenshotPath = (reportPath.isEmpty()?"./temp":reportPath) + "/" + screenShotName;
                screenShot(screenshotPath, driver);
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
                    driver = null;
                    data.drop("browser");
                }
            }
        }
    }

    protected synchronized void screenShot(String fileName, WebDriver driver) {
        Thread.interrupted();
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        copy(scrFile).to(fileName);
    }

    @Override
    public void defineDataLoaders() {
//        runData.put("dl##",new StaticDL().add("browser","webdriver::edge").add("Scenario","Open Edge."))
        runData.put("dl##", new StaticDL().add("browser", "webdriver::chrome").add("Scenario", "Open chrome."));
    }
}
