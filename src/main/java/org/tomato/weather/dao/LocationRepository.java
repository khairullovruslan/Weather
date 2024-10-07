package org.tomato.weather.dao;

import org.tomato.weather.entity.Location;

public class LocationRepository extends BaseRepository<Long, Location>{
    private final static LocationRepository INSTANCE = new LocationRepository();
    private LocationRepository(){
        super(Location.class);
    }
    public LocationRepository(Class<Location> clazz) {
        super(clazz);
    }

    public static LocationRepository getInstance() {
        return INSTANCE;
    }
}
