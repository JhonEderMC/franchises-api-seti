package co.com.bancolombia.usecase.product;

import co.com.bancolombia.exception.BranchNotFoundException;
import co.com.bancolombia.exception.ProductNotFoundException;
import co.com.bancolombia.gateway.BranchRepository;
import co.com.bancolombia.model.log.Logger;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RemoveProductFromBranchUseCase {

    private final BranchRepository branchRepository;
    private final Logger logger;

    public Mono<Void> execute(String branchId, String productId) {
        logger.info("Removing product {} from branch {}", productId, branchId);

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
                .doOnSuccess(v -> logger.info("Product removed successfully"))
                .doOnError(error -> logger.error("Error removing product: {}", error.getMessage()));
    }
}
