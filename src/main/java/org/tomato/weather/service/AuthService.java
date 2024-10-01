package org.tomato.weather.service;

import lombok.RequiredArgsConstructor;
import org.tomato.weather.dao.SessionRepository;
import org.tomato.weather.dao.UserRepository;
import org.tomato.weather.dto.UserDto;
import org.tomato.weather.entity.User;
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
        return userRepository.save(User.builder()
                        .login(user.login())
                        .password(user.password())
                .build());
    }

    public void logout(String sessionId) {
        sessionRepository.delete(sessionId);
    }
}
