package co.com.bancolombia.usecase.product;

import co.com.bancolombia.exception.FranchiseNotFoundException;
import co.com.bancolombia.gateway.BranchRepository;
import co.com.bancolombia.gateway.FranchiseRepository;
import co.com.bancolombia.model.Branch;
import co.com.bancolombia.model.Franchise;
import co.com.bancolombia.model.Product;
import co.com.bancolombia.model.log.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GetMaxStockProductsByFranchiseUseCaseTest {

    FranchiseRepository franchiseRepository = mock(FranchiseRepository.class);
    BranchRepository branchRepository = mock(BranchRepository.class);
    Logger logger = mock(Logger.class);
    GetMaxStockProductsByFranchiseUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetMaxStockProductsByFranchiseUseCase(franchiseRepository, branchRepository, logger);
    }

    @Test
    void shouldReturnMaxStockProductPerBranch() {
        String franchiseId = "F1";
        Branch branch1 = Branch.builder()
                .id("B1").name("Branch 1")
                .products(Arrays.asList(
                        Product.builder().id("P1").name("Prod1").stock(5).build(),
                        Product.builder().id("P2").name("Prod2").stock(10).build()
                )).build();
        Branch branch2 = Branch.builder()
                .id("B2").name("Branch 2")
                .products(Collections.singletonList(
                        Product.builder().id("P3").name("Prod3").stock(7).build()
                )).build();

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(Franchise.create(franchiseId)));
        when(branchRepository.findByFranchiseId(franchiseId)).thenReturn(Flux.just(branch1, branch2));

        StepVerifier.create(useCase.execute(franchiseId))
                .assertNext(ps -> {
                    assertEquals("P2", ps.getProductId());
                    assertEquals("Prod2", ps.getProductName());
                    assertEquals("B1", ps.getBranchId());
                    assertEquals("Branch 1", ps.getBranchName());
                    assertEquals(10, ps.getStock());
                })
                .assertNext(ps -> {
                    assertEquals("P3", ps.getProductId());
                    assertEquals("Prod3", ps.getProductName());
                    assertEquals("B2", ps.getBranchId());
                    assertEquals("Branch 2", ps.getBranchName());
                    assertEquals(7, ps.getStock());
                })
                .verifyComplete();

        verify(logger).info(contains("Getting max stock products"), eq(franchiseId));
        verify(logger).info(contains("Max stock products retrieved successfully"));
    }

    @Test
    void shouldReturnErrorWhenFranchiseNotFound() {
        String franchiseId = "F2";
        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(franchiseId))
                .expectErrorSatisfies(e -> assertTrue(e instanceof FranchiseNotFoundException))
                .verify();

        verify(logger).info(contains("Getting max stock products"), eq(franchiseId));
        verify(logger, never()).info(contains("Max stock products retrieved successfully"));
    }

    @Test
    void shouldLogErrorOnBranchRepositoryFailure() {
        String franchiseId = "F3";
        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(Franchise.create(franchiseId)));
        when(branchRepository.findByFranchiseId(franchiseId)).thenReturn(Flux.error(new RuntimeException("DB error")));

        StepVerifier.create(useCase.execute(franchiseId))
                .expectErrorMessage("DB error")
                .verify();

        verify(logger).info(contains("Getting max stock products"), eq(franchiseId));
        verify(logger).error(contains("Error getting max stock products"), eq("DB error"));
    }
}