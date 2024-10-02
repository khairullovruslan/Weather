package org.tomato.weather.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tomato.weather.dao.SessionRepository;
import org.tomato.weather.listener.SessionCleanupListener;

import java.time.LocalDateTime;

@Slf4j
public class SessionCleaner implements Runnable{
    private final Logger logger = LoggerFactory.getLogger(SessionCleanupListener.class);
    @Override
    public void run() {
        logger.info("start cleaner : {}", LocalDateTime.now());
        SessionRepository.getInstance().cleanSession();
    }
}
