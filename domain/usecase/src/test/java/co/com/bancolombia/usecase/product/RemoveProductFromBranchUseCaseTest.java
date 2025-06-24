package co.com.bancolombia.usecase.product;

import co.com.bancolombia.exception.BranchNotFoundException;
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

class RemoveProductFromBranchUseCaseTest {

    BranchRepository branchRepository = mock(BranchRepository.class);
    Logger logger = mock(Logger.class);
    RemoveProductFromBranchUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new RemoveProductFromBranchUseCase(branchRepository, logger);
    }

    @Test
    void shouldReturnErrorWhenBranchNotFound() {
        String branchId = "B2";
        String productId = "P2";

        when(branchRepository.findById(branchId)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(branchId, productId))
                .expectErrorSatisfies(e -> assertTrue(e instanceof BranchNotFoundException))
                .verify();

        verify(logger).info(contains("Removing product"), eq(productId), eq(branchId));
        verify(logger, never()).info(contains("Product removed successfully"));
        verify(branchRepository, never()).update(any());
    }

    @Test
    void shouldReturnErrorWhenProductNotFound() {
        String branchId = "B3";
        String productId = "P3";
        Branch branch = Branch.builder().id(branchId).products(Collections.emptyList()).build();

        when(branchRepository.findById(branchId)).thenReturn(Mono.just(branch));

        StepVerifier.create(useCase.execute(branchId, productId))
                .expectErrorSatisfies(e -> assertTrue(e instanceof ProductNotFoundException))
                .verify();

        verify(logger).info(contains("Removing product"), eq(productId), eq(branchId));
        verify(logger, never()).info(contains("Product removed successfully"));
        verify(branchRepository, never()).update(any());
    }

}