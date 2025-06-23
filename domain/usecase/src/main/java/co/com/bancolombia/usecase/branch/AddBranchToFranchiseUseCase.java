package co.com.bancolombia.usecase.branch;

import co.com.bancolombia.exception.FranchiseNotFoundException;
import co.com.bancolombia.gateway.BranchRepository;
import co.com.bancolombia.gateway.FranchiseRepository;
import co.com.bancolombia.model.Branch;
import co.com.bancolombia.model.log.Logger;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AddBranchToFranchiseUseCase {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final Logger logger;

    public Mono<Branch> execute(String franchiseId, String branchName) {
        logger.info("Adding branch {} to franchise {}", branchName, franchiseId);

        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .then(Mono.just(Branch.create(branchName, franchiseId)))
                .flatMap(branchRepository::save)
                .doOnSuccess(branch -> logger.info("Branch created successfully with id: {}", branch.getId()))
                .doOnError(error -> logger.error("Error creating branch: {}", error.getMessage()));
    }
}
