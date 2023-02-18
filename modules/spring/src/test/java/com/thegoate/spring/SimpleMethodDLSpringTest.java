package com.thegoate.spring;

import com.thegoate.Goate;
import com.thegoate.data.GoateDLP;
import com.thegoate.data.GoateProvider;
import com.thegoate.data.StaticDL;
import com.thegoate.expect.Expectation;
import com.thegoate.spring.boot.HelloAuto;
import com.thegoate.spring.boot.TestApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.testng.annotations.Test;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.testng.Assert.assertEquals;

/**
 * Created by Eric Angeli on 10/31/2017.
 */
@SpringBootTest(webEnvironment = RANDOM_PORT,classes = TestApplication.class)
@TestExecutionListeners(inheritListeners = false, listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
public class SimpleMethodDLSpringTest extends SpringTestEngine {

    @Autowired
    HelloAuto bot;

    public SimpleMethodDLSpringTest(){
        super();
    }

    public SimpleMethodDLSpringTest(Goate data){
        super(data);
    }

    @Override
    public void defineDataLoaders(){
        super.defineDataLoaders();
    }

    @GoateDLP(name="sample")
    public Goate[] dlp(){
        defineDataLoaders();
        Goate[] d = new Goate[2];
        d[0]=new Goate().put("run##", new StaticDL().add("Scenario", "A")).put("run##", new StaticDL().add("Scenario", "B"));
        return d;
    }

    @GoateProvider(name = "sample")
    @Test(groups = {"unit", "spring"}, dataProvider = "springMethodLoader")
    public void testMethod1(Goate d){
        expect(Expectation.build().actual(bot).isNull(false));
        assertEquals(bot.autobots(), "roll out");
    }
}
