package com.thegoate.rest.retrofit;

import com.thegoate.rest.retrofit.models.HelloWorldTestGreeting;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SimpleServerInterface {

    @GET("/hello/test/{name}/hello")
    Call<HelloWorldTestGreeting> helloGreeting(@Path(value = "name") String name);

    @GET("/hello/test/{name}/hello")
    Call<ResponseBody> helloGreetingRaw(@Path(value = "name") String name);
}
