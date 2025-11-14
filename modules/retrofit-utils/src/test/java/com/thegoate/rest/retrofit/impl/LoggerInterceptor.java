package com.thegoate.rest.retrofit.impl;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;


public class LoggerInterceptor implements Interceptor {
    private Duration expectedResponseDuration;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final Charset UTF8 = StandardCharsets.UTF_8;
    private volatile Set<String> headersToRedact;

    private boolean printBody = true;

    public LoggerInterceptor() {
        this.headersToRedact = Collections.emptySet();
    }

    public LoggerInterceptor doNotPrintBody() {
        printBody = false;
        return this;
    }

    public LoggerInterceptor setExpectedResponseDuration(Duration expectedResponseDuration) {
        this.expectedResponseDuration = expectedResponseDuration;
        return this;
    }

    public void redactHeader(String name) {
        Set<String> newHeadersToRedact = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        newHeadersToRedact.addAll(this.headersToRedact);
        newHeadersToRedact.add(name);
        this.headersToRedact = newHeadersToRedact;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        StringBuilder sb = new StringBuilder();

        Request request = chain.request();
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        String requestStartMessage = "--> "
                + request.method()
                + ' ' + request.url()
                + (connection != null ? " " + connection.protocol() : "");
        if (hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        sb.append(requestStartMessage + "\n");

        if (hasRequestBody) {
            // Request body headers are only present when installed as a network interceptor. Force
            // them to be included (when available) so there values are known.
            if (requestBody.contentType() != null) {
                sb.append("Content-Type: " + requestBody.contentType() + "\n");
            }
            if (requestBody.contentLength() != -1) {
                sb.append("Content-Length: " + requestBody.contentLength() + "\n");
            }
        }

        Headers headers = request.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            String name = headers.name(i);
            // Skip headers from the request body as they are explicitly logged above.
            if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                logHeader(sb, headers, i);
            }
        }

        if ( !printBody ) {
            sb.append("<-- END HTTP (skipped printing body)" + "\n");
        } else {
            if (!hasRequestBody) {
                sb.append("--> END ").append(request.method()).append("\n");
            } else if (bodyHasUnknownEncoding(request.headers())) {
                sb.append("--> END ").append(request.method()).append(" (encoded body omitted)").append("\n");
            } else if (requestBody.isDuplex()) {
                sb.append("--> END ").append(request.method()).append(" (duplex request body omitted)");
            } else if (excludeUrlFromBodyLogging(request.url().toString())) {
                sb.append("--> END ").append(request.method()).append(" (unprintable body omitted)");
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                sb.append("\n");
                if (isPlaintext(buffer)) {
                    sb.append(buffer.readString(charset)).append("\n");
                    sb.append("--> END ").append(request.method()).append(" (").append(requestBody.contentLength()).append("-byte body)").append("\n");
                } else {
                    sb.append("--> END ").append(request.method()).append(" (binary ").append(requestBody.contentLength()).append("-byte body omitted)").append("\n");
                }
            }

        }
        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            sb.append("<-- HTTP FAILED: ").append(e).append("\n");
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        sb.append("<-- ").append(response.code()).append(response.message().isEmpty() ? "" : ' ' + response.message()).append(' ').append(response.request().url()).append(" (").append(tookMs).append("ms)").append("\n");

        for (int i = 0, count = headers.size(); i < count; i++) {
            logHeader(sb, headers, i);
        }

        if ( !printBody ) {
            sb.append("<-- END HTTP (skipped printing body)" + "\n");
        } else {
            if (!HttpHeaders.hasBody(response)) {
                sb.append("<-- END HTTP" + "\n");
            } else if (bodyHasUnknownEncoding(response.headers())) {
                sb.append("<-- END HTTP (encoded body omitted)" + "\n");
            } else if (excludeUrlFromBodyLogging(request.url().toString())) {
                sb.append("<-- END HTTP (excluded url omitted)" + "\n");
            } else {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.getBuffer();

                Long gzippedLength = null;
                if ("gzip".equalsIgnoreCase(headers.get("Content-Encoding"))) {
                    gzippedLength = buffer.size();
                    try (GzipSource gzippedResponseBody = new GzipSource(buffer.clone())) {
                        buffer = new Buffer();
                        buffer.writeAll(gzippedResponseBody);
                    }
                }

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                if (!isPlaintext(buffer)) {
                    sb.append("\n");
                    sb.append("<-- END HTTP (binary ").append(buffer.size()).append("-byte body omitted)").append("\n");
                    return response;
                }

                if (contentLength != 0) {
                    sb.append("\n");
                    sb.append(buffer.clone().readString(charset)).append("\n");
                }

                if (gzippedLength != null) {
                    sb.append("<-- END HTTP (").append(buffer.size()).append("-byte, ").append(gzippedLength).append("-gzipped-byte body)").append("\n");
                } else {
                    sb.append("<-- END HTTP (").append(buffer.size()).append("-byte body)").append("\n");
                }
            }
        }

        logger.info(sb.toString());
        return response;
    }

    private void logHeader(StringBuilder sb, Headers headers, int i) {
        String value = headersToRedact.contains(headers.name(i)) ? "██" : headers.value(i);
        sb.append(headers.name(i)).append(": ").append(value).append("\n");
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private static boolean bodyHasUnknownEncoding(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null
                && !contentEncoding.equalsIgnoreCase("identity")
                && !contentEncoding.equalsIgnoreCase("gzip");
    }

    private static boolean excludeUrlFromBodyLogging(String url) {
        return url.contains("download");
    }

}
