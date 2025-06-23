package co.com.bancolombia.usecase.product;

import co.com.bancolombia.exception.BranchNotFoundException;
import co.com.bancolombia.exception.ProductNotFoundException;
import co.com.bancolombia.gateway.BranchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class RemoveProductFromBranchUseCase {
    private final BranchRepository branchRepository;

    public Mono<Void> execute(String branchId, String productId) {
        log.info("Removing product {} from branch {}", productId, branchId);

        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(branchId)))
                .flatMap(branch -> {
                    boolean productExists = branch.getProducts().stream()
                            .anyMatch(p -> p.getId().equals(productId));

                    if (!productExists) {
                        return Mono.error(new ProductNotFoundException(productId));
                    }

                    return Mono.just(branch.removeProduct(productId));
                })
                .flatMap(branchRepository::update)
                .then()
                .doOnSuccess(v -> log.info("Product removed successfully"))
                .doOnError(error -> log.error("Error removing product: {}", error.getMessage()));
    }
}
