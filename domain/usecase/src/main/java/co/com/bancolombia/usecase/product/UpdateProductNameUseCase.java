package co.com.bancolombia.usecase.product;

import co.com.bancolombia.exception.BranchNotFoundException;
import co.com.bancolombia.exception.ProductNotFoundException;
import co.com.bancolombia.gateway.BranchRepository;
import co.com.bancolombia.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class UpdateProductNameUseCase {
    private final BranchRepository branchRepository;

    public Mono<Product> execute(String branchId, String productId, String newName) {
        log.info("Updating product {} name to {} in branch {}", productId, newName, branchId);

        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(branchId)))
                .flatMap(branch -> {
                    List<Product> updatedProducts = new ArrayList<>();
                    boolean found = false;

                    for (Product product : branch.getProducts()) {
                        if (product.getId().equals(productId)) {
                            updatedProducts.add(product.withName(newName));
                            found = true;
                        } else {
                            updatedProducts.add(product);
                        }
                    }

                    if (!found) {
                        return Mono.error(new ProductNotFoundException(productId));
                    }

                    return Mono.just(branch.toBuilder().products(updatedProducts).build());
                })
                .flatMap(branchRepository::update)
                .map(branch -> branch.getProducts().stream()
                        .filter(p -> p.getId().equals(productId))
                        .findFirst()
                        .orElseThrow())
                .doOnSuccess(product -> log.info("Product name updated successfully"))
                .doOnError(error -> log.error("Error updating product name: {}", error.getMessage()));
    }
}