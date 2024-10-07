package org.tomato.weather.service;

import org.tomato.weather.dao.LocationRepository;
import org.tomato.weather.dto.LocationDTO;
import org.tomato.weather.entity.Location;
import org.tomato.weather.entity.User;

import java.util.List;

public class LocationService {
    private final static LocationService INSTANCE = new LocationService();

    private final  LocationRepository locationRepository = LocationRepository.getInstance();

    private LocationService(){
    }

    public static LocationService getInstance() {
        return INSTANCE;
    }
    public void saveLocation(User user, LocationDTO locationDTO){
        locationRepository.save(locationDtoToLocationMapper(locationDTO, user));
    }
    public List<LocationDTO> findByUser(User user){
        return locationRepository.findByUserId(user.getId()).stream().map(this::locationToLocationDtoMapper).toList();
    }

    private Location locationDtoToLocationMapper(LocationDTO locationDTO, User user){
        return Location
                .builder()
                .name(locationDTO.getName())
                .longitude(locationDTO.getLon())
                .latitude(locationDTO.getLat())
                .user(user)
                .build();
    }
    private LocationDTO locationToLocationDtoMapper(Location location){
        return LocationDTO
                .builder()
                .name(location.getName())
                .lon(location.getLongitude())
                .lat(location.getLatitude())
                .build();
    }

    public void removeLocation(User user, LocationDTO locationDTO) {
        locationRepository.removeLocation(locationDTO, user);
    }
}
