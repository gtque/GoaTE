package com.goate.selenium.staff;

import com.goate.selenium.annotations.Driver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Returns a new opera web driver.
 * Created by Eric Angeli on 6/28/2017.
 */
@Driver(type = "opera")
public class OperaWebDriver extends GoateDriver {

    @Override
    protected DesiredCapabilities loadCapabilities() {
        return DesiredCapabilities.operaBlink();
    }

    @Override
    public WebDriver build() {
        return new OperaDriver(dc);
    }
}
