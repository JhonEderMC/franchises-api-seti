package co.com.bancolombia.usecase.branch;

import co.com.bancolombia.exception.FranchiseNotFoundException;
import co.com.bancolombia.gateway.BranchRepository;
import co.com.bancolombia.gateway.FranchiseRepository;
import co.com.bancolombia.model.Branch;
import co.com.bancolombia.model.Franchise;
import co.com.bancolombia.model.log.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddBranchToFranchiseUseCaseTest {

    FranchiseRepository franchiseRepository = mock(FranchiseRepository.class);
    BranchRepository branchRepository = mock(BranchRepository.class);
    Logger logger = mock(Logger.class);

    AddBranchToFranchiseUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AddBranchToFranchiseUseCase(franchiseRepository, branchRepository, logger);
    }

    @Test
    void shouldAddBranchToFranchiseSuccessfully() {
        String franchiseId = "F1";
        String branchName = "Branch X";
        Branch branch = Branch.create(branchName, franchiseId);

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(Franchise.create(franchiseId)));
        when(branchRepository.save(any(Branch.class))).thenReturn(Mono.just(branch));

        StepVerifier.create(useCase.execute(franchiseId, branchName))
                .expectNextMatches(b -> b.getName().equals(branchName) && b.getFranchiseId().equals(franchiseId))
                .verifyComplete();

        verify(logger).info(anyString(), eq(branchName), eq(franchiseId));
        verify(logger).info(contains("Branch created successfully"), any());
    }

    @Test
    void shouldReturnErrorWhenFranchiseNotFound() {
        String franchiseId = "F2";
        String branchName = "Branch Y";

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(franchiseId, branchName))
                .expectErrorSatisfies(e -> assertTrue(e instanceof FranchiseNotFoundException))
                .verify();

        verify(logger).info(anyString(), eq(branchName), eq(franchiseId));
        verify(logger, never()).info(contains("Branch created successfully"), any());
    }

    @Test
    void shouldLogErrorOnBranchSaveFailure() {
        String franchiseId = "F3";
        String branchName = "Branch Z";

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(Franchise.create(franchiseId)));
        when(branchRepository.save(any(Branch.class))).thenReturn(Mono.error(new RuntimeException("DB error")));

        StepVerifier.create(useCase.execute(franchiseId, branchName))
                .expectErrorMessage("DB error")
                .verify();

        verify(logger).info(anyString(), eq(branchName), eq(franchiseId));
        verify(logger).error(contains("Error creating branch"), eq("DB error"));
    }
}