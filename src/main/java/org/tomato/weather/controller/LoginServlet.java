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
import org.tomato.weather.service.CookieAndSessionService;
import org.tomato.weather.service.LoginService;
import org.tomato.weather.util.PasswordUtil;
import org.tomato.weather.util.ThymeleafUtil;

import java.io.IOException;

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
        String login = req.getParameter("login");
        String password = req.getParameter("pwd");
        User user = loginService.findUserByLogin(login);
        if (PasswordUtil.checkPassword(password, user.getPassword())){
            Cookie cookie = cookieAndSessionService.createCookieAndRegisterNewSession(user);
            cookie.setMaxAge(60 * 60 * 2);
            resp.addCookie(cookie);
        }
        else {
           resp.sendRedirect("/login");
        }

    }
}
