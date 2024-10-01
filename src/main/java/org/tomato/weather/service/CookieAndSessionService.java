package org.tomato.weather.service;

import jakarta.servlet.http.Cookie;
import org.tomato.weather.dao.SessionRepository;
import org.tomato.weather.entity.Session;
import org.tomato.weather.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class CookieAndSessionService {
    private final SessionRepository sessionRepository = SessionRepository.getInstance();
    private final int hoursLiveSession = 15;
    private final static CookieAndSessionService INSTANCE = new CookieAndSessionService();
    private CookieAndSessionService(){}

    public static CookieAndSessionService getInstance() {
        return INSTANCE;
    }


    public Cookie createCookieAndRegisterNewSession(User user){
        LocalDateTime dateTimeWithHours = LocalDate.now().atStartOfDay()
                .plusHours(hoursLiveSession);
        Session session = Session
                .builder()
                .id(String.valueOf(UUID.randomUUID()))
                .user(user)
                .expiresAt(dateTimeWithHours)
                .build();
        try {
            Session session1 = sessionRepository.save(session);
            return new Cookie("SESSION_ID", session1.getId());
        }
        catch (Exception e){
            e.printStackTrace();;
            throw  e;
        }

    }
}
