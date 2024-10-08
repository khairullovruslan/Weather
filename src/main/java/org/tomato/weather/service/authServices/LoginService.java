package org.tomato.weather.service.authServices;

import org.tomato.weather.dao.UserRepository;
import org.tomato.weather.entity.User;
import org.tomato.weather.exception.authException.LoginNotFoundException;

import java.util.Optional;

public class LoginService {
    private LoginService(){
    }
    private final static LoginService INSTANCE = new LoginService();
    private final UserRepository userRepository = UserRepository.getInstance();

    public User findUserByLogin(String login){
        Optional<User> user = userRepository.findByLogin(login);
        if (user.isPresent()){
            return user.get();
        }
        throw new LoginNotFoundException();

    }

    public static LoginService getInstance() {
        return INSTANCE;
    }
}
