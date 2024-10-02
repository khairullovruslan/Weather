package org.tomato.weather.controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.tomato.weather.controller.BaseServlet;
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
public class LoginServlet extends BaseServlet {
    private final LoginService loginService = LoginService.getInstance();
    private final CookieAndSessionService cookieAndSessionService = CookieAndSessionService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processTemplate("login", req, resp, new ArrayList<>());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = ThymeleafUtil.buildWebContext(req, resp, getServletContext());
        String login = req.getParameter("login");
        String password = req.getParameter("pwd");
        Optional<User> optionalUser = loginService.findUserByLogin(login);
        if (optionalUser.isEmpty()){
            processTemplate("login", req, resp,  new ArrayList<>(List.of("Пользователя с таким Login не был найден. Попробуйте еще раз")));
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
                    processTemplate("login", req, resp,   new ArrayList<>(List.of("Ваша сессия была завершена, войдите еще раз")));
                    return;
                }
            }
            cookie.setMaxAge(60 * 60 * 2);
            resp.addCookie(cookie);
        }
        else {
            processTemplate("login", req, resp,   new ArrayList<>(List.of("Неверный пароль. Попробуйте еще раз")));
        }

    }
}
