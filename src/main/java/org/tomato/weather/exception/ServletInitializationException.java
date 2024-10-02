package org.tomato.weather.exception;

public class ServletInitializationException extends RuntimeException {
    public ServletInitializationException(String string, Throwable e) {
        super(string, e);
    }
}
