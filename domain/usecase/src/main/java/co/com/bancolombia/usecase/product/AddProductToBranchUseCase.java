package co.com.bancolombia.usecase.product;

import co.com.bancolombia.exception.BranchNotFoundException;
import co.com.bancolombia.exception.InvalidStockException;
import co.com.bancolombia.gateway.BranchRepository;
import co.com.bancolombia.model.Product;
import co.com.bancolombia.model.log.Logger;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AddProductToBranchUseCase {
    private final BranchRepository branchRepository;
    private final Logger logger;

    public Mono<Product> execute(String branchId, String productName, int stock) {
        logger.info("Adding product {} to branch {} with stock {}", productName, branchId, stock);

        if (stock < 0) {
            return Mono.error(new InvalidStockException(stock));
        }

        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(branchId)))
                .map(branch -> {
                    Product product = Product.create(productName, stock, branchId);
                    product.setId(java.util.UUID.randomUUID().toString());
                    return branch.addProduct(product);
                })
                .flatMap(branchRepository::update)
                .map(branch -> branch.getProducts().get(branch.getProducts().size() - 1)) //TODO: see later if use last() method
                .doOnSuccess(product -> logger.info("Product created successfully with id: {}", product.getId()))
                .doOnError(error -> logger.error("Error creating product: {}", error.getMessage()));
    }
}
