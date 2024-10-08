package or.tomato.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tomato.weather.dto.LocationDTO;
import org.tomato.weather.dto.WeatherDTO;
import org.tomato.weather.entity.Location;
import org.tomato.weather.exception.locationException.LocationNotFoundException;
import org.tomato.weather.exception.locationException.WeatherNotFoundException;
import org.tomato.weather.service.OpenWeatherMapService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OpenWeatherMapServiceTest {
    private OpenWeatherMapService openWeatherApiService;
    private HttpClient httpClientMock;
    private HttpResponse<String> httpResponseMock;


    @BeforeEach
     void setUp() {
        httpClientMock = mock(HttpClient.class);
        httpResponseMock = mock(HttpResponse.class);
        openWeatherApiService = OpenWeatherMapService.getInstance();

        try {
            var httpClientField = OpenWeatherMapService.class.getDeclaredField("httpClient");
            httpClientField.setAccessible(true);
            httpClientField.set(openWeatherApiService, httpClientMock);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
     void testGetWeather_Success() throws Exception {
        String jResp = "{\"coord\":{\"lon\":49.1221,\"lat\":55.7887},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"base\":\"stations\",\"main\":{\"temp\":289.49,\"feels_like\":288.07,\"temp_min\":289.49,\"temp_max\":289.49,\"pressure\":1021,\"humidity\":34,\"sea_level\":1021,\"grnd_level\":1007},\"visibility\":10000,\"wind\":{\"speed\":3.74,\"deg\":207,\"gust\":4.65},\"clouds\":{\"all\":57},\"dt\":1728036716,\"sys\":{\"type\":1,\"id\":9038,\"country\":\"RU\",\"sunrise\":1728010347,\"sunset\":1728051100},\"timezone\":10800,\"id\":551487,\"name\":\"Kazan’\",\"cod\":200}";

        when(httpResponseMock.statusCode()).thenReturn(200);
        when(httpResponseMock.body()).thenReturn(jResp);
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponseMock);


        Location location = Location
                .builder()
                .latitude(55.7887)
                .longitude(49.1221)
                .name("Kazan").build();

        WeatherDTO weatherDTO = openWeatherApiService.getWeather(location);

        assertNotNull(weatherDTO);
        assertEquals("Kazan", weatherDTO.getName());
        assertEquals("broken clouds", weatherDTO.getWeather().get(0).getDescription());
    }

    @Test
    void testGetLocation_Success() throws IOException, InterruptedException, URISyntaxException {
        String jResp = "[{\"name\":\"Kazan\",\"local_names\":{\"cs\":\"Kazaň\",\"es\":\"Kazán\",\"et\":\"Kaasan\",\"ru\":\"городской округ Казань\",\"eo\":\"Kazano\",\"pt\":\"Cazã\",\"en\":\"Kazan\",\"kk\":\"Қазан\",\"sk\":\"Kazaň\",\"fr\":\"Kazan\",\"ca\":\"districte urbà de Kazan\",\"id\":\"Kazan\",\"lt\":\"Kazanės miesto apygarda\",\"hy\":\"Կազան\",\"it\":\"Kazan'\",\"sr\":\"Казањ\",\"ka\":\"ყაზანი\",\"cv\":\"Хусан\",\"da\":\"Kazan\",\"pl\":\"Kazań\",\"ascii\":\"Kazan\",\"ja\":\"カザン\",\"tr\":\"Kazan\",\"ko\":\"카잔\",\"uz\":\"Qozon\",\"ro\":\"Kazan\",\"tt\":\"Казан шәһәр бүлгесе\",\"uk\":\"Казань\",\"fi\":\"Kazan\",\"ar\":\"قازان\",\"az\":\"Kazan\",\"zh\":\"喀山\",\"kv\":\"Казан\",\"ba\":\"Ҡазан\",\"he\":\"קאזאן\",\"de\":\"Kasan\",\"hu\":\"Kazany\",\"nl\":\"Kazan\",\"hi\":\"काज़ान\",\"lv\":\"Kazaņa\",\"os\":\"Хъазан\",\"tk\":\"Kazan\",\"feature_name\":\"Kazan\",\"oc\":\"Kazan\",\"hr\":\"Kazanj\",\"kn\":\"ಕಾಜಾ಼ನ್\"},\"lat\":55.7823547,\"lon\":49.1242266,\"country\":\"RU\",\"state\":\"Tatarstan\"},{\"name\":\"Kazan\",\"local_names\":{\"cv\":\"Хусан\",\"lv\":\"Kazaņa\",\"ja\":\"カザン\",\"tr\":\"Kazan\",\"ro\":\"Kazan\",\"zh\":\"喀山\",\"et\":\"Kaasan\",\"pl\":\"Kazań\",\"sk\":\"Kazaň\",\"ca\":\"Kazan\",\"eo\":\"Kazano\",\"ar\":\"قازان\",\"hr\":\"Kazanj\",\"en\":\"Kazan\",\"fi\":\"Kazan\",\"ko\":\"카잔\",\"de\":\"Kasan\",\"uz\":\"Qozon\",\"it\":\"Kazan'\",\"ru\":\"Казань\",\"da\":\"Kazan\",\"fr\":\"Kazan\",\"lt\":\"Kazanė\",\"es\":\"Kazán\",\"tk\":\"Kazan\",\"ba\":\"Ҡазан\",\"uk\":\"Казань\",\"ascii\":\"Kazan\",\"ku\":\"Kazan\",\"feature_name\":\"Kazan\",\"hi\":\"काज़ान\",\"hu\":\"Kazany\",\"oc\":\"Kazan\",\"pt\":\"Cazã\",\"hy\":\"Կազան\",\"ka\":\"ყაზანი\",\"sr\":\"Казањ\",\"kk\":\"Қазан\",\"nl\":\"Kazan\",\"os\":\"Хъазан\",\"az\":\"Kazan\",\"cs\":\"Kazaň\",\"tt\":\"Казан\",\"he\":\"קאזאן\",\"kn\":\"ಕಾಜಾ಼ನ್\",\"id\":\"Kazan\",\"kv\":\"Казан\"},\"lat\":55.7823547,\"lon\":49.1242266,\"country\":\"RU\",\"state\":\"Tatarstan\"},{\"name\":\"Kahramankazan\",\"local_names\":{\"ja\":\"カフラマンカザン\",\"tr\":\"Kahramankazan\",\"ur\":\"کازان\",\"zh\":\"卡拉曼卡贊\",\"ce\":\"КахӀраманказан\",\"fa\":\"کازان\"},\"lat\":40.2054445,\"lon\":32.6813148,\"country\":\"TR\"},{\"name\":\"Kazan\",\"lat\":39.5854048,\"lon\":44.0041981,\"country\":\"TR\"},{\"name\":\"Kazan\",\"local_names\":{\"tr\":\"Kazan\"},\"lat\":37.6978656,\"lon\":44.2385417,\"country\":\"TR\"}]";
        when(httpResponseMock.statusCode()).thenReturn(200);
        when(httpResponseMock.body()).thenReturn(jResp);
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponseMock);

        List<LocationDTO> locationDTOList = openWeatherApiService.getCityByName("Kazan");

        assertNotNull(locationDTOList);
        assertEquals(5, locationDTOList.size());
        for (LocationDTO locationDTO: locationDTOList){
            assertTrue(locationDTO.getName().contains("Kazan") || locationDTO.getName().contains("kazan"));
        }
    }

    @Test
    void testGetWeather_throwsException() throws IOException, InterruptedException {
        when(httpResponseMock.statusCode()).thenReturn(404);
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponseMock);

        Location location = Location
                .builder()
                .latitude(55.7887)
                .longitude(49.1221)
                .name("Kazan").build();

        assertThrows(
                WeatherNotFoundException.class,
                () -> openWeatherApiService.getWeather(location));
    }

    @Test
    void testGetLocation_throwsException() throws IOException, InterruptedException {
        when(httpResponseMock.statusCode()).thenReturn(404);
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponseMock);

        assertThrows(
                LocationNotFoundException.class,
                () -> openWeatherApiService.getCityByName("Kazan"));
    }
}
