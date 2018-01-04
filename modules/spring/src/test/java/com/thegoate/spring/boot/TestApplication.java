package com.thegoate.spring.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by eangeli on 8/3/2016.
 */
@SpringBootApplication
@EnableTransactionManagement
public class TestApplication {

    public static void main(String[] args) {
        System.out.println("******************************************");
        System.out.println("          STARTING UP                     ");
        System.out.println("******************************************");
        ApplicationContext ctx = SpringApplication.run(TestApplication.class, args);
    }
}
