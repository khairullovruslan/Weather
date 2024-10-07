package org.tomato.weather.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.tomato.weather.dto.LocationDTO;
import org.tomato.weather.dto.WeatherDTO;
import org.tomato.weather.entity.Location;
import org.tomato.weather.exception.LocationNotFoundException;
import org.tomato.weather.exception.WeatherNotFoundException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenWeatherMapService {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String KEY = System.getenv("api_key");
    private static final String HTTP_REQUEST_GET_CITY_BY_NAME = "http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=5&appid=%s";
    private static final String HTTP_REQUEST_GET_WEATHER = "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s&lang=en&units=metric";
    private static final OpenWeatherMapService INSTANCE = new OpenWeatherMapService();
    private OpenWeatherMapService(){}

    public static OpenWeatherMapService getInstance() {
        return INSTANCE;
    }

    public WeatherDTO getWeather(Location location) throws URISyntaxException, IOException, InterruptedException {
        String cityReq = String.format(HTTP_REQUEST_GET_WEATHER, location.getLatitude(), location.getLongitude(), KEY);
        HttpResponse<String> response = httpSender(cityReq);
        if (response.statusCode() == 200) {
            WeatherDTO weatherDTO = objectMapper.readValue(response.body(), WeatherDTO.class);
            weatherDTO.setName(location.getName());
            return weatherDTO;
        }
        throw new WeatherNotFoundException();
    }

    public Map<LocationDTO, WeatherDTO> getWeatherList(List<LocationDTO> locations) throws URISyntaxException, IOException, InterruptedException {
        Map<LocationDTO, WeatherDTO> map = new HashMap<>();
        for (LocationDTO location : locations) {
            String cityReq = String.format(HTTP_REQUEST_GET_WEATHER, location.getLat(), location.getLon(), KEY);
            HttpResponse<String> response = httpSender(cityReq);
            if (response.statusCode() == 200) {
                WeatherDTO weatherDTO = objectMapper.readValue(response.body(), WeatherDTO.class);
                weatherDTO.setName(location.getName());
                map.put(location, weatherDTO);
            }
        }
        return map;
    }

    public List<LocationDTO> getCityByName(String name) throws URISyntaxException, IOException, InterruptedException {
        String encodedLocationName = URLEncoder.encode(name, StandardCharsets.UTF_8);
        String cityReq = String.format(HTTP_REQUEST_GET_CITY_BY_NAME, encodedLocationName, KEY);
        HttpResponse<String> response = httpSender(cityReq);
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<>() {
            });
        }
        throw new LocationNotFoundException();
    }


    private HttpResponse<String> httpSender(String request) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI(request))
                .GET()
                .build();
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

}
