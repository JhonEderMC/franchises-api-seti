package co.com.bancolombia.api.error;

import co.com.bancolombia.api.entry.dto.ErrorResponse;
import co.com.bancolombia.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private ServerWebExchange exchange;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        exchange = mock(ServerWebExchange.class);
        var request = mock(org.springframework.http.server.reactive.ServerHttpRequest.class);
        when(exchange.getRequest()).thenReturn(request);
        when(request.getPath()).thenReturn(org.springframework.http.server.RequestPath.parse("/test", "/test"));
    }

    @Test
    void handleFranchiseNotFoundException() {
        FranchiseNotFoundException ex = new FranchiseNotFoundException("CODE1");

        Mono<ResponseEntity<ErrorResponse>> result = handler.handleFranchiseNotFoundException(ex, exchange);
        ResponseEntity<ErrorResponse> response = result.block();

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("FRANCHISE_NOT_FOUND", response.getBody().getCode());
        assertEquals("Franchise not found with id: CODE1", response.getBody().getMessage());
        assertEquals("/test", response.getBody().getPath());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleBranchNotFoundException() {
        BranchNotFoundException ex = new BranchNotFoundException("CODE2");
        Mono<ResponseEntity<ErrorResponse>> result = handler.handleBranchNotFoundException(ex, exchange);

        ResponseEntity<ErrorResponse> response = result.block();

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("BRANCH_NOT_FOUND", response.getBody().getCode());
        assertEquals("Branch not found with id: CODE2", response.getBody().getMessage());
    }

    @Test
    void handleProductNotFoundException() {
        ProductNotFoundException ex = new ProductNotFoundException("CODE3");

        Mono<ResponseEntity<ErrorResponse>> result = handler.handleProductNotFoundException(ex, exchange);
        ResponseEntity<ErrorResponse> response = result.block();

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("PRODUCT_NOT_FOUND", response.getBody().getCode());
        assertEquals("Product not found with id: CODE3", response.getBody().getMessage());
    }

    @Test
    void handleInvalidStockException() {
        int stock = 1;
        InvalidStockException ex = new InvalidStockException(stock);
        Mono<ResponseEntity<ErrorResponse>> result = handler.handleInvalidStockException(ex, exchange);
        ResponseEntity<ErrorResponse> response = result.block();

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("INVALID_STOCK", response.getBody().getCode());
        assertEquals("Invalid stock value: 1. Stock must be non-negative.", response.getBody().getMessage());
    }

    @Test
    void handleDuplicateEntityException() {
        DuplicateEntityException ex = new DuplicateEntityException("CODE5", "Duplicate entity");

        Mono<ResponseEntity<ErrorResponse>> result = handler.handleDuplicateEntityException(ex, exchange);
        ResponseEntity<ErrorResponse> response = result.block();

        assertEquals(409, response.getStatusCodeValue());
        assertEquals("DUPLICATE_CODE5", response.getBody().getCode());
        assertEquals("CODE5 already exists with identifier: Duplicate entity", response.getBody().getMessage());
    }

    @Test
    void handleBusinessException() {
        BusinessException ex = new BusinessException("CODE6", "Business error");

        Mono<ResponseEntity<ErrorResponse>> result = handler.handleBusinessException(ex, exchange);
        ResponseEntity<ErrorResponse> response = result.block();

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("CODE6", response.getBody().getCode());
        assertEquals("Business error", response.getBody().getMessage());
    }

    @Test
    void handleGenericException() {
        Exception ex = new Exception("Unexpected");

        Mono<ResponseEntity<ErrorResponse>> result = handler.handleGenericException(ex, exchange);
        ResponseEntity<ErrorResponse> response = result.block();

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("INTERNAL_ERROR", response.getBody().getCode());
        assertEquals("An unexpected error occurred", response.getBody().getMessage());
    }
}