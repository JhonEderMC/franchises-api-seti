package co.com.bancolombia.usecase.franchise;

import co.com.bancolombia.gateway.FranchiseRepository;
import co.com.bancolombia.model.Franchise;
import co.com.bancolombia.model.log.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateFranchiseUseCaseTest {

    FranchiseRepository franchiseRepository = mock(FranchiseRepository.class);
    Logger logger = mock(Logger.class);
    CreateFranchiseUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateFranchiseUseCase(franchiseRepository, logger);
    }

    @Test
    void shouldCreateFranchiseSuccessfully() {
        String name = "Franchise X";
        Franchise created = Franchise.builder().id("F1").name(name).build();

        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(created));

        StepVerifier.create(useCase.execute(name))
                .expectNextMatches(f -> f.getId().equals("F1") && f.getName().equals(name))
                .verifyComplete();

        verify(logger).info(contains("Creating new franchise"), eq(name));
        verify(logger).info(contains("Franchise created successfully"), eq("F1"));
    }

    @Test
    void shouldLogErrorWhenRepositoryFails() {
        String name = "Franchise Y";
        when(franchiseRepository.save(any(Franchise.class)))
                .thenReturn(Mono.error(new RuntimeException("DB error")));

        StepVerifier.create(useCase.execute(name))
                .expectErrorMessage("DB error")
                .verify();

        verify(logger).info(contains("Creating new franchise"), eq(name));
        verify(logger).error(contains("Error creating franchise"), eq("DB error"));
    }
}