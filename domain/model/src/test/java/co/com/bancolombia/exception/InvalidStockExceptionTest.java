package co.com.bancolombia.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidStockExceptionTest {
    @Test
    void shouldSetErrorCodeAndMessageForNegativeStock() {
        InvalidStockException exception = new InvalidStockException(-5);
        assertEquals("INVALID_STOCK", exception.getCode());
        assertEquals("Invalid stock value: -5. Stock must be non-negative.", exception.getMessage());
    }

    @Test
    void shouldSetErrorCodeAndMessageForZeroStock() {
        InvalidStockException exception = new InvalidStockException(0);
        assertEquals("INVALID_STOCK", exception.getCode());
        assertEquals("Invalid stock value: 0. Stock must be non-negative.", exception.getMessage());
    }

    @Test
    void shouldSetErrorCodeAndMessageForPositiveStock() {
        InvalidStockException exception = new InvalidStockException(10);
        assertEquals("INVALID_STOCK", exception.getCode());
        assertEquals("Invalid stock value: 10. Stock must be non-negative.", exception.getMessage());
    }
}