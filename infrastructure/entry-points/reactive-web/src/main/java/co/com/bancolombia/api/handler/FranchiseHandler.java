package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.entry.dto.FranchiseDTO;
import co.com.bancolombia.api.entry.dto.UpdateNameDTO;
import co.com.bancolombia.usecase.franchise.CreateFranchiseUseCase;
import co.com.bancolombia.usecase.franchise.UpdateFranchiseNameUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class FranchiseHandler {
    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final UpdateFranchiseNameUseCase updateFranchiseNameUseCase;

    public Mono<ServerResponse> createFranchise(ServerRequest request) {
        return request.bodyToMono(FranchiseDTO.class)
                .flatMap(franchiseDTO -> {
                    log.info("REST request to create franchise: {}", franchiseDTO.getName());
                    return createFranchiseUseCase.execute(franchiseDTO.getName())
                            .map(franchise -> FranchiseDTO.builder()
                                    .id(franchise.getId())
                                    .name(franchise.getName())
                                    .build());
                })
                .flatMap(dto -> ServerResponse.status(201)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dto));
    }

    public Mono<ServerResponse> updateFranchiseName(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        return request.bodyToMono(UpdateNameDTO.class)
                .flatMap(updateNameDTO -> {
                    log.info("REST request to update franchise {} name", franchiseId);
                    return updateFranchiseNameUseCase.execute(franchiseId, updateNameDTO.getName())
                            .map(franchise -> FranchiseDTO.builder()
                                    .id(franchise.getId())
                                    .name(franchise.getName())
                                    .build());
                })
                .flatMap(dto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dto));
    }
}

