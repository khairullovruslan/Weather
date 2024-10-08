package org.tomato.weather.listener;


import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.tomato.weather.util.authUtil.SessionCleaner;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class SessionCleanupListener implements ServletContextListener {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        scheduler.scheduleAtFixedRate(new SessionCleaner(), 0, 1, TimeUnit.HOURS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        scheduler.shutdown();
    }
}
