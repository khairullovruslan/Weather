package org.tomato.weather.controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;
import org.tomato.weather.controller.BaseServlet;
import org.tomato.weather.entity.User;
import org.tomato.weather.exception.SessionDuplicateException;
import org.tomato.weather.service.AuthService;
import org.tomato.weather.service.CookieAndSessionService;
import org.tomato.weather.service.LoginService;
import org.tomato.weather.util.PasswordUtil;
import org.tomato.weather.util.ThymeleafUtil;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends BaseServlet {
    private final AuthService authService = AuthService.getInstance();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processTemplate("login", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addCookie(authService.login(req));
        // todo  redirect home page



    }
}
