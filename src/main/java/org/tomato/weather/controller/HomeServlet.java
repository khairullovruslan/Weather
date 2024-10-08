package org.tomato.weather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.thymeleaf.context.WebContext;
import org.tomato.weather.dto.LocationDTO;
import org.tomato.weather.dto.WeatherDTO;
import org.tomato.weather.entity.User;
import org.tomato.weather.service.authServices.CookieAndSessionService;
import org.tomato.weather.service.LocationService;
import org.tomato.weather.service.OpenWeatherMapService;
import org.tomato.weather.util.handlers.AjaxExceptionHandler;
import org.tomato.weather.util.AjaxUtil;
import org.tomato.weather.util.ThymeleafUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/home")
public class HomeServlet extends BaseServlet {
    private final CookieAndSessionService cookieAndSessionService = CookieAndSessionService.getInstance();
    private final LocationService locationService = LocationService.getInstance();
    private final OpenWeatherMapService openWeatherMapService = OpenWeatherMapService.getInstance();
    private final AjaxExceptionHandler handler = AjaxExceptionHandler.getInstance();
    private final AjaxUtil ajaxUtil = AjaxUtil.getInstance();


    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        User user = cookieAndSessionService.findUserByCookie(req.getCookies());
        List<LocationDTO> locationDTOList = locationService.findByUser(user);
        Map<LocationDTO, WeatherDTO> weatherDTOS = openWeatherMapService.getWeatherList(locationDTOList);
        WebContext context = ThymeleafUtil.buildWebContext(req, resp, getServletContext());
        context.setVariable("weathers", weatherDTOS);
        processTemplate(context, "home-view", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        LocationDTO locationDTO = objectMapper.readValue(req.getInputStream(), LocationDTO.class);
        try {
            User user = cookieAndSessionService.findUserByCookie(req.getCookies());
            locationService.removeLocation(user, locationDTO);
            ajaxUtil.senderRespUrl(req.getContextPath() + "/home", resp);
        } catch (Exception e) {
            handler.handle(e, req, resp);
        }

    }
}
