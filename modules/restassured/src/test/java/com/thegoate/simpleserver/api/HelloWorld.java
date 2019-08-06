/*
 * Copyright (c) 2017. Eric Angeli
 *
 *  Permission is hereby granted, free of charge,
 *  to any person obtaining a copy of this software
 *  and associated documentation files (the "Software"),
 *  to deal in the Software without restriction,
 *  including without limitation the rights to use, copy,
 *  modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit
 *  persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission
 *  notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 *  AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 */
package com.thegoate.simpleserver.api;

import com.thegoate.simpleserver.pojo.SimpleName;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

/**
 * Created by Eric Angeli on 11/30/2018.
 */
@RestController
@RequestMapping("/hello")
public class HelloWorld {

    @PutMapping("/world")
    public String putWorld(){
        return getWorld();
    }
    @DeleteMapping("/world")
    public String deleteWorld(){
        return getWorld();
    }
    @GetMapping("/world")
    public String getWorld(){
        return "{\"greeting\":\"hello\"}";
    }

    @PutMapping("/auth")
    public String putWorld(@RequestHeader("Authorization") String auth) {
        return getWorld(auth);
    }

    @DeleteMapping("/auth")
    public String deleteWorld(@RequestHeader("Authorization") String auth) {
        return getWorld(auth);
    }

    @GetMapping("/auth")
    public String getWorld(@RequestHeader("Authorization") String auth){
        String response = "{\"greeting\":\"hello\"}";
        if(auth.contains("Basic")){
            String decoded = new String(Base64.getDecoder().decode(auth.replace("Basic ", "").getBytes()));
            try {
                String[] value = decoded.split(":");
                response = "{\"user\":\"" + value[0] + "\", \"password\":\"" + value[1] + "\"}";
            } catch (Exception e){
                e.printStackTrace();
                response = "{\"error\":\""+e.getMessage()+"\",\"content\":\""+decoded+"\"}";
            }
        }
        return response;
    }

    @PostMapping("/world")
    public String postWorld(@RequestBody SimpleName name){
        return "{\"greeting\":\"hello "+name.getName()+"\"}";
    }
}
