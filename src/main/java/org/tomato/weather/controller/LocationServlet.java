package org.tomato.weather.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.thymeleaf.context.WebContext;
import org.tomato.weather.dto.LocationDTO;
import org.tomato.weather.dto.WeatherDTO;
import org.tomato.weather.entity.User;
import org.tomato.weather.exception.locationException.LocationIsEmptyException;
import org.tomato.weather.exception.authException.SessionNotFoundException;
import org.tomato.weather.service.CookieAndSessionService;
import org.tomato.weather.service.LocationService;
import org.tomato.weather.service.OpenWeatherMapService;
import org.tomato.weather.util.AjaxExceptionHandler;
import org.tomato.weather.util.AjaxUtil;
import org.tomato.weather.util.LocationUtil;
import org.tomato.weather.util.ThymeleafUtil;


import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/location")
public class LocationServlet extends BaseServlet {
    private final OpenWeatherMapService openWeatherMapService = OpenWeatherMapService.getInstance();
    private final CookieAndSessionService cookieAndSessionService = CookieAndSessionService.getInstance();
    private final LocationService locationService = LocationService.getInstance();
    private final AjaxExceptionHandler handler = AjaxExceptionHandler.getInstance();
    private final AjaxUtil ajaxUtil = AjaxUtil.getInstance();

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String name = LocationUtil.getInstance().convertRequestToLocationName(req);
        List<LocationDTO> locationDTOList = openWeatherMapService.getCityByName(name);
        Map<LocationDTO, WeatherDTO> weatherDTOS = openWeatherMapService.getWeatherList(locationDTOList);
        WebContext context = ThymeleafUtil.buildWebContext(req, resp, getServletContext());
        List<LocationDTO> locations;
        try {
            User user = cookieAndSessionService.findUserByCookie(req.getCookies());

            locations = locationService.findByUserAndName(user, name);
            for (LocationDTO locationDTO : weatherDTOS.keySet()){
                if (locations.contains(locationDTO)){
                    locationDTO.setTheUserHas(true);
                    weatherDTOS.put(locationDTO, weatherDTOS.get(locationDTO));
                }
            }

        } catch (SessionNotFoundException | LocationIsEmptyException ignored) {
        }

        context.setVariable("weathers", weatherDTOS);
        processTemplate(context, "locations-view", req,  resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        LocationDTO locationDTO = objectMapper.readValue(req.getInputStream(), LocationDTO.class);
        try {
            User user = cookieAndSessionService.findUserByCookie(req.getCookies());
            locationService.saveLocation(user, locationDTO);
            ajaxUtil.senderRespUrl(req.getContextPath() + "/home", resp);
        } catch (Exception e) {
            handler.handle(e, req, resp);
        }


    }
}
