package org.tomato.weather.dao;

import org.hibernate.Session;
import org.tomato.weather.dao.commons.BaseRepository;
import org.tomato.weather.entity.User;
import org.tomato.weather.util.HibernateUtil;

import java.util.Optional;

public class UserRepository extends BaseRepository<Long, User> {
    private UserRepository(Class<User> clazz) {
        super(clazz);
    }

    private UserRepository(){
        super(User.class);
    }

    private static final UserRepository INSTANCE = new UserRepository(User.class);

    public static UserRepository getInstance() {
        return INSTANCE;
    }

    public Optional<User> findByLogin(String login) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()){

            var cb = session.getCriteriaBuilder();
            var criteria = cb.createQuery(User.class);
            var user = criteria.from(User.class);
            criteria.select(user).where(cb.equal(user.get("login"), login));
            var user1 = session.createQuery(criteria).uniqueResult();
            return Optional.ofNullable(user1);
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
