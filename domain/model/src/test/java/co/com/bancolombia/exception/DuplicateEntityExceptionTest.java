package co.com.bancolombia.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DuplicateEntityExceptionTest {

    @Test
    void shouldSetCodeAndMessage() {
        DuplicateEntityException exception = new DuplicateEntityException("User", "42");
        assertEquals("DUPLICATE_USER", exception.getCode());
        assertEquals("User already exists with identifier: 42", exception.getMessage());
    }

    @Test
    void shouldHandleEmptyEntityAndIdentifier() {
        DuplicateEntityException exception = new DuplicateEntityException("", "");
        assertEquals("DUPLICATE_", exception.getCode());
        assertEquals(" already exists with identifier: ", exception.getMessage());
    }

}