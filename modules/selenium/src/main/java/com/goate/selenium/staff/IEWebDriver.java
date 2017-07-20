package com.goate.selenium.staff;

import com.goate.selenium.annotations.Driver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Returns a new ie web driver.
 * Created by Eric Angeli on 6/28/2017.
 */
@Driver(type = "ie")
public class IEWebDriver extends GoateDriver {

    @Override
    protected DesiredCapabilities loadCapabilities() {
        return DesiredCapabilities.internetExplorer();
    }

    @Override
    public WebDriver build() {
        return new InternetExplorerDriver(dc);
    }
}
