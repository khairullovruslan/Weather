package org.tomato.weather.controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.tomato.weather.dto.UserDto;
import org.tomato.weather.entity.User;
import org.tomato.weather.service.RegistrationService;
import org.tomato.weather.util.PasswordUtil;
import org.tomato.weather.util.ThymeleafUtil;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/registration") // Исправленный URL-адрес
public class RegistrationServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private final RegistrationService registrationService = RegistrationService.getInstance();
    private static final Logger logger = Logger.getLogger(RegistrationServlet.class.getName()); // Логгер



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        templateEngine = (TemplateEngine) req.getServletContext().getAttribute("templateEngine"); // Инициализация templateEngine

        WebContext context = ThymeleafUtil.buildWebContext(req, resp, getServletContext());
        templateEngine.process("registration", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = PasswordUtil.hashPassword(req.getParameter("pwd"));

        try {
            User user = registrationService.registration(UserDto.builder()
                    .login(login)
                    .password(password)
                    .build());
            logger.info("Пользователь успешно зарегистирован: " + user.getLogin()); //  Используем логгер

            resp.sendRedirect(req.getContextPath() + "/success"); // Перенаправление на страницу успеха
        } catch (Exception e) {
            logger.severe("Ошибка при регистрации: " + e.getMessage()); //  Используем логгер для записи ошибок

            WebContext context = ThymeleafUtil.buildWebContext(req, resp, getServletContext());
            context.setVariable("error", e.getMessage());
            templateEngine.process("registration", context, resp.getWriter());
        }
    }
}