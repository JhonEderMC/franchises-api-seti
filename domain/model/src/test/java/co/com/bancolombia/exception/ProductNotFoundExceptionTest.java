package co.com.bancolombia.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductNotFoundExceptionTest {

    @Test
    void shouldSetErrorCodeAndMessage() {
        String productId = "789";
        ProductNotFoundException exception = new ProductNotFoundException(productId);

        assertEquals("PRODUCT_NOT_FOUND", exception.getCode());
        assertEquals("Product not found with id: 789", exception.getMessage());
    }

    @Test
    void shouldHandleNullProductId() {
        ProductNotFoundException exception = new ProductNotFoundException(null);

        assertEquals("PRODUCT_NOT_FOUND", exception.getCode());
        assertEquals("Product not found with id: null", exception.getMessage());
    }

}