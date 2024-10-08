package org.tomato.weather.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import org.thymeleaf.TemplateEngine;
import org.tomato.weather.exception.authException.SessionNotFoundException;
import org.tomato.weather.service.authServices.CookieAndSessionService;
import org.tomato.weather.util.AuthFilterUtil;
import org.tomato.weather.util.ExceptionHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebFilter("/*")
public class AuthFilter extends HttpFilter {
    private final List<String> pagesForAuthorizedUsers = new ArrayList<>(List.of("/home", "/logout", "/location"));
    private final List<String> pagesForAllUsers = new ArrayList<>(List.of("/registration", "/login", "/location"));
    private final AuthFilterUtil util = AuthFilterUtil.getInstance();
    private final CookieAndSessionService cookieAndSessionService = CookieAndSessionService.getInstance();
    private TemplateEngine templateEngine;
    private ExceptionHandler exceptionHandler;
    @Override
    public void init() throws ServletException {
        templateEngine = (TemplateEngine) this.getServletContext().getAttribute("templateEngine");
        exceptionHandler = new ExceptionHandler(templateEngine, this.getServletContext());
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        req.setCharacterEncoding("utf-8");
        res.setCharacterEncoding("utf-8");
        try {
            String path = util.getPathFromrReq(req);
            try {
                cookieAndSessionService.findUserByCookie(req.getCookies());
                if (!checkedRulesForAuthorizedUser(path)){
                    res.sendRedirect(req.getContextPath() + "/home");
                }
            }
            catch (SessionNotFoundException e){
                System.out.println("сессия не найдена");
                if (!checkedRulesForUnauthorizedUser(path)){
                    res.sendRedirect(req.getContextPath() + "/login");
                }
            }
            chain.doFilter(req, res);
        }
        catch (Exception e){
            exceptionHandler.handle(e, req, res);
        }
    }

    private boolean checkedRulesForAuthorizedUser(String path){
        return pagesForAuthorizedUsers.contains(path);

    }
    private boolean checkedRulesForUnauthorizedUser(String path){
        return pagesForAllUsers.contains(path);

    }


}
