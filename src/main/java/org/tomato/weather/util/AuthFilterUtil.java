package org.tomato.weather.util;

import jakarta.servlet.http.HttpServletRequest;

public class AuthFilterUtil {
    private final static AuthFilterUtil INSTANCE = new AuthFilterUtil();

    private AuthFilterUtil(){
    }

    public static AuthFilterUtil getInstance() {
        return INSTANCE;
    }

    public String getPathFromrReq(HttpServletRequest req) {
        return req.getRequestURI().substring(req.getContextPath().length());
    }
}
