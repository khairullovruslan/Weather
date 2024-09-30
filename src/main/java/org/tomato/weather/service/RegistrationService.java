package org.tomato.weather.service;

import lombok.RequiredArgsConstructor;
import org.tomato.weather.dao.UserRepository;
import org.tomato.weather.dto.UserDto;
import org.tomato.weather.entity.User;
import org.tomato.weather.mappers.UserMapper;

public class RegistrationService {
    private final static  RegistrationService INSTANCE = new RegistrationService();

    private final UserRepository userRepository = UserRepository.getInstance();

    public static RegistrationService getInstance() {
        return INSTANCE;
    }

    public User registration(UserDto user){
        return userRepository.save(User.builder()
                        .login(user.login())
                        .password(user.password())
                .build());
    }
}
