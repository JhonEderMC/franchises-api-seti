package co.com.bancolombia.usecase.product;

import co.com.bancolombia.exception.FranchiseNotFoundException;
import co.com.bancolombia.gateway.BranchRepository;
import co.com.bancolombia.gateway.FranchiseRepository;
import co.com.bancolombia.model.ProductStock;
import co.com.bancolombia.model.log.Logger;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@RequiredArgsConstructor
public class GetMaxStockProductsByFranchiseUseCase {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final Logger logger;

    public Flux<ProductStock> execute(String franchiseId) {
        logger.info("Getting max stock products for franchise {}", franchiseId);

        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .thenMany(branchRepository.findByFranchiseId(franchiseId))
                .flatMap(branch -> Flux.fromIterable(branch.getProducts())
                        .sort(Comparator.comparing(p -> -p.getStock()))  // Sort by stock in descending order
                        .take(1)
                        .map(product -> ProductStock.builder()
                                .productId(product.getId())
                                .productName(product.getName())
                                .branchId(branch.getId())
                                .branchName(branch.getName())
                                .stock(product.getStock())
                                .build()))
                .doOnComplete(() -> logger.info("Max stock products retrieved successfully"))
                .doOnError(error -> logger.error("Error getting max stock products: {}", error.getMessage()));
    }
}
