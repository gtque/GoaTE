package com.thegoate.spring.boot;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Writes to the database....
 * Created by eangeli on 8/3/2016.
 */
@RestController
public class HelloDB {
    @RequestMapping(value = "/db", method = POST)
    public String insertIntoDB(@RequestBody SimplePojo data){
        String result = "";

        return result;
    }

}
