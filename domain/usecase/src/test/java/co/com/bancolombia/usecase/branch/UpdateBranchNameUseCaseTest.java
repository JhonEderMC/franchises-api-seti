package co.com.bancolombia.usecase.branch;

import co.com.bancolombia.exception.BranchNotFoundException;
import co.com.bancolombia.gateway.BranchRepository;
import co.com.bancolombia.model.Branch;
import co.com.bancolombia.model.log.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class UpdateBranchNameUseCaseTest {

    BranchRepository branchRepository = mock(BranchRepository.class);
    Logger logger = mock(Logger.class);
    UpdateBranchNameUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateBranchNameUseCase(branchRepository, logger);
    }

    @Test
    void shouldUpdateBranchNameSuccessfully() {
        String branchId = "B1";
        String oldName = "Old Branch";
        String newName = "New Branch";
        Branch branch = Branch.builder().id(branchId).name(oldName).build();
        Branch updatedBranch = branch.withName(newName);

        when(branchRepository.findById(branchId)).thenReturn(Mono.just(branch));
        when(branchRepository.update(any(Branch.class))).thenReturn(Mono.just(updatedBranch));

        StepVerifier.create(useCase.execute(branchId, newName))
                .expectNextMatches(b -> b.getName().equals(newName) && b.getId().equals(branchId))
                .verifyComplete();

        verify(logger).info(contains("Updating branch"), eq(branchId), eq(newName));
        verify(logger).info(contains("Branch name updated successfully"));
        verify(branchRepository).update(argThat(b -> b.getName().equals(newName)));
    }

    @Test
    void shouldReturnErrorWhenBranchNotFound() {
        String branchId = "B2";
        String newName = "Any Name";

        when(branchRepository.findById(branchId)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(branchId, newName))
                .expectErrorSatisfies(e -> assertTrue(e instanceof BranchNotFoundException))
                .verify();

        verify(logger).info(contains("Updating branch"), eq(branchId), eq(newName));
        verify(logger, never()).info(contains("Branch name updated successfully"));
        verify(branchRepository, never()).update(any());
    }

    @Test
    void shouldLogErrorOnUpdateFailure() {
        String branchId = "B3";
        String oldName = "Branch";
        String newName = "New Name";
        Branch branch = Branch.builder().id(branchId).name(oldName).build();

        when(branchRepository.findById(branchId)).thenReturn(Mono.just(branch));
        when(branchRepository.update(any(Branch.class))).thenReturn(Mono.error(new RuntimeException("DB error")));

        StepVerifier.create(useCase.execute(branchId, newName))
                .expectErrorMessage("DB error")
                .verify();

        verify(logger).info(contains("Updating branch"), eq(branchId), eq(newName));
        verify(logger).error(contains("Error updating branch name"), eq("DB error"));
    }
}