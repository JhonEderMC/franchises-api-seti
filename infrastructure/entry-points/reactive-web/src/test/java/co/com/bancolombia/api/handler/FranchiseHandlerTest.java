package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.entry.dto.FranchiseDTO;
import co.com.bancolombia.api.entry.dto.UpdateNameDTO;
import co.com.bancolombia.model.Franchise;
import co.com.bancolombia.usecase.franchise.CreateFranchiseUseCase;
import co.com.bancolombia.usecase.franchise.UpdateFranchiseNameUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FranchiseHandlerTest {

    private CreateFranchiseUseCase createFranchiseUseCase;
    private UpdateFranchiseNameUseCase updateFranchiseNameUseCase;
    private FranchiseHandler handler;

    @BeforeEach
    void setUp() {
        createFranchiseUseCase = mock(CreateFranchiseUseCase.class);
        updateFranchiseNameUseCase = mock(UpdateFranchiseNameUseCase.class);
        handler = new FranchiseHandler(createFranchiseUseCase, updateFranchiseNameUseCase);
    }

    @Test
    void createFranchise_shouldReturnCreatedFranchise() {
        FranchiseDTO franchiseDTO = FranchiseDTO.builder().name("TestFranchise").build();
        Franchise franchise = Franchise.builder().id("f1").name("TestFranchise").build();

        ServerRequest request = mock(ServerRequest.class);
        when(request.bodyToMono(FranchiseDTO.class)).thenReturn(Mono.just(franchiseDTO));
        when(createFranchiseUseCase.execute("TestFranchise")).thenReturn(Mono.just(franchise));

        Mono<ServerResponse> responseMono = handler.createFranchise(request);
        ServerResponse response = responseMono.block();

        assertNotNull(response);
        assertEquals(201, response.statusCode().value());
    }

    @Test
    void updateFranchiseName_shouldReturnUpdatedFranchise() {
        String franchiseId = "f1";
        UpdateNameDTO updateNameDTO = new UpdateNameDTO("UpdatedName");
        Franchise franchise = Franchise.builder().id(franchiseId).name("UpdatedName").build();

        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("franchiseId")).thenReturn(franchiseId);
        when(request.bodyToMono(UpdateNameDTO.class)).thenReturn(Mono.just(updateNameDTO));
        when(updateFranchiseNameUseCase.execute(franchiseId, "UpdatedName")).thenReturn(Mono.just(franchise));

        Mono<ServerResponse> responseMono = handler.updateFranchiseName(request);
        ServerResponse response = responseMono.block();

        assertNotNull(response);
        assertEquals(200, response.statusCode().value());
    }
}