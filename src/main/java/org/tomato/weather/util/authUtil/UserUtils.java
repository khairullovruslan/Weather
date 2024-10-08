package org.tomato.weather.util.authUtil;

import jakarta.servlet.http.HttpServletRequest;
import org.tomato.weather.dto.UserRegistrationDto;

public class UserUtils {
    public static UserRegistrationDto buildUserRegistrationDto(HttpServletRequest req){
        String login = req.getParameter("login");
        String pwd = req.getParameter("pwd");
        return UserRegistrationDto
                .builder()
                .password(pwd)
                .login(login)
                .build();
    }
}
