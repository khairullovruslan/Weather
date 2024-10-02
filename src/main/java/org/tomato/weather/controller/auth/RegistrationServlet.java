package org.tomato.weather.controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.tomato.weather.controller.BaseServlet;
import org.tomato.weather.dto.UserDto;
import org.tomato.weather.entity.User;
import org.tomato.weather.exception.LoginDuplicateException;
import org.tomato.weather.service.AuthService;
import org.tomato.weather.util.PasswordUtil;
import org.tomato.weather.util.ThymeleafUtil;
import org.tomato.weather.validator.PasswordValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/registration")
public class RegistrationServlet extends BaseServlet {
    private final AuthService authService = AuthService.getInstance();
    private static final Logger logger = Logger.getLogger(RegistrationServlet.class.getName());



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processTemplate("registration", req, resp, new ArrayList<>());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String pwd = req.getParameter("pwd");
        var list = PasswordValidator.validatePassword(pwd);
        if (!list.isEmpty()) {
            processTemplate("registration", req, resp, list);
            return;
        }
        String password = PasswordUtil.hashPassword(pwd);

        try {
            User user = authService.registration(UserDto.builder()
                    .login(login)
                    .password(password)
                    .build());
            logger.info("Пользователь успешно зарегистирован: " + user.getLogin());

            resp.sendRedirect(req.getContextPath() + "/login");
        } catch (LoginDuplicateException e) {
            logger.severe("Ошибка при регистрации: login должен быть уникальным");
            processTemplate("registration", req, resp, new ArrayList<>(List.of("login должен быть уникальным")));
        }
    }
}