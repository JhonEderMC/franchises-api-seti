package co.com.bancolombia.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BranchNotFoundExceptionTest {

    @Test
    void shouldSetErrorCodeAndMessage() {
        String branchId = "123";
        BranchNotFoundException exception = new BranchNotFoundException(branchId);

        assertEquals("BRANCH_NOT_FOUND", exception.getCode());
        assertEquals("Branch not found with id: 123", exception.getMessage());
    }

    @Test
    void shouldHandleNullBranchId() {
        BranchNotFoundException exception = new BranchNotFoundException(null);

        assertEquals("BRANCH_NOT_FOUND", exception.getCode());
        assertEquals("Branch not found with id: null", exception.getMessage());
    }

}