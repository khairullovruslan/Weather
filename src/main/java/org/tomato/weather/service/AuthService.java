package org.tomato.weather.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.tomato.weather.dao.SessionRepository;
import org.tomato.weather.dao.UserRepository;
import org.tomato.weather.dto.UserDto;
import org.tomato.weather.entity.User;
import org.tomato.weather.exception.LoginDuplicateException;
import org.tomato.weather.exception.SessionDuplicateException;
import org.tomato.weather.mappers.UserMapper;

import java.util.UUID;

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
