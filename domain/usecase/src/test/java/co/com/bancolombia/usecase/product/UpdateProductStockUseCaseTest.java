package co.com.bancolombia.usecase.product;

import co.com.bancolombia.exception.BranchNotFoundException;
import co.com.bancolombia.exception.InvalidStockException;
import co.com.bancolombia.exception.ProductNotFoundException;
import co.com.bancolombia.gateway.BranchRepository;
import co.com.bancolombia.model.Branch;
import co.com.bancolombia.model.log.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UpdateProductStockUseCaseTest {

    BranchRepository branchRepository = mock(BranchRepository.class);
    Logger logger = mock(Logger.class);
    UpdateProductStockUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateProductStockUseCase(branchRepository, logger);
    }

    @Test
    void shouldReturnErrorWhenBranchNotFound() {
        String branchId = "B2";
        String productId = "P2";
        int newStock = 7;

        when(branchRepository.findById(branchId)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(branchId, productId, newStock))
                .expectErrorSatisfies(e -> assertTrue(e instanceof BranchNotFoundException))
                .verify();

        verify(logger).info(contains("Updating product"), eq(productId), eq(newStock), eq(branchId));
        verify(logger, never()).info(contains("Product stock updated successfully"));
        verify(branchRepository, never()).update(any());
    }

    @Test
    void shouldReturnErrorWhenProductNotFound() {
        String branchId = "B3";
        String productId = "P3";
        int newStock = 8;
        Branch branch = Branch.builder().id(branchId).products(Collections.emptyList()).build();

        when(branchRepository.findById(branchId)).thenReturn(Mono.just(branch));

        StepVerifier.create(useCase.execute(branchId, productId, newStock))
                .expectErrorSatisfies(e -> assertTrue(e instanceof ProductNotFoundException))
                .verify();

        verify(logger).info(contains("Updating product"), eq(productId), eq(newStock), eq(branchId));
        verify(logger, never()).info(contains("Product stock updated successfully"));
        verify(branchRepository, never()).update(any());
    }

    @Test
    void shouldReturnErrorWhenStockIsInvalid() {
        String branchId = "B4";
        String productId = "P4";
        int newStock = -1;

        StepVerifier.create(useCase.execute(branchId, productId, newStock))
                .expectErrorSatisfies(e -> assertTrue(e instanceof InvalidStockException))
                .verify();

        verify(logger).info(contains("Updating product"), eq(productId), eq(newStock), eq(branchId));
        verify(logger, never()).info(contains("Product stock updated successfully"));
        verify(branchRepository, never()).findById(any());
        verify(branchRepository, never()).update(any());
    }

}