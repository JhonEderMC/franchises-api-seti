package co.com.bancolombia.usecase.franchise;

import co.com.bancolombia.exception.FranchiseNotFoundException;
import co.com.bancolombia.gateway.FranchiseRepository;
import co.com.bancolombia.model.Franchise;
import co.com.bancolombia.model.log.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UpdateFranchiseNameUseCaseTest {

    FranchiseRepository franchiseRepository = mock(FranchiseRepository.class);
    Logger logger = mock(Logger.class);
    UpdateFranchiseNameUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateFranchiseNameUseCase(franchiseRepository, logger);
    }

    @Test
    void shouldUpdateFranchiseNameSuccessfully() {
        String franchiseId = "F1";
        String oldName = "Old Franchise";
        String newName = "New Franchise";
        Franchise franchise = Franchise.builder().id(franchiseId).name(oldName).build();
        Franchise updatedFranchise = franchise.withName(newName);

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(franchise));
        when(franchiseRepository.update(any(Franchise.class))).thenReturn(Mono.just(updatedFranchise));

        StepVerifier.create(useCase.execute(franchiseId, newName))
                .expectNextMatches(f -> f.getId().equals(franchiseId) && f.getName().equals(newName))
                .verifyComplete();

        verify(logger).info(contains("Updating franchise"), eq(franchiseId), eq(newName));
        verify(logger).info(contains("Franchise name updated successfully"));
        verify(franchiseRepository).update(argThat(f -> f.getName().equals(newName)));
    }

    @Test
    void shouldReturnErrorWhenFranchiseNotFound() {
        String franchiseId = "F2";
        String newName = "Any Name";

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(franchiseId, newName))
                .expectErrorSatisfies(e -> assertTrue(e instanceof FranchiseNotFoundException))
                .verify();

        verify(logger).info(contains("Updating franchise"), eq(franchiseId), eq(newName));
        verify(logger, never()).info(contains("Franchise name updated successfully"));
        verify(franchiseRepository, never()).update(any());
    }

    @Test
    void shouldLogErrorOnUpdateFailure() {
        String franchiseId = "F3";
        String oldName = "Franchise";
        String newName = "New Name";
        Franchise franchise = Franchise.builder().id(franchiseId).name(oldName).build();

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(franchise));
        when(franchiseRepository.update(any(Franchise.class))).thenReturn(Mono.error(new RuntimeException("DB error")));

        StepVerifier.create(useCase.execute(franchiseId, newName))
                .expectErrorMessage("DB error")
                .verify();

        verify(logger).info(contains("Updating franchise"), eq(franchiseId), eq(newName));
        verify(logger).error(contains("Error updating franchise name"), eq("DB error"));
    }
}