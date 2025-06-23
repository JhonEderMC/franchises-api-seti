package co.com.bancolombia.usecase.franchise;

import co.com.bancolombia.exception.FranchiseNotFoundException;
import co.com.bancolombia.gateway.FranchiseRepository;
import co.com.bancolombia.model.Franchise;
import co.com.bancolombia.model.log.Logger;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateFranchiseNameUseCase {

    private final FranchiseRepository franchiseRepository;
    private final Logger logger;

    public Mono<Franchise> execute(String franchiseId, String newName) {
        logger.info("Updating franchise {} name to: {}", franchiseId, newName);

        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .map(franchise -> franchise.withName(newName))
                .flatMap(franchiseRepository::update)
                .doOnSuccess(franchise -> logger.info("Franchise name updated successfully"))
                .doOnError(error -> logger.error("Error updating franchise name: {}", error.getMessage()));
    }
}
