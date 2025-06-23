package co.com.bancolombia.usecase.product;

import co.com.bancolombia.exception.BranchNotFoundException;
import co.com.bancolombia.exception.InvalidStockException;
import co.com.bancolombia.exception.ProductNotFoundException;
import co.com.bancolombia.gateway.BranchRepository;
import co.com.bancolombia.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class UpdateProductStockUseCase {
    private final BranchRepository branchRepository;

    public Mono<Product> execute(String branchId, String productId, int newStock) {
        log.info("Updating product {} stock to {} in branch {}", productId, newStock, branchId);

        if (newStock < 0) {
            return Mono.error(new InvalidStockException(newStock));
        }

        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(branchId)))
                .flatMap(branch -> {
                    Product product = branch.getProducts().stream()
                            .filter(p -> p.getId().equals(productId))
                            .findFirst()
                            .orElse(null);

                    if (product == null) {
                        return Mono.error(new ProductNotFoundException(productId));
                    }

                    return Mono.just(branch.updateProductStock(productId, newStock));
                })
                .flatMap(branchRepository::update)
                .map(branch -> branch.getProducts().stream()
                        .filter(p -> p.getId().equals(productId))
                        .findFirst()
                        .orElseThrow())
                .doOnSuccess(product -> log.info("Product stock updated successfully"))
                .doOnError(error -> log.error("Error updating product stock: {}", error.getMessage()));
    }
}
