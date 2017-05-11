package com.thegoate.testng;

import com.thegoate.Goate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.xml.XmlTest;

import java.lang.reflect.Method;


/**
 * The base class for writing TestNG classes.
 * Created by Eric Angeli on 5/11/2017.
 */
public abstract class TestNGEngine implements ITest, TestNG{
    final Logger LOG = LoggerFactory.getLogger(getClass());
    protected Goate data = null;
    protected Goate runData;
    protected Goate constantData;
    protected int runNumber = 0;
    protected String scenario = "";
    protected String methodName = "";
    protected boolean includeClassMethodInName = true;
    ITestContext testContext = null;
    XmlTest xt = null;

    public static int number = 0;

    public TestNGEngine(){
        setData(null);
    }

    public TestNGEngine(Goate data){
        init(data);
    }

    @BeforeMethod(alwaysRun = true)
    public void startUp(Method method) {
        methodName = method.getName();
        String startMessage = "\n"+
                "***************************Starting Up***************************\n"+
                "*\t"+getTestName()+"\t*\n"+
                "*****************************************************************\n";
        LOG.debug(startMessage);
    }

    @AfterMethod(alwaysRun = true)
    public void finishUp(Method method) {
        String endMessage = "\n"+
                "*****************************************************************\n"+
                "*\t"+getTestName()+"\t*\n"+
                "***************************Finished Up***************************\n";
        LOG.debug(endMessage);
    }
    protected void init(Goate data){
        setData(data);
        setScenario(get("Scenario","empty::",String.class));
        bumpRunNumber();
    }

    @Override
    public String getTestName() {
        StringBuilder name = new StringBuilder("");
        if(includeClassMethodInName){
            name.append(getClass().getCanonicalName()+":"+methodName+":");
        }
        name.append(scenario);
        name.append("("+runNumber+")");
        return name.toString();
    }

    @Override
    @DataProvider(name = "dataLoader")
    public Object[][] dataLoader(ITestContext context) throws Exception {
        number = 0;//resets the count, assume TestNG loads all the runs before processing the next class.
        this.testContext = context;
        xt = context.getCurrentXmlTest();
        initDataLoaders();
        return TestNGRunFactory.loadRuns(getRunDataLoader(), getConstantDataLoader());
    }

    protected void initDataLoaders(){
        runData = new Goate();
        constantData = new Goate();
        defineDataLoaders();
    }

    @Override
    public void defineDataLoaders(Goate runData, Goate constantData) {
        this.runData = runData;
        this.constantData = constantData;
    }

    @Override
    public Goate getRunDataLoader() {
        return this.runData;
    }

    @Override
    public Goate getConstantDataLoader() {
        return this.constantData;
    }

    @Override
    public Goate getData() {
        return this.data;
    }

    @Override
    public void setData(Goate data) {
        if(data==null){
            data = new Goate();
        }
        this.data = data;
    }

    @Override
    public void setRunNumber(int number) {
        runNumber = number;
    }

    @Override
    public void bumpRunNumber() {
        number++;
        setRunNumber(number);
    }

    @Override
    public int getRunNumber() {
        return runNumber;
    }

    @Override
    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    @Override
    public String getScenario() {
        return scenario;
    }

    @Override
    public Object get(String key) {
        return get(key, null);
    }

    @Override
    public Object get(String key, Object def) {
        return get(key, def, Object.class);
    }

    @Override
    public <T> T get(String key, Object def, Class<T> type) {
        return data.get(key, def, true, type);
    }

    @Override
    public TestNG put(String key, Object val) {
        data.put(key, val);
        return this;
    }
}
