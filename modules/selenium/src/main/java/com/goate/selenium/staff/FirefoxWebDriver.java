package com.goate.selenium.staff;

import com.goate.selenium.annotations.Driver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Returns a new firefox web driver.
 * Created by Eric Angeli on 6/28/2017.
 */
@Driver(type = "firefox", property = "gecko")
public class FirefoxWebDriver extends GoateDriver {

    @Override
    protected DesiredCapabilities loadCapabilities() {
        return DesiredCapabilities.firefox();
    }

    @Override
    public WebDriver build() {
        return new FirefoxDriver(dc);
    }
}
