package org.tomato.weather.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.tomato.weather.dao.commons.BaseRepository;
import org.tomato.weather.dto.LocationDTO;
import org.tomato.weather.entity.Location;
import org.tomato.weather.entity.User;
import org.tomato.weather.util.HibernateUtil;

import java.util.List;

public class LocationRepository extends BaseRepository<Long, Location> {
    private final static LocationRepository INSTANCE = new LocationRepository();

    private LocationRepository() {
        super(Location.class);
    }

    public LocationRepository(Class<Location> clazz) {
        super(clazz);
    }

    public static LocationRepository getInstance() {
        return INSTANCE;
    }

    public List<Location> findByUserId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            var cb = session.getCriteriaBuilder();
            var criteria = cb.createQuery(Location.class);
            var locationJpaRoot = criteria.from(Location.class);
            criteria.select(locationJpaRoot).where(cb.equal(locationJpaRoot.get("user").get("id"), id));
            return session.createQuery(criteria).list();
        } catch (Exception e) {
            throw e;
        }
    }

    public void removeLocation(LocationDTO locationDTO, User user) {
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaDelete<Location> criteriaDelete = cb.createCriteriaDelete(Location.class);
            Root<Location> sessionEntity = criteriaDelete.from(Location.class);
            criteriaDelete
                    .where(cb.equal(sessionEntity.get("user").get("id"), user.getId()))
                    .where(cb.equal(sessionEntity.get("name"), locationDTO.getName()))
                    .where(cb.equal(sessionEntity.get("latitude"), locationDTO.getLat()))
                    .where(cb.equal(sessionEntity.get("longitude"), locationDTO.getLon()));
            int count = session.createQuery(criteriaDelete).executeUpdate();
            transaction.commit();
        }
    }

    public List<Location> findByUserAndName(Long id, String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            var cb = session.getCriteriaBuilder();
            var criteria = cb.createQuery(Location.class);
            var locationJpaRoot = criteria.from(Location.class);
            criteria.select(locationJpaRoot)
                    .where(cb.equal(locationJpaRoot.get("user").get("id"), id))
                    .where(cb.like(locationJpaRoot.get("name"), name));
            return session.createQuery(criteria).list();
        } catch (Exception e) {
            throw e;
        }
    }
}
