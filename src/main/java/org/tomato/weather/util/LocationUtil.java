package org.tomato.weather.util;

import jakarta.servlet.http.HttpServletRequest;
import org.tomato.weather.exception.locationException.LocationIsEmptyException;

public class LocationUtil {
    private final static LocationUtil INSTANCE = new LocationUtil();
    private LocationUtil(){}

    public static LocationUtil getInstance() {
        return INSTANCE;
    }

    public String convertRequestToLocationName(HttpServletRequest request){
        String name = request.getParameter("name");
        if (name == null){
            throw new LocationIsEmptyException();
        }
        return name;

    }

}
