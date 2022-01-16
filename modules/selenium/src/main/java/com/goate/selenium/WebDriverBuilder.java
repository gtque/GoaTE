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
        init(browser);
    }

	public WebDriverBuilder(Browser browser) {
		init(browser.type());
	}

	private void init(String browser) {
		this.browser = browser;
	}
	public WebDriverBuilder isRemote(boolean remote) {
		if(remote) {
			if(!browser.startsWith("remote:")){
				browser = "remote:" + browser;
			}
		} else {
			browser = browser.replace("remote:", "");
		}
		return this;
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
