package com.latencyviewer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModLogger {
    private static final String MOD_NAME = "Fabric Latency Viewer";
    private static final String LOG_PREFIX = "[" + MOD_NAME + "] ";

    private static ModLogger instance;
    private final Logger logger;

    private ModLogger() {
        this.logger = LoggerFactory.getLogger(MOD_NAME);
    }

    public static ModLogger getInstance() {
        if (instance == null) {
            instance = new ModLogger();
        }
        return instance;
    }

    public void info(String message) {
        logger.info(LOG_PREFIX + message);
    }

    public void info(String format, Object... args) {
        logger.info(LOG_PREFIX + format, args);
    }

    public void debug(String message) {
        logger.debug(LOG_PREFIX + message);
    }

    public void debug(String format, Object... args) {
        logger.debug(LOG_PREFIX + format, args);
    }

    public void warn(String message) {
        logger.warn(LOG_PREFIX + message);
    }

    public void warn(String format, Object... args) {
        logger.warn(LOG_PREFIX + format, args);
    }

    public void error(String message) {
        logger.error(LOG_PREFIX + message);
    }

    public void error(String message, Throwable throwable) {
        logger.error(LOG_PREFIX + message, throwable);
    }

    public void error(String format, Object... args) {
        logger.error(LOG_PREFIX + format, args);
    }
}
