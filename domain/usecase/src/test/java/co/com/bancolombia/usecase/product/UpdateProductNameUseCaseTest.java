package co.com.bancolombia.usecase.product;

import co.com.bancolombia.exception.BranchNotFoundException;
import co.com.bancolombia.exception.ProductNotFoundException;
import co.com.bancolombia.gateway.BranchRepository;
import co.com.bancolombia.model.Branch;
import co.com.bancolombia.model.Product;
import co.com.bancolombia.model.log.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UpdateProductNameUseCaseTest {

    BranchRepository branchRepository = mock(BranchRepository.class);
    Logger logger = mock(Logger.class);
    UpdateProductNameUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateProductNameUseCase(branchRepository, logger);
    }

    @Test
    void shouldUpdateProductNameSuccessfully() {
        String branchId = "B1";
        String productId = "P1";
        String oldName = "Old Product";
        String newName = "New Product";
        Product product = Product.builder().id(productId).name(oldName).build();
        Branch branch = Branch.builder().id(branchId).products(Arrays.asList(product)).build();
        Branch updatedBranch = Branch.builder().id(branchId)
                .products(Arrays.asList(product.withName(newName))).build();

        when(branchRepository.findById(branchId)).thenReturn(Mono.just(branch));
        when(branchRepository.update(any(Branch.class))).thenReturn(Mono.just(updatedBranch));

        StepVerifier.create(useCase.execute(branchId, productId, newName))
                .assertNext(updatedProduct -> {
                    assertEquals(productId, updatedProduct.getId());
                    assertEquals(newName, updatedProduct.getName());
                })
                .verifyComplete();

        verify(logger).info(contains("Updating product"), eq(productId), eq(newName), eq(branchId));
        verify(logger).info(contains("Product name updated successfully"));
    }

    @Test
    void shouldReturnErrorWhenBranchNotFound() {
        String branchId = "B2";
        String productId = "P2";
        String newName = "Any Name";

        when(branchRepository.findById(branchId)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(branchId, productId, newName))
                .expectErrorSatisfies(e -> assertTrue(e instanceof BranchNotFoundException))
                .verify();

        verify(logger).info(contains("Updating product"), eq(productId), eq(newName), eq(branchId));
        verify(logger, never()).info(contains("Product name updated successfully"));
        verify(branchRepository, never()).update(any());
    }

    @Test
    void shouldReturnErrorWhenProductNotFound() {
        String branchId = "B3";
        String productId = "P3";
        String newName = "New Name";
        Branch branch = Branch.builder().id(branchId).products(Collections.emptyList()).build();

        when(branchRepository.findById(branchId)).thenReturn(Mono.just(branch));

        StepVerifier.create(useCase.execute(branchId, productId, newName))
                .expectErrorSatisfies(e -> assertTrue(e instanceof ProductNotFoundException))
                .verify();

        verify(logger).info(contains("Updating product"), eq(productId), eq(newName), eq(branchId));
        verify(logger, never()).info(contains("Product name updated successfully"));
        verify(branchRepository, never()).update(any());
    }

    @Test
    void shouldLogErrorOnUpdateFailure() {
        String branchId = "B4";
        String productId = "P4";
        String oldName = "Product";
        String newName = "New Name";
        Product product = Product.builder().id(productId).name(oldName).build();
        Branch branch = Branch.builder().id(branchId).products(Arrays.asList(product)).build();
        Branch updatedBranch = Branch.builder().id(branchId)
                .products(Arrays.asList(product.withName(newName))).build();

        when(branchRepository.findById(branchId)).thenReturn(Mono.just(branch));
        when(branchRepository.update(any(Branch.class))).thenReturn(Mono.error(new RuntimeException("DB error")));

        StepVerifier.create(useCase.execute(branchId, productId, newName))
                .expectErrorMessage("DB error")
                .verify();

        verify(logger).info(contains("Updating product"), eq(productId), eq(newName), eq(branchId));
        verify(logger).error(contains("Error updating product name"), eq("DB error"));
    }
}