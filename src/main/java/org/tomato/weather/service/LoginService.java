package org.tomato.weather.service;

import org.tomato.weather.dao.UserRepository;
import org.tomato.weather.dto.UserDto;
import org.tomato.weather.entity.User;

import java.util.Optional;

public class LoginService {
    private LoginService(){
    }
    private final static LoginService INSTANCE = new LoginService();
    private final UserRepository userRepository = UserRepository.getInstance();

    public Optional<User> findUserByLogin(String login){
        return userRepository.findByLogin(login);
    }

    public static LoginService getInstance() {
        return INSTANCE;
    }
}
