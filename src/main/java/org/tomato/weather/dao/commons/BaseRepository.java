package org.tomato.weather.dao.commons;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.tomato.weather.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public  abstract class BaseRepository<K, E> implements Repository<K, E> {
    private final Class<E> clazz;

    @Override
    public E save(E entity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            var transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
            return entity;
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }


    }

    @Override
    public void delete(K id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            var transaction = session.beginTransaction();
            session.remove(session.find(clazz, id));
            transaction.commit();
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Optional<E> findById(K id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            return Optional.ofNullable(session.find(clazz, id));
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<E> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            var criteria = session.getCriteriaBuilder().createQuery(clazz);
            criteria.from(clazz);
            return session.createQuery(criteria).getResultList();
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }

    }
}
