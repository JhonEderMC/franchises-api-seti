package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.entry.dto.BranchDTO;
import co.com.bancolombia.api.entry.dto.UpdateNameDTO;
import co.com.bancolombia.usecase.branch.AddBranchToFranchiseUseCase;
import co.com.bancolombia.usecase.branch.UpdateBranchNameUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class BranchHandler {
    private final AddBranchToFranchiseUseCase addBranchToFranchiseUseCase;
    private final UpdateBranchNameUseCase updateBranchNameUseCase;

    public Mono<ServerResponse> addBranchToFranchise(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        return request.bodyToMono(BranchDTO.class)
                .flatMap(branchDTO -> {
                    log.info("REST request to add branch to franchise {}", franchiseId);
                    return addBranchToFranchiseUseCase.execute(franchiseId, branchDTO.getName())
                            .map(branch -> BranchDTO.builder()
                                    .id(branch.getId())
                                    .name(branch.getName())
                                    .franchiseId(branch.getFranchiseId())
                                    .build());
                })
                .flatMap(branchDTO -> ServerResponse.status(201).bodyValue(branchDTO));
    }

    public Mono<ServerResponse> updateBranchName(ServerRequest request) {
        String branchId = request.pathVariable("branchId");
        return request.bodyToMono(UpdateNameDTO.class)
                .flatMap(updateNameDTO -> {
                    log.info("REST request to update branch {} name", branchId);
                    return updateBranchNameUseCase.execute(branchId, updateNameDTO.getName())
                            .map(branch -> BranchDTO.builder()
                                    .id(branch.getId())
                                    .name(branch.getName())
                                    .franchiseId(branch.getFranchiseId())
                                    .build());
                })
                .flatMap(branchDTO -> ServerResponse.ok().bodyValue(branchDTO));
    }
}
