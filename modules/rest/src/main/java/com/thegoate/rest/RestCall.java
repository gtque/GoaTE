package com.thegoate.rest;

import com.thegoate.Goate;
import com.thegoate.annotations.AnnotationFactory;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.rest.staff.ApiEmployee;
import com.thegoate.staff.GoateJob;

import java.lang.reflect.InvocationTargetException;

/**
 * Wrapper for programmatically using the REST framework in GoaTE.
 * Created by Eric Angeli on 11/8/2018.
 */
public class RestCall {
    BleatBox LOG = BleatFactory.getLogger(getClass());

    Goate definition = new Goate();

    public RestCall baseURL(String url){
        definition.put("base url", url);
        return this;
    }

    public RestCall security(String security){
        definition.put("security", security);
        return this;
    }

    public RestCall timeout(int timeout){
        definition.put("rest.timeout", timeout);
        return this;
    }

    public RestCall customParam(String key, String param){
        definition.put("custom params.##", key+":="+param);
        return this;
    }

    public RestCall multipart(String key, String param){
        definition.put("multipart##", key+":="+param);
        return this;
    }

    public RestCall formParam(String key, String param){
        definition.put("form params.##", key+":="+param);
        return this;
    }

    public RestCall pathParam(String key, String param){
        definition.put("path params.##", key+":="+param);
        return this;
    }

    public RestCall queryParam(String key, String param){
        definition.put("query params.##", key+":="+param);
        return this;
    }

    public RestCall urlParam(String key, String param){
        definition.put("url params.##", key+":="+param);
        return this;
    }

    public RestCall header(String key, String param){
        definition.put("headers.##", key+":="+param);
        return this;
    }

    public RestCall body(Object body){
        definition.put("body", body);
        return this;
    }

    public Object get(String endpoint){
        definition.put("endpoint", endpoint);
        return execute("get");
    }
    public Object put(String endpoint){
        definition.put("endpoint", endpoint);
        return execute("put");
    }
    public Object post(String endpoint){
        definition.put("endpoint", endpoint);
        return execute("post");
    }
    public Object patch(String endpoint){
        definition.put("endpoint", endpoint);
        return execute("patch");
    }
    public Object delete(String endpoint){
        definition.put("endpoint", endpoint);
        return execute("delete");
    }
    private Object execute(String method){
        definition.put("method", method);
        AnnotationFactory af = new AnnotationFactory();
        Object result = null;
        try {
            ApiEmployee worker = (ApiEmployee) af.annotatedWith(GoateJob.class).find(method).using("jobs").build();
            worker.init(definition);
            result = worker.work();
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            LOG.error("Barn API Init", "problem finding something to execute a " + method +"\nmake sure you have an implementation library included.", e);
        }
        return result;
    }

}
