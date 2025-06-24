package co.com.bancolombia.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BusinessExceptionTest {

    @Test
    void shouldSetCodeAndMessage() {
        BusinessException exception = new BusinessException("ERROR_CODE", "Error message");
        assertEquals("ERROR_CODE", exception.getCode());
        assertEquals("Error message", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldSetCodeMessageAndCause() {
        Throwable cause = new RuntimeException("Root cause");
        BusinessException exception = new BusinessException("CODE", "Message", cause);
        assertEquals("CODE", exception.getCode());
        assertEquals("Message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}