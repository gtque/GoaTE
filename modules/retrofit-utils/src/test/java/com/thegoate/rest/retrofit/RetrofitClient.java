package com.thegoate.rest.retrofit;

import com.thegoate.rest.retrofit.impl.RetrofitBase;
import com.thegoate.rest.retrofit.impl.RetrofitBuilder;

import java.time.Duration;

public class RetrofitClient extends RetrofitBase {

    /**
     * Builds the specified retrofit client
     * sets the content-type to json
     * sets the cache control to no cache
     * sets the Authorization header to the value from the token.
     * Allows you to override the default retrofit expected duration set in the properties file. `RETROFIT_EXPECTED_RESPONSE_DURATION_SECONDS`
     *
     * @param url String - the base URL for the Retrofit client
     * @param serviceType Class<T> - the class type of the Retrofit service to create
     * @param overrideExpectedDuration Duration - If null, retrofit client builder will default to the setting from the properties file.
     * @param <T> the type of the Retrofit service
     * @return T - an instance of the Retrofit service type
     */
    public static <T> T getService(String url, Duration overrideExpectedDuration, Class<T> serviceType,
                                   boolean includeNulls, boolean printBody) {
        RetrofitBuilder clientBuilder = RetrofitBuilder.build(url)
                .header("Content-Type", new RetrofitBase.ContentTypes()::applicationJson)
                .header("Cache-Control", new RetrofitBase.CacheControls()::noCache)
                .expectedDuration(overrideExpectedDuration);

        if (includeNulls) {
            clientBuilder.includeNulls();
        }
        if (!printBody) {
            clientBuilder.doNotPrintBody();
        }
        return getClient(clientBuilder).create(serviceType);
    }

}

