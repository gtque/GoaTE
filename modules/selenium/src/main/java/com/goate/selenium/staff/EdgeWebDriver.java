package com.goate.selenium.staff;

import com.goate.selenium.annotations.Driver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Returns a new edge web driver.
 * Created by Eric Angeli on 6/28/2017.
 */
@Driver(type = "edge")
public class EdgeWebDriver extends GoateDriver {

    @Override
    protected DesiredCapabilities loadCapabilities() {
        return DesiredCapabilities.edge();
    }

    @Override
    public WebDriver build() {
        return new EdgeDriver(dc);
    }
}
