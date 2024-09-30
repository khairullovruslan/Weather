package org.tomato.weather.dao;

import java.util.List;
import java.util.Optional;

public interface Repository<K, E> {
    E save(E entity);

    void delete(K id);

    Optional<E> findById(K id);

    List<E> findAll();

}
