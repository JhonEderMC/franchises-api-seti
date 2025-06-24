package co.com.bancolombia.usecase.product;

import co.com.bancolombia.exception.BranchNotFoundException;
import co.com.bancolombia.exception.InvalidStockException;
import co.com.bancolombia.gateway.BranchRepository;
import co.com.bancolombia.model.Branch;
import co.com.bancolombia.model.log.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AddProductToBranchUseCaseTest {

    BranchRepository branchRepository = mock(BranchRepository.class);
    Logger logger = mock(Logger.class);
    AddProductToBranchUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AddProductToBranchUseCase(branchRepository, logger);
    }

    @Test
    void shouldAddProductToBranchSuccessfully() {
        String branchId = "B1";
        String productName = "Product X";
        int stock = 10;
        Branch branch = Branch.builder().id(branchId).products(new ArrayList<>()).build();

        // Simulate branch.addProduct returns a new branch with the product added
        when(branchRepository.findById(branchId)).thenReturn(Mono.just(branch));
        when(branchRepository.update(any(Branch.class))).thenAnswer(invocation -> {
            Branch updated = invocation.getArgument(0);
            return Mono.just(updated);
        });

        StepVerifier.create(useCase.execute(branchId, productName, stock))
                .assertNext(product -> {
                    assertEquals(productName, product.getName());
                    assertEquals(stock, product.getStock());
                    assertEquals(branchId, product.getBranchId());
                    assertNotNull(product.getId());
                })
                .verifyComplete();

        verify(logger).info(contains("Adding product"), eq(productName), eq(branchId), eq(stock));
        verify(logger).info(contains("Product created successfully"), any());
    }

    @Test
    void shouldReturnErrorWhenBranchNotFound() {
        String branchId = "B2";
        String productName = "Product Y";
        int stock = 5;

        when(branchRepository.findById(branchId)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(branchId, productName, stock))
                .expectErrorSatisfies(e -> assertTrue(e instanceof BranchNotFoundException))
                .verify();

        verify(logger).info(contains("Adding product"), eq(productName), eq(branchId), eq(stock));
        verify(logger, never()).info(contains("Product created successfully"), any());
    }

    @Test
    void shouldReturnErrorWhenStockIsInvalid() {
        String branchId = "B3";
        String productName = "Product Z";
        int stock = -1;

        StepVerifier.create(useCase.execute(branchId, productName, stock))
                .expectErrorSatisfies(e -> assertTrue(e instanceof InvalidStockException))
                .verify();

        verify(logger).info(contains("Adding product"), eq(productName), eq(branchId), eq(stock));
        verify(logger, never()).info(contains("Product created successfully"), any());
    }

    @Test
    void shouldLogErrorOnUpdateFailure() {
        String branchId = "B4";
        String productName = "Product W";
        int stock = 7;
        Branch branch = Branch.builder().id(branchId).products(new ArrayList<>()).build();

        when(branchRepository.findById(branchId)).thenReturn(Mono.just(branch));
        when(branchRepository.update(any(Branch.class))).thenReturn(Mono.error(new RuntimeException("DB error")));

        StepVerifier.create(useCase.execute(branchId, productName, stock))
                .expectErrorMessage("DB error")
                .verify();

        verify(logger).info(contains("Adding product"), eq(productName), eq(branchId), eq(stock));
        verify(logger).error(contains("Error creating product"), eq("DB error"));
    }
}