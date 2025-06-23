package co.com.bancolombia.usecase.branch;

import co.com.bancolombia.exception.FranchiseNotFoundException;
import co.com.bancolombia.gateway.BranchRepository;
import co.com.bancolombia.gateway.FranchiseRepository;
import co.com.bancolombia.model.Branch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class AddBranchToFranchiseUseCase {
    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;

    public Mono<Branch> execute(String franchiseId, String branchName) {
        log.info("Adding branch {} to franchise {}", branchName, franchiseId);

        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .then(Mono.just(Branch.create(branchName, franchiseId)))
                .flatMap(branchRepository::save)
                .doOnSuccess(branch -> log.info("Branch created successfully with id: {}", branch.getId()))
                .doOnError(error -> log.error("Error creating branch: {}", error.getMessage()));
    }
}
