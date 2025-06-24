package co.com.bancolombia.log;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LoggerAdapterTest {

    private Logger slf4jLogger;
    private LoggerAdapter loggerAdapter;

    @BeforeEach
    void setUp() {
        slf4jLogger = mock(Logger.class);
        loggerAdapter = new LoggerAdapter(Object.class) {
            {
                // Override the logger field for testing
                this.logger = slf4jLogger;
            }
        };
    }

    @Test
    void shouldDelegateInfo() {
        loggerAdapter.info("Info message {}", 1);
        verify(slf4jLogger).info("Info message {}", 1);
    }

    @Test
    void shouldDelegateError() {
        loggerAdapter.error("Error message {}", "err");
        verify(slf4jLogger).error("Error message {}", "err");
    }

    @Test
    void shouldDelegateWarn() {
        loggerAdapter.warn("Warn message {}", 2);
        verify(slf4jLogger).warn("Warn message {}", 2);
    }

    @Test
    void shouldDelegateDebug() {
        loggerAdapter.debug("Debug message {}", 3);
        verify(slf4jLogger).debug("Debug message {}", 3);
    }
}