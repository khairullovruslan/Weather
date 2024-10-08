package org.tomato.weather.controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.tomato.weather.controller.BaseServlet;
import org.tomato.weather.dto.UserRegistrationDto;
import org.tomato.weather.entity.User;
import org.tomato.weather.exception.authException.RegistrationException;
import org.tomato.weather.service.authServices.AuthService;
import org.tomato.weather.util.authUtil.PasswordUtil;
import org.tomato.weather.util.authUtil.UserUtils;

import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

@Slf4j
@WebServlet("/registration")
public class RegistrationServlet extends BaseServlet {
    private final AuthService authService = AuthService.getInstance();
    private static final Logger logger = Logger.getLogger(RegistrationServlet.class.getName());


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processTemplate("registration", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UserRegistrationDto userRegistrationDTO = UserUtils.buildUserRegistrationDto(req);
        Set<ConstraintViolation<UserRegistrationDto>> violations = validator.validate(userRegistrationDTO);

        if (!violations.isEmpty()) {
            throw new RegistrationException(violations);
        }

        String password = PasswordUtil.hashPassword(userRegistrationDTO.getPassword());
        userRegistrationDTO.setPassword(password);
        User user = authService.registration(userRegistrationDTO);
        logger.info("Пользователь успешно зарегистирован: " + user.getLogin());
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}