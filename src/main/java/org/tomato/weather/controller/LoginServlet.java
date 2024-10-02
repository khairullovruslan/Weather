package org.tomato.weather.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.tomato.weather.entity.User;
import org.tomato.weather.exception.SessionDuplicateException;
import org.tomato.weather.service.CookieAndSessionService;
import org.tomato.weather.service.LoginService;
import org.tomato.weather.util.PasswordUtil;
import org.tomato.weather.util.ThymeleafUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final LoginService loginService = LoginService.getInstance();
    private final CookieAndSessionService cookieAndSessionService = CookieAndSessionService.getInstance();
    private  TemplateEngine templateEngine;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        templateEngine = (TemplateEngine) req.getServletContext().getAttribute("templateEngine");
        WebContext context = ThymeleafUtil.buildWebContext(req, resp, getServletContext());
        templateEngine.process("login", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = ThymeleafUtil.buildWebContext(req, resp, getServletContext());
        String login = req.getParameter("login");
        String password = req.getParameter("pwd");
        Optional<User> optionalUser = loginService.findUserByLogin(login);
        if (optionalUser.isEmpty()){
            context.setVariable("errorList", new ArrayList<>(List.of("Пользователя с таким Login не был найден. Попробуйте еще раз")));
            templateEngine.process("login", context, resp.getWriter());
            return;
        }
        User user = optionalUser.get();
        if (PasswordUtil.checkPassword(password, user.getPassword())){
            Cookie cookie;
            try {
                cookie = cookieAndSessionService.createCookieAndRegisterNewSession(user);
            }
            catch (SessionDuplicateException e){

                Optional<Cookie> cookieOptional = cookieAndSessionService.findAndUpdateSessionByUserId(user);
                if (cookieOptional.isPresent()){
                    cookie = cookieOptional.get();
                }
                else {
                    context.setVariable("errorList", new ArrayList<>(List.of("Ваша сессия была завершена, войдите еще раз")));
                    templateEngine.process("login", context, resp.getWriter());
                    return;
                }
            }
            cookie.setMaxAge(60 * 60 * 2);
            resp.addCookie(cookie);
        }
        else {
            context.setVariable("errorList", new ArrayList<>(List.of("Неверный пароль. Попробуйте еще раз")));
            templateEngine.process("login", context, resp.getWriter());
        }

    }
}
