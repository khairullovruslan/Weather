package org.tomato.weather.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.tomato.weather.exception.ServletInitializationException;
import org.tomato.weather.util.handlers.ExceptionHandler;
import org.tomato.weather.util.ThymeleafUtil;

import java.io.IOException;

@Slf4j
public abstract class BaseServlet extends HttpServlet {
    protected TemplateEngine templateEngine;
    private ExceptionHandler exceptionHandler;
    protected Validator validator;

    @Override
    public void init() {
        try (var validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.getValidator();
            templateEngine = (TemplateEngine) this.getServletContext().getAttribute("templateEngine");
            exceptionHandler = new ExceptionHandler(templateEngine, this.getServletContext());
        } catch (Exception e) {
            exceptionHandler.handle(new ServletInitializationException("Failed to initialize a servlet", e));
        }

    }

    @SneakyThrows
    protected void processTemplate(String templateName, HttpServletRequest request, HttpServletResponse response) {
        WebContext context = ThymeleafUtil.buildWebContext(request, response, getServletContext());
        context.setVariable("context", request.getContextPath());
        log.info("Processing template: {}", templateName);
        templateEngine.process(templateName, context, response.getWriter());
    }

    @SneakyThrows
    protected void processTemplate(WebContext context, String templateName, HttpServletRequest request, HttpServletResponse response) {
        context.setVariable("context", request.getContextPath());
        templateEngine.process(templateName, context, response.getWriter());
    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Processing {} request for {}", req.getMethod(), req.getServletPath());
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            super.service(req, resp);
            log.info("Successfully processed {} request for {}", req.getMethod(), req.getServletPath());
        } catch (Exception e) {
            exceptionHandler.handle(e, req, resp);
        }
    }
}
