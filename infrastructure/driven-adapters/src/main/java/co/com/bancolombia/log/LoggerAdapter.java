package co.com.bancolombia.log;

import co.com.bancolombia.model.log.Logger;
import org.slf4j.LoggerFactory;


public class LoggerAdapter implements Logger {
    private final org.slf4j.Logger logger;

    public LoggerAdapter(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    @Override
    public void info(String message, Object... args) {
        logger.info(message, args);
    }

    @Override
    public void error(String message, Object... args) {
        logger.error(message, args);
    }

    @Override
    public void warn(String message, Object... args) {
        logger.warn(message, args);
    }

    @Override
    public void debug(String message, Object... args) {
        logger.debug(message, args);
    }
}

