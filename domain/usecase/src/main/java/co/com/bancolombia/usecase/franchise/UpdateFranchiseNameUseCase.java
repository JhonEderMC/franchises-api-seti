package co.com.bancolombia.usecase.franchise;

import co.com.bancolombia.exception.FranchiseNotFoundException;
import co.com.bancolombia.gateway.FranchiseRepository;
import co.com.bancolombia.model.Franchise;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class UpdateFranchiseNameUseCase {
    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> execute(String franchiseId, String newName) {
        log.info("Updating franchise {} name to: {}", franchiseId, newName);

        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .map(franchise -> franchise.withName(newName))
                .flatMap(franchiseRepository::update)
                .doOnSuccess(franchise -> log.info("Franchise name updated successfully"))
                .doOnError(error -> log.error("Error updating franchise name: {}", error.getMessage()));
    }
}
