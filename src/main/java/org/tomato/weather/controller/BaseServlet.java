package org.tomato.weather.controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.tomato.weather.util.ThymeleafUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class BaseServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    @Override
    public void init(){
        templateEngine =  (TemplateEngine) this.getServletContext().getAttribute("templateEngine");
    }

    @SneakyThrows
    protected void processTemplate(String templateName, HttpServletRequest request, HttpServletResponse response, List<String> errorList){
        WebContext context = ThymeleafUtil.buildWebContext(request, response, getServletContext());
        log.info("Processing template: {}", templateName);
        if (!errorList.isEmpty()){
            context.setVariable("errorList", errorList);
        }
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
            e.printStackTrace();
            throw e;
//            exceptionHandler.handle(e, req, resp);
        }
    }
}
