package co.com.bancolombia.usecase.product;

import co.com.bancolombia.exception.FranchiseNotFoundException;
import co.com.bancolombia.gateway.BranchRepository;
import co.com.bancolombia.gateway.FranchiseRepository;
import co.com.bancolombia.model.ProductStock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@Slf4j
@RequiredArgsConstructor
public class GetMaxStockProductsByFranchiseUseCase {
    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;

    public Flux<ProductStock> execute(String franchiseId) {
        log.info("Getting max stock products for franchise {}", franchiseId);

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
                .doOnComplete(() -> log.info("Max stock products retrieved successfully"))
                .doOnError(error -> log.error("Error getting max stock products: {}", error.getMessage()));
    }
}
