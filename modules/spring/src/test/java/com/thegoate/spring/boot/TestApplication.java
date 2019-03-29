package com.thegoate.spring.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by eangeli on 8/3/2016.
 */
@SpringBootApplication//(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@EnableTransactionManagement
public class TestApplication {

    public static void main(String[] args) {
        System.out.println("******************************************");
        System.out.println("          STARTING UP                     ");
        System.out.println("******************************************");
        ApplicationContext ctx = SpringApplication.run(TestApplication.class, args);
    }
}
