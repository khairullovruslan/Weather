package org.tomato.weather.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.tomato.weather.service.AuthService;
import org.tomato.weather.service.CookieAndSessionService;

import java.io.IOException;

@WebServlet("/logout")
public class LogOutServlet extends HttpServlet {
    private final AuthService authService = AuthService.getInstance();
    private final CookieAndSessionService cookieAndSessionService = CookieAndSessionService.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cookie = cookieAndSessionService.findCookie(req.getCookies());
        if (cookie != null){
            authService.logout(cookie);
        }
        Cookie cookie1 = new Cookie("username", null);
        cookie1.setMaxAge(0);
        resp.addCookie(cookie1);
        resp.sendRedirect(req.getContextPath() + "/login");

    }
}
