package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.entry.dto.BranchDTO;
import co.com.bancolombia.api.entry.dto.UpdateNameDTO;
import  co.com.bancolombia.model.Branch;
import co.com.bancolombia.usecase.branch.AddBranchToFranchiseUseCase;
import co.com.bancolombia.usecase.branch.UpdateBranchNameUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BranchHandlerTest {

    private AddBranchToFranchiseUseCase addBranchToFranchiseUseCase;
    private UpdateBranchNameUseCase updateBranchNameUseCase;
    private BranchHandler handler;

    @BeforeEach
    void setUp() {
        addBranchToFranchiseUseCase = mock(AddBranchToFranchiseUseCase.class);
        updateBranchNameUseCase = mock(UpdateBranchNameUseCase.class);
        handler = new BranchHandler(addBranchToFranchiseUseCase, updateBranchNameUseCase);
    }

    @Test
    void addBranchToFranchise_shouldReturnCreatedBranch() {
        String franchiseId = "f1";
        BranchDTO branchDTO = BranchDTO.builder().name("Branch1").build();
        Branch branch = Branch.builder().id("b1").name("Branch1").franchiseId(franchiseId).build();

        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("franchiseId")).thenReturn(franchiseId);
        when(request.bodyToMono(BranchDTO.class)).thenReturn(Mono.just(branchDTO));
        when(addBranchToFranchiseUseCase.execute(franchiseId, "Branch1")).thenReturn(Mono.just(branch));

        Mono<ServerResponse> responseMono = handler.addBranchToFranchise(request);
        ServerResponse response = responseMono.block();

        assertNotNull(response);
        assertEquals(201, response.statusCode().value());
    }

    @Test
    void updateBranchName_shouldReturnUpdatedBranch() {
        String branchId = "b1";
        UpdateNameDTO updateNameDTO = new UpdateNameDTO("NewName");
        Branch branch = Branch.builder().id(branchId).name("NewName").franchiseId("f1").build();

        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("branchId")).thenReturn(branchId);
        when(request.bodyToMono(UpdateNameDTO.class)).thenReturn(Mono.just(updateNameDTO));
        when(updateBranchNameUseCase.execute(branchId, "NewName")).thenReturn(Mono.just(branch));

        Mono<ServerResponse> responseMono = handler.updateBranchName(request);
        ServerResponse response = responseMono.block();

        assertNotNull(response);
        assertEquals(200, response.statusCode().value());
    }
}