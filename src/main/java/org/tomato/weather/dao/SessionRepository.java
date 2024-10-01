package org.tomato.weather.dao;

import org.tomato.weather.entity.Session;
import org.tomato.weather.entity.User;
import org.tomato.weather.service.CookieAndSessionService;
import org.tomato.weather.util.HibernateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


public class SessionRepository extends BaseRepository<String, Session> {
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
    public Optional<Session> findUpdateByUser(User user) {
        try (var session = HibernateUtil.getSessionFactory().openSession()){
            var transaction = session.beginTransaction();
            var cb = session.getCriteriaBuilder();
            var criteria = cb.createQuery(Session.class);
            var sessionEnt = criteria.from(Session.class);
            criteria.select(sessionEnt).where(cb.equal(sessionEnt.get("user").get("id"), user.getId()));
            Session session1 = session.createQuery(criteria).uniqueResult();
            if (session1 != null){
                session1.setExpiresAt(LocalDate.now().atStartOfDay()
                        .plusHours(CookieAndSessionService.hoursLiveSession));
                session.merge(session1);
            }
            transaction.commit();
            return Optional.ofNullable(session1);
        }
    }

}
