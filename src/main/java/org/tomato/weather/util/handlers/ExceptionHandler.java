package org.tomato.weather.util.handlers;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.tomato.weather.exception.*;
import org.tomato.weather.exception.authException.*;
import org.tomato.weather.util.ThymeleafUtil;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExceptionHandler {
    private final TemplateEngine templateEngine;
    private final ServletContext context;

    public ExceptionHandler(TemplateEngine templateEngine, ServletContext servletContext) {
        this.templateEngine = templateEngine;
        this.context = servletContext;
    }

    @SneakyThrows
    protected void processTemplate(String templateName, HttpServletRequest request, HttpServletResponse response,
                                   List<String> errorList) {
        WebContext context = ThymeleafUtil.buildWebContext(request, response, this.context);
        log.info("Processing template: {}", templateName);
        if (!errorList.isEmpty()) {
            context.setVariable("errorList", errorList);
        }
        templateEngine.process(templateName, context, response.getWriter());
    }

    public void handle(Exception e, HttpServletRequest req, HttpServletResponse resp) {
        try {
            switch (e) {
                case RegistrationException exc -> {
                    List<String> errors = exc.getViolations().stream()
                            .map(ConstraintViolation::getMessage)
                            .toList();
                    processTemplate("registration", req, resp, errors);
                }
                case LoginDuplicateException ignored -> processTemplate("registration", req, resp,
                        new ArrayList<>(List.of("Пользователь с таким логинов уже зарегестрирован." +
                                " Попробуйте еще раз")));

                case LoginNotFoundException ignored -> processTemplate("login", req, resp,
                        new ArrayList<>(List.of("Пользователь с таким логинов не найден.")));
                case WrongPasswordException ignored -> processTemplate("login", req, resp,
                        new ArrayList<>(List.of("Неверный пароль")));
                case SessionNotFoundException ignore -> processTemplate("login", req, resp,
                        new ArrayList<>(List.of("Ваша сессия устарела.")));
                default -> throw new IllegalStateException("Unexpected value: " + e);
            }
        } catch (Exception notFoundException) {
            log.error("NOT FOUND EXCEPTION");
            notFoundException.printStackTrace();
            // todo сделать перенаправление на /index
            throw new RuntimeException();
        }

    }

    public void handle(ServletInitializationException e) {
        log.error("ServletInitializationException caught", e);
        throw e;
    }

    
}
