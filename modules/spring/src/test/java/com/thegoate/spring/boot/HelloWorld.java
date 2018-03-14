package com.thegoate.spring.boot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by eangeli on 8/3/2016.
 */
@RestController
public class HelloWorld {
    @RequestMapping("/")
    public String hello(){
        return "world";
    }
}
