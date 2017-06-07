package com.thegoate.testng;

import com.thegoate.Goate;
import com.thegoate.data.DataLoader;
import org.testng.ITestContext;

/**
 * The interface for TestNG based test classes.
 * Created by Eric Angeli on 5/11/2017.
 */
public interface TestNG {
    Object[][] dataLoader(ITestContext context) throws Exception;
    void defineDataLoaders();
    void defineDataLoaders(Goate runData, Goate constantData);
    Goate getRunDataLoader();
    Goate getConstantDataLoader();
    Goate getData();
    void setData(Goate data);
    void setRunNumber(int number);
    void bumpRunNumber();
    int getRunNumber();
    void setScenario(String scenario);
    String getScenario();
    Object get(String key);
    <T>T get(String key, Class<T> type);
    Object get(String key, Object def);
    <T>T get(String key, Object def, Class<T> type);
    TestNG put(String key, Object val);
}
