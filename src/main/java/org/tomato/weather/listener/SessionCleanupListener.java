package org.tomato.weather.listener;


import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tomato.weather.util.SessionCleaner;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
@Slf4j
public class SessionCleanupListener implements ServletContextListener {
    private ScheduledExecutorService scheduler;


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new SessionCleaner(), 0, 1, TimeUnit.HOURS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }
}
