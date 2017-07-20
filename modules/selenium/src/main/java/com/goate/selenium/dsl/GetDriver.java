package com.goate.selenium.dsl;

import com.goate.selenium.WebDriverBuilder;
import com.thegoate.Goate;
import com.thegoate.dsl.DSL;
import com.thegoate.dsl.GoateDSL;
import org.openqa.selenium.WebDriver;

/**
 * Builds and sets the selenium web driver.
 * Created by Eric Angeli on 6/27/2017.
 */
@GoateDSL(word = "webdriver")
public class GetDriver extends DSL {
    public GetDriver(){
        super();
    }

    public GetDriver(Object value){
        super(value);
    }

    @Override
    public Object evaluate(Goate data) {
        String browser = ""+get(1,data);
        WebDriver driver = new WebDriverBuilder(browser).build();
        if(!key.isEmpty()){
            data.put(key,driver);
        }
        return driver;
    }
}
