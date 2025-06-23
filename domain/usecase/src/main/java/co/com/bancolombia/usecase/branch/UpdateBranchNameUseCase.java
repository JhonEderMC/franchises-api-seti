package co.com.bancolombia.usecase.branch;

import co.com.bancolombia.exception.BranchNotFoundException;
import co.com.bancolombia.gateway.BranchRepository;
import co.com.bancolombia.model.Branch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class UpdateBranchNameUseCase {
    private final BranchRepository branchRepository;

    public Mono<Branch> execute(String branchId, String newName) {
        log.info("Updating branch {} name to: {}", branchId, newName);

        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(branchId)))
                .map(branch -> branch.withName(newName))
                .flatMap(branchRepository::update)
                .doOnSuccess(branch -> log.info("Branch name updated successfully"))
                .doOnError(error -> log.error("Error updating branch name: {}", error.getMessage()));
    }
}
