package com.thegoate.spring;

import com.thegoate.Goate;
import com.thegoate.data.StaticDL;
import com.thegoate.spring.boot.SimpleDAO;
import com.thegoate.spring.boot.SimplePojo;
import com.thegoate.spring.boot.TestApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created by Eric Angeli on 10/31/2017.
 */
@SpringBootTest(webEnvironment = RANDOM_PORT,classes = TestApplication.class)
@TestExecutionListeners(inheritListeners = false, listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
@Transactional
@EnableTransactionManagement
public class SimpleSpringTransactionalTest extends SpringTransactionalTestEngine {

    @Autowired
    SimpleDAO bot;

    public SimpleSpringTransactionalTest(){
        super();
    }

    //@Factory(dataProvider = "dataLoader")
    public SimpleSpringTransactionalTest(Goate data){
        super(data);
    }

    @Override
    public void defineDataLoaders(){
        super.defineDataLoaders();
        runData.put("run##", new StaticDL().add("Scenario", "A")).put("run##", new StaticDL().add("Scenario", "B"));
    }

    @Test(groups = {"spring"})
    public void test1(){
        assertNotNull(bot);
//        assertEquals(bot.countRows(), 0);
        SimplePojo pojo = new SimplePojo("howdy", "checkers");
        bot.insert(pojo);
        assertEquals(bot.countRows(), 1);
    }
}
