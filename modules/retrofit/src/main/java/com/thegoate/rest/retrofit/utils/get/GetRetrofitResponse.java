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

package com.thegoate.rest.retrofit.utils.get;

import com.thegoate.Goate;
import com.thegoate.rest.RestResult;
import com.thegoate.statics.ResetStatic;
import com.thegoate.statics.ResetStatics;
import com.thegoate.utils.get.GetTool;
import com.thegoate.utils.get.GetUtil;
import com.thegoate.utils.get.NotFound;
import com.thegoate.utils.togoate.ToGoate;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gets a retrofit response
 * Created by Eric Angeli on 11/7/2025.
 */
@GetUtil(type = Response.class)
@ResetStatics
public class GetRetrofitResponse extends GetTool implements ResetStatic {

    private static final Logger log = LoggerFactory.getLogger(GetRetrofitResponse.class);
    static Map<Object, Goate> resp = new ConcurrentHashMap<>();

    public GetRetrofitResponse() {
        super(null);
    }

    public GetRetrofitResponse(Object selector) {
        super(selector);
    }

    @Override
    public void resetStatics() {
        resp = null;
        resp = new ConcurrentHashMap<>();
    }

    @Override
    public boolean isType(Object check) {
        return check instanceof Response;
    }

    @Override
    public Object from(Object container) {
        Object result = null;
        if (("" + selector).contains("get_from_rest_response::")) {
            selector = ("" + selector).replace("get_from_rest_response::", "");
        }
        if (container != null) {
            if (!resp.containsKey(container)) {
                resp.put(container, new Goate());
            }
            Response r = (Response) container;
            if (selector.equals("status code")) {
                result = r.code();
            } else if (selector.equals(RestResult.body)) {
                result = r.body();
            } else if (selector.equals("body as a string")) {
                if (resp.get(container).containsKey("body as a string")) {
                    result = resp.get(container).get("body as a string");
                } else {
                    result = r.raw().body().toString();
                    resp.get(container).put("body as a string", result);
                }
            } else if (selector.equals("response time")) {
                long tx = r.raw().sentRequestAtMillis();
                long rx = r.raw().receivedResponseAtMillis();
                result = rx - tx;
            } else if (selector.equals("session id")) {
                String cookie = getCookieValue(r.headers().values("Set-Cookie"), "SESSIONID");
                result = cookie.substring(cookie.indexOf("=") + 1);
            } else if (selector.equals("status line")) {
                result = r.message();
            } else if (selector.equals("json")) {
                if (resp.get(container).containsKey("body as a string")) {
                    result = resp.get(container).get("body as a string");
                } else {
                    result = r.raw().body().toString();
                    resp.get(container).put("body as a string", result);
                }
            } else if (selector.equals("xml")) {
                if (resp.get(container).containsKey("body as a string")) {
                    result = resp.get(container).get("body as a string");
                } else {
                    result = r.raw().body().toString();
                    resp.get(container).put("body as a string", result);
                }
            } else if (selector.equals("html")) {
                if (resp.get(container).containsKey("body as a string")) {
                    result = resp.get(container).get("body as a string");
                } else {
                    result = r.raw().body().toString();
                    resp.get(container).put("body as a string", result);
                }
            } else if (selector.toString().startsWith("header")) {
                result = r.headers().get(selector.toString().substring("header".length()).trim());
            } else if (selector.toString().startsWith("cookie")) {
                String cookie = getCookieValue(r.headers().values("Set-Cookie"), selector.toString().substring("cookie".length()).trim());
                result = cookie.substring(cookie.indexOf("=") + 1);
            } else if (selector.toString().startsWith("detailedCookies")) {//this has to be checked before detailedCookie
                result = getDetailedCookies(r.headers().values("Set-Cookie"), r.raw().request().url());
            } else if (selector.toString().startsWith("detailedCookie")) {
                result = getDetailedCookie(r.headers().values("Set-Cookie"), r.raw().request().url(), selector.toString().substring("detailedCookie".length()).trim());
            } else if (selector.toString().startsWith("body as input stream") || selector.toString().startsWith("input stream")) {
                result = new InputStreamReader(r.raw().body().byteStream());
            } else if (selector.toString().startsWith(RestResult.content)) {
                result = r.raw().body();
            } else if (selector.toString().startsWith(RestResult.byteArray)) {
                try {
                    result = r.raw().body().bytes();
                } catch (IOException e) {
                    log.error("problem getting response content: %s".formatted(e.getMessage()), e);
                }
            } else {
                Goate g = null;
                if (resp.get(container).containsKey("as goate")) {
                    g = resp.get(container);
                } else {
                    String bodyString = "";
                    if (resp.get(container).containsKey("body as a string")) {
                        bodyString = resp.get(container).get("body as a string", "", String.class);
                    } else {
                        bodyString = r.raw().body().toString();
                        resp.get(container).put("body as a string", bodyString);
                    }

                    g = new ToGoate(bodyString).convert();
                    resp.get(container).put("as goate", g);
                }
//                result = new Get(selector).from(r.body().prettyPrint());
                if (g != null) {
                    if (g.keys().contains("" + selector)) {
                        result = g.get("" + selector);
                    } else {
                        result = new NotFound("" + selector);
                    }
                }
            }

        }

        result = processNested(result);//process nested gets.
        return result;
    }

    private String getCookieValue(List<String> cookieList, String cookieName) {
        String cookie = cookieList.stream().filter(crumb -> crumb.contains(cookieName)).findFirst().orElse("");
        if (!cookie.isEmpty()) {
            String[] crumbs = cookie.split(";");
            try {
                cookie = crumbs[0];
            } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                cookie = "";
            }
        }
        return cookie;
    }

    private List<Cookie> getDetailedCookies(List<String> cookies, final HttpUrl url) {
        List<Cookie> detailedCookies = new ArrayList<>();
        cookies.forEach(cookie -> {
                    detailedCookies.add(Cookie.parse(url, cookie));
                }
        );
        return detailedCookies;
    }

    private Cookie getDetailedCookie(List<String> cookies, final HttpUrl url, String cookie) {
        List<Cookie> detailedCookies = getDetailedCookies(cookies, url);
        Cookie detailedCookie = null;
        if (detailedCookies != null && detailedCookies.size() > 0) {
            Optional<Cookie> crumb = detailedCookies.stream().filter(dc -> dc.name().equals(cookie)).findFirst();
            if (crumb.isPresent()) {
                detailedCookie = crumb.get();
            }
        }
        return detailedCookie;
    }

}
