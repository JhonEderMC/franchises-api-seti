package co.com.bancolombia.model.log;

public interface Logger {
    void info(String message, Object... args);
    void error(String message, Object... args);
    void warn(String message, Object... args);
    void debug(String message, Object... args);
}

