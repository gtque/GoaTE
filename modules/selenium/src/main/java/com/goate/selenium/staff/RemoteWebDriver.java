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

import com.goate.selenium.WebDriverBuilder;
import com.goate.selenium.annotations.Driver;
import com.thegoate.Goate;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Returns a new chrome web driver.
 * Created by Eric Angeli on 6/28/2017.
 */
@Driver(type = "remote")
public class RemoteWebDriver extends GoateDriver {

    String remote = "";
    GoateDriver remoteDriver = null;

    @Override
    protected MutableCapabilities loadCapabilities() {
        if(remoteDriver==null){
            try {
                remoteDriver = new WebDriverBuilder(remote).buildGoateDriver();
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
               LOG.error("problem initializing remote driver capabilities:"+e.getMessage(), e);
            }
        }
        return remoteDriver!=null?remoteDriver.getDc():null;
    }

    @Override
    public WebDriver build() {
        WebDriver driver = null;
        try{
            driver = new org.openqa.selenium.remote.RemoteWebDriver(new URL(new Goate().get("remoteURL","eut::selenium.remote.url",String.class)),dc);
        }catch (MalformedURLException e){
            LOG.error("problem setting up remote web driver: " + e.getMessage(), e);
        }
        return driver;
    }

    public RemoteWebDriver remote(String remote){
        this.remote = remote;
        return this;
    }
}
