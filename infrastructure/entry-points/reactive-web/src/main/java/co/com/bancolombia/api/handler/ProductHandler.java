package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.entry.dto.ProductDTO;
import co.com.bancolombia.api.entry.dto.ProductStockDTO;
import co.com.bancolombia.api.entry.dto.UpdateNameDTO;
import co.com.bancolombia.api.entry.dto.UpdateStockDTO;
import co.com.bancolombia.usecase.product.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductHandler {

    public static final String PRODUCT_ID = "productId";
    public static final String BRANCH_ID = "branchId";
    private final AddProductToBranchUseCase addProductToBranchUseCase;
    private final RemoveProductFromBranchUseCase removeProductFromBranchUseCase;
    private final UpdateProductStockUseCase updateProductStockUseCase;
    private final UpdateProductNameUseCase updateProductNameUseCase;
    private final GetMaxStockProductsByFranchiseUseCase getMaxStockProductsByFranchiseUseCase;

    public Mono<ServerResponse> addProductToBranch(ServerRequest request) {
        String branchId = request.pathVariable(BRANCH_ID);
        return request.bodyToMono(ProductDTO.class)
                .flatMap(productDTO -> {
                    log.info("REST request to add product to branch {}", branchId);
                    return addProductToBranchUseCase.execute(branchId, productDTO.getName(), productDTO.getStock())
                            .map(product -> ProductDTO.builder()
                                    .id(product.getId())
                                    .name(product.getName())
                                    .stock(product.getStock())
                                    .branchId(product.getBranchId())
                                    .build());
                })
                .flatMap(productDTO -> ServerResponse.status(201)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(productDTO));
    }

    public Mono<ServerResponse> removeProductFromBranch(ServerRequest request) {
        String branchId = request.pathVariable(BRANCH_ID);
        String productId = request.pathVariable(PRODUCT_ID);
        log.info("REST request to remove product {} from branch {}", productId, branchId);
        return removeProductFromBranchUseCase.execute(branchId, productId)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> updateProductStock(ServerRequest request) {
        String branchId = request.pathVariable(BRANCH_ID);
        String productId = request.pathVariable(PRODUCT_ID);
        return request.bodyToMono(UpdateStockDTO.class)
                .flatMap(updateStockDTO -> {
                    log.info("REST request to update product {} stock in branch {}", productId, branchId);
                    return updateProductStockUseCase.execute(branchId, productId, updateStockDTO.getStock())
                            .map(product -> ProductDTO.builder()
                                    .id(product.getId())
                                    .name(product.getName())
                                    .stock(product.getStock())
                                    .branchId(product.getBranchId())
                                    .build());
                })
                .flatMap(productDTO -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(productDTO));
    }

    public Mono<ServerResponse> updateProductName(ServerRequest request) {
        String branchId = request.pathVariable(BRANCH_ID);
        String productId = request.pathVariable(PRODUCT_ID);
        return request.bodyToMono(UpdateNameDTO.class)
                .flatMap(updateNameDTO -> {
                    log.info("REST request to update product {} name in branch {}", productId, branchId);
                    return updateProductNameUseCase.execute(branchId, productId, updateNameDTO.getName())
                            .map(product -> ProductDTO.builder()
                                    .id(product.getId())
                                    .name(product.getName())
                                    .stock(product.getStock())
                                    .branchId(product.getBranchId())
                                    .build());
                })
                .flatMap(productDTO -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(productDTO));
    }

    public Mono<ServerResponse> getMaxStockProductsByFranchise(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        log.info("REST request to get max stock products for franchise {}", franchiseId);
        Flux<ProductStockDTO> result = getMaxStockProductsByFranchiseUseCase.execute(franchiseId)
                .map(productStock -> ProductStockDTO.builder()
                        .productId(productStock.getProductId())
                        .productName(productStock.getProductName())
                        .branchId(productStock.getBranchId())
                        .branchName(productStock.getBranchName())
                        .stock(productStock.getStock())
                        .build());
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result, ProductStockDTO.class);
    }
}

