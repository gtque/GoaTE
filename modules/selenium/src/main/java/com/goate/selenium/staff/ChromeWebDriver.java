package com.goate.selenium.staff;

import com.goate.selenium.annotations.Driver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Returns a new chrome web driver.
 * Created by Eric Angeli on 6/28/2017.
 */
@Driver(type = "chrome")
public class ChromeWebDriver extends GoateDriver {

    @Override
    protected DesiredCapabilities loadCapabilities() {
        return DesiredCapabilities.chrome();
    }

    @Override
    public WebDriver build() {
        return new ChromeDriver(dc);
    }
}
