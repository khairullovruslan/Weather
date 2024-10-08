package org.tomato.weather.service.authServices;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.exception.ConstraintViolationException;
import org.tomato.weather.dao.SessionRepository;
import org.tomato.weather.dao.UserRepository;
import org.tomato.weather.dto.UserRegistrationDto;
import org.tomato.weather.entity.User;
import org.tomato.weather.exception.authException.LoginDuplicateException;
import org.tomato.weather.exception.authException.SessionDuplicateException;
import org.tomato.weather.exception.authException.WrongPasswordException;
import org.tomato.weather.util.PasswordUtil;

public class AuthService {
    private final static AuthService INSTANCE = new AuthService();

    private final UserRepository userRepository = UserRepository.getInstance();
    private final SessionRepository sessionRepository = SessionRepository.getInstance();
    private final LoginService loginService = LoginService.getInstance();
    private final CookieAndSessionService cookieAndSessionService = CookieAndSessionService.getInstance();

    public static AuthService getInstance() {
        return INSTANCE;
    }

    public User registration(UserRegistrationDto user) {
        try {
            return userRepository.save(User.builder()
                    .login(user.getLogin())
                    .password(user.getPassword())
                    .build());
        } catch (ConstraintViolationException e) {
            throw new LoginDuplicateException();
        }
    }

    public Cookie login(HttpServletRequest req) {
        String login = req.getParameter("login");
        String password = req.getParameter("pwd");
        User user = loginService.findUserByLogin(login);
        if (PasswordUtil.checkPassword(password, user.getPassword())) {
            try {
                return cookieAndSessionService.createCookieAndRegisterNewSession(user);
            } catch (SessionDuplicateException e) {
                return cookieAndSessionService.findAndUpdateSessionByUserId(user);
            }
        }
        throw new WrongPasswordException();
    }

    public void logout(String sessionId) {
        sessionRepository.delete(sessionId);
    }
}
