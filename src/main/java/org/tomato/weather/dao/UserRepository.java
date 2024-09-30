package org.tomato.weather.dao;

import org.tomato.weather.entity.User;

public class UserRepository extends BaseRepository<Long, User>{
    public UserRepository(Class<User> clazz) {
        super(clazz);
    }

    private static final UserRepository INSTANCE = new UserRepository(User.class);

    public static UserRepository getInstance() {
        return INSTANCE;
    }
}
