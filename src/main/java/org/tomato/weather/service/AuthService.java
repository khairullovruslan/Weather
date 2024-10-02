package org.tomato.weather.service;

import org.hibernate.exception.ConstraintViolationException;
import org.tomato.weather.dao.SessionRepository;
import org.tomato.weather.dao.UserRepository;
import org.tomato.weather.dto.UserDto;
import org.tomato.weather.entity.User;
import org.tomato.weather.exception.LoginDuplicateException;

public class AuthService {
    private final static  AuthService INSTANCE = new AuthService();

    private final UserRepository userRepository = UserRepository.getInstance();
    private final SessionRepository sessionRepository = SessionRepository.getInstance();


    public static AuthService getInstance() {
        return INSTANCE;
    }

    public User registration(UserDto user){
        try {
            return  userRepository.save(User.builder()
                    .login(user.login())
                    .password(user.password())
                    .build());
        }
        catch (ConstraintViolationException e){
            throw new LoginDuplicateException();
        }

    }

    public void logout(String sessionId) {
        sessionRepository.delete(sessionId);
    }
}
