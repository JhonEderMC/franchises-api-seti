package co.com.bancolombia.exception;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FranchiseNotFoundExceptionTest {
    @Test
    void shouldSetErrorCodeAndMessage() {
        String franchiseId = "456";
        FranchiseNotFoundException exception = new FranchiseNotFoundException(franchiseId);

        assertEquals("FRANCHISE_NOT_FOUND", exception.getCode());
        assertEquals("Franchise not found with id: 456", exception.getMessage());
    }

    @Test
    void shouldHandleNullFranchiseId() {
        FranchiseNotFoundException exception = new FranchiseNotFoundException(null);

        assertEquals("FRANCHISE_NOT_FOUND", exception.getCode());
        assertEquals("Franchise not found with id: null", exception.getMessage());
    }

}