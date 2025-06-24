package co.com.bancolombia.usecase.branch;

import co.com.bancolombia.exception.BranchNotFoundException;
import co.com.bancolombia.gateway.BranchRepository;
import co.com.bancolombia.model.Branch;
import co.com.bancolombia.model.log.Logger;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateBranchNameUseCase {
    private final BranchRepository branchRepository;
    private final Logger logger;

    public Mono<Branch> execute(String branchId, String newName) {
        logger.info("Updating branch {} name to: {}", branchId, newName);

        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(branchId)))
                .map(branch -> branch.withName(newName))
                .flatMap(branchRepository::update)
                .doOnSuccess(branch -> logger.info("Branch name updated successfully"))
                .doOnError(error -> logger.error("Error updating branch name: {}", error.getMessage()));
    }
}
