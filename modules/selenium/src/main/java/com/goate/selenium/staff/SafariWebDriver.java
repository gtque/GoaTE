package com.goate.selenium.staff;

import com.goate.selenium.annotations.Driver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;

/**
 * Returns a new safari web driver.
 * Created by Eric Angeli on 6/28/2017.
 */
@Driver(type = "safari")
public class SafariWebDriver extends GoateDriver {

    @Override
    protected DesiredCapabilities loadCapabilities() {
        return DesiredCapabilities.safari();
    }

    @Override
    public WebDriver build() {
        return new SafariDriver(dc);
    }
}
