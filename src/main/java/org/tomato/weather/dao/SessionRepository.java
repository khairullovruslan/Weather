package org.tomato.weather.dao;

import org.tomato.weather.entity.Session;

import java.util.UUID;

public class SessionRepository extends BaseRepository<UUID, Session> {
    private final static SessionRepository INSTANCE = new SessionRepository(Session.class);
    private SessionRepository(){
        super(Session.class);
    }

    public static SessionRepository getInstance() {
        return INSTANCE;
    }
    private SessionRepository(Class<Session> clazz) {
        super(clazz);
    }

}
