package org.tomato.weather.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tomato.weather.dao.commons.BaseRepository;
import org.tomato.weather.entity.Session;
import org.tomato.weather.entity.User;
import org.tomato.weather.service.authServices.CookieAndSessionService;
import org.tomato.weather.util.HibernateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;


@Slf4j
public class SessionRepository extends BaseRepository<String, Session> {
    private static final Logger logger =  LoggerFactory.getLogger(SessionRepository.class);
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

    public void cleanSession() {
        try (var session = HibernateUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaDelete<Session> criteriaDelete = cb.createCriteriaDelete(Session.class);
            Root<Session> sessionEntity = criteriaDelete.from(Session.class);
            criteriaDelete.where(cb.lessThan(sessionEntity.get("expiresAt"), LocalDateTime.now()));
            int count = session.createQuery(criteriaDelete).executeUpdate();
            logger.info("Count delete session {}", count);
            transaction.commit();
        }
    }
}
