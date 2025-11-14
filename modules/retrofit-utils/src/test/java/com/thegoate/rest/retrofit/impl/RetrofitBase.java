package com.thegoate.rest.retrofit.impl;

import com.google.gson.GsonBuilder;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static com.thegoate.dsl.words.EutConfigDSL.eut;

public class RetrofitBase {
    protected final static Retrofit getClient(RetrofitBuilder clientBuilder) {
        return getClient(clientBuilder, clientBuilder.getLoggingInterceptor());
    }

    protected final static Retrofit getClient(RetrofitBuilder clientBuilder, Interceptor loggingInterceptor) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        var timeout = Duration.of(getTimeout(), ChronoUnit.SECONDS);
        httpClient.callTimeout(timeout)
                .connectTimeout(timeout)
                .readTimeout(timeout);

        ignoreSSL(httpClient);

        //inject auth header
        if (clientBuilder.headers() != null) {
            httpClient.addInterceptor(clientBuilder.headers());
        }

        //configure logging
        httpClient.addInterceptor(loggingInterceptor);

        //configure cookie handling
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        httpClient.cookieJar(new JavaNetCookieJar(cookieManager));
        httpClient.followRedirects(true);

        //define gson converter.
        GsonBuilder gson = new GsonBuilder();
        if(clientBuilder.doIncludeNulls()) {
            gson.serializeNulls();
        }
        if(clientBuilder.getGsonTypeAdapters()!=null){
            for(Map.Entry<Type,Object> typeAdapter:clientBuilder.getGsonTypeAdapters().entrySet()) {
                gson.registerTypeAdapter(typeAdapter.getKey(), typeAdapter.getValue());
            }
        }

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(clientBuilder.url())
                .addConverterFactory(ScalarsConverterFactory.create());

        if (clientBuilder.getFactories() != null){
            for (Converter.Factory factory : clientBuilder.getFactories()) {
                builder.addConverterFactory(factory);
            }
        }
        return builder.addConverterFactory(GsonConverterFactory.create(gson.create())).client(httpClient.build()).build();
    }

    private static OkHttpClient.Builder ignoreSSL(OkHttpClient.Builder builder) {
        try {
            TrustManager[] trustManager = new TrustManager[] {
                    new X509TrustManager() {

                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[] {};
                        }
                    }
            };
            var sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManager, new SecureRandom());
            var sslSocketFactory = sslContext.getSocketFactory();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustManager[0]);
            builder.hostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return builder;
    }

    private static Long getTimeout() {
        String timeout = eut("retrofit.timeout");
        if (timeout == null) {
            timeout = "120";
        }
        return Long.parseLong(timeout);
    }

    public static class ContentTypes {

        public String formUrlEncoded() {
            return "application/x-www-form-urlencoded";
        }

        public String applicationJson() {
            return "application/json";
        }

        public String textHtml() {
            return "text/html";
        }

        public String cspReport() {
            return "application/csp-report";
        }
    }

    public static class CacheControls {

        public String noCache() {
            return "no-cache";
        }

    }

}
