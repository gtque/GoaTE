package com.thegoate.rest.retrofit.impl;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class HeaderInjector implements Interceptor {
    private Map<String, Supplier<String>> headers =  new ConcurrentHashMap<>();

    public HeaderInjector header(String header, Supplier<String> value) {
        headers.put(header, value);
        return this;

    }
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
        headers.forEach((header, value) -> requestBuilder.addHeader(header, value.get()));
        return chain.proceed(requestBuilder.build());
    }

}
