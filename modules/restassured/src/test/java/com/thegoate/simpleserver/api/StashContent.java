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

import com.thegoate.simpleserver.pojo.ErrorMessage;
import com.thegoate.simpleserver.pojo.Message;
import com.thegoate.simpleserver.pojo.SimpleContent;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Eric Angeli on 11/30/2018.
 */
@RestController
@RequestMapping("/stash")
public class StashContent {

    Map<String, SimpleContent> data = new ConcurrentHashMap<>();

    @GetMapping("/message/{id}")
    public Message getMessage(@PathVariable String id, HttpServletResponse response){
        if(data.containsKey(id)){
            return data.get(id);
        }
        response.setStatus(404);
        ErrorMessage error = new ErrorMessage();
        error.setError("id not found");
        return error;
    }

    @PostMapping("/message")
    public Message postMessage(@RequestBody SimpleContent message){
        message.setId(""+System.nanoTime());
        message.setDate(LocalDate.now());
        data.put(message.getId(),message);
        return message;
    }

    @PutMapping("/message/{id}")
    public Message putMessage(@PathVariable String id, @RequestBody SimpleContent message, HttpServletResponse response){
        if(data.containsKey(id)){
            data.get(id).setMessage(message.getMessage());
            return data.get(id);
        }
        response.setStatus(404);
        ErrorMessage error = new ErrorMessage();
        error.setError("id not found");
        return error;
    }

    @DeleteMapping("/message/{id}")
    public Message deleteMessage(@PathVariable String id, HttpServletResponse response){
        Message message = null;
        if(data.containsKey(id)){
            //data.remove(id);
            response.setStatus(204);
        } else {
            ErrorMessage error = new ErrorMessage();
            error.setError("id not found");
            message = error;
            response.setStatus(404);
        }
        return message;
    }
}
