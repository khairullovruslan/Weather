package org.tomato.weather.util.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.tomato.weather.exception.authException.SessionNotFoundException;

import java.io.PrintWriter;

public class AjaxExceptionHandler {
    private static final AjaxExceptionHandler INSTANCE = new AjaxExceptionHandler();
    private AjaxExceptionHandler(){}

    public static AjaxExceptionHandler getInstance() {
        return INSTANCE;
    }

    @SneakyThrows
    public void senderRespUrl(String url, HttpServletResponse resp){
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = resp.getWriter();
        out.print("{\"url\": \"" + url + "\"}");
        out.flush();
    }
    public void handle(Exception e, HttpServletRequest req, HttpServletResponse resp) {
        try {
            switch (e) {
                case SessionNotFoundException ignored -> {
                    senderRespUrl(req.getContextPath() + "/login", resp);
                }

                default -> senderRespUrl(req.getContextPath() + "/error", resp);
            }
        } catch (Exception notFoundException) {
            notFoundException.printStackTrace();
            senderRespUrl(req.getContextPath() + "/error", resp);
        }

    }
}
