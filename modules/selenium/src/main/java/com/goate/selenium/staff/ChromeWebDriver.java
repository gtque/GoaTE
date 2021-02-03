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
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Returns a new chrome web driver.
 * Created by Eric Angeli on 6/28/2017.
 */
@Driver(type = "chrome")
public class ChromeWebDriver extends GoateDriver {

    @Override
    protected MutableCapabilities loadCapabilities() {
        return new ChromeOptions();
    }

    @Override
    public GoateDriver addCapability(String name, Object value){
        if(dc!=null){
            if(name.startsWith("args")){
                ((ChromeOptions)dc).addArguments(""+value);
            } else {
                super.addCapability(name, value);
            }
        }else{
            LOG.warn("Capabilities have not been loaded for the driver.");
        }
        return this;
    }

    @Override
    public WebDriver build() {
        return new ChromeDriver((ChromeOptions)dc);
    }
}
