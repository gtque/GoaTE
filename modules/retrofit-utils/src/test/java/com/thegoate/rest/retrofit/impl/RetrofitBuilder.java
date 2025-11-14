package com.thegoate.rest.retrofit.impl;

import retrofit2.Converter;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.thegoate.dsl.words.EutConfigDSL.eut;

public class RetrofitBuilder {

    private String url;
    private HeaderInjector headers;
    private List<Converter.Factory> factories;

    private Map<Type, Object> gsonTypeAdapters;

    private boolean includeNulls = false;

    private Duration expectedDuration;

    private RetrofitBuilder(String url) {
        this.url = url;
    }

    private boolean printBody = true;

    public static RetrofitBuilder build(String url) {
        if (url == null || url.isEmpty()) {
            throw new RetrofitConfigurationException("url must be set and not null");
        }
        return new RetrofitBuilder(url);
    }

    public RetrofitBuilder header(String header, Supplier<String> value) {
        if (headers == null) {
            headers = new HeaderInjector();
        }
        headers.header(header, value);
        return this;
    }

    public RetrofitBuilder includeNulls() {
        this.includeNulls = true;
        return this;
    }

    public RetrofitBuilder dropNulls() {
        this.includeNulls = false;
        return this;
    }

    public boolean doIncludeNulls() {
        return this.includeNulls;
    }

    public RetrofitBuilder expectedDuration(Duration duration) {
        this.expectedDuration = duration;
        return this;
    }

    public String url() {
        return this.url;
    }

    public HeaderInjector headers() {
        return this.headers;
    }

    /**
     * If expectedDuration is set, use that value,
     * otherwise check if the RETROFIT_EXPECTED_RESPONSE_DURATION_SECONDS property has been set.
     * If it has, use that value,
     * otherwise default to 450 seconds.
     *
     * @return
     */
    public Duration expectedResponseDuration() {
        Duration duration = this.expectedDuration;
        if (duration == null) {
            String configuredDuration = eut("retrofit.expected.response.duration.seconds");
            if (configuredDuration == null) {
                configuredDuration = "450";
            }
            duration = Duration.ofSeconds(Long.parseLong(configuredDuration));
        }
        return duration;
    }

    public RetrofitBuilder doNotPrintBody() {
        printBody = false;
        return this;
    }

    public LoggerInterceptor getLoggingInterceptor() {
        LoggerInterceptor interceptor = new LoggerInterceptor().setExpectedResponseDuration(this.expectedResponseDuration());
        if (!printBody) {
            interceptor.doNotPrintBody();
        }
        return interceptor;
    }

    public List<Converter.Factory> getFactories() {
        return factories;
    }

    public RetrofitBuilder addFactory(Converter.Factory factory) {
        if (this.factories == null) {
            this.factories = new ArrayList<>();
        }
        this.factories.add(factory);
        return this;
    }

    public Map<Type, Object> getGsonTypeAdapters() {
        return this.gsonTypeAdapters;
    }

    public RetrofitBuilder addTypeAdapter(Type type, Object typeAdapter) {
        if (gsonTypeAdapters == null) {
            gsonTypeAdapters = new HashMap<>();
        }
        gsonTypeAdapters.put(type, typeAdapter);
        return this;
    }

}
