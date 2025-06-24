package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.entry.dto.ProductDTO;
import co.com.bancolombia.api.entry.dto.UpdateNameDTO;
import co.com.bancolombia.api.entry.dto.UpdateStockDTO;
import co.com.bancolombia.model.Product;
import co.com.bancolombia.model.ProductStock;
import co.com.bancolombia.usecase.product.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductHandlerTest {

    private AddProductToBranchUseCase addProductToBranchUseCase;
    private RemoveProductFromBranchUseCase removeProductFromBranchUseCase;
    private UpdateProductStockUseCase updateProductStockUseCase;
    private UpdateProductNameUseCase updateProductNameUseCase;
    private GetMaxStockProductsByFranchiseUseCase getMaxStockProductsByFranchiseUseCase;
    private ProductHandler handler;

    @BeforeEach
    void setUp() {
        addProductToBranchUseCase = mock(AddProductToBranchUseCase.class);
        removeProductFromBranchUseCase = mock(RemoveProductFromBranchUseCase.class);
        updateProductStockUseCase = mock(UpdateProductStockUseCase.class);
        updateProductNameUseCase = mock(UpdateProductNameUseCase.class);
        getMaxStockProductsByFranchiseUseCase = mock(GetMaxStockProductsByFranchiseUseCase.class);
        handler = new ProductHandler(
                addProductToBranchUseCase,
                removeProductFromBranchUseCase,
                updateProductStockUseCase,
                updateProductNameUseCase,
                getMaxStockProductsByFranchiseUseCase
        );
    }

    @Test
    void addProductToBranch_shouldReturnCreatedProduct() {
        String branchId = "b1";
        ProductDTO productDTO = ProductDTO.builder().name("Product1").stock(10).build();
        Product product = Product.builder().id("p1").name("Product1").stock(10).branchId(branchId).build();

        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("branchId")).thenReturn(branchId);
        when(request.bodyToMono(ProductDTO.class)).thenReturn(Mono.just(productDTO));
        when(addProductToBranchUseCase.execute(branchId, "Product1", 10)).thenReturn(Mono.just(product));

        Mono<ServerResponse> responseMono = handler.addProductToBranch(request);
        ServerResponse response = responseMono.block();

        assertNotNull(response);
        assertEquals(201, response.statusCode().value());
    }

    @Test
    void removeProductFromBranch_shouldReturnNoContent() {
        String branchId = "b1";
        String productId = "p1";

        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("branchId")).thenReturn(branchId);
        when(request.pathVariable("productId")).thenReturn(productId);
        when(removeProductFromBranchUseCase.execute(branchId, productId)).thenReturn(Mono.empty());

        Mono<ServerResponse> responseMono = handler.removeProductFromBranch(request);
        ServerResponse response = responseMono.block();

        assertNotNull(response);
        assertEquals(204, response.statusCode().value());
    }

    @Test
    void updateProductStock_shouldReturnUpdatedProduct() {
        String branchId = "b1";
        String productId = "p1";
        UpdateStockDTO updateStockDTO = new UpdateStockDTO(20);
        Product product = Product.builder().id(productId).name("Product1").stock(20).branchId(branchId).build();

        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("branchId")).thenReturn(branchId);
        when(request.pathVariable("productId")).thenReturn(productId);
        when(request.bodyToMono(UpdateStockDTO.class)).thenReturn(Mono.just(updateStockDTO));
        when(updateProductStockUseCase.execute(branchId, productId, 20)).thenReturn(Mono.just(product));

        Mono<ServerResponse> responseMono = handler.updateProductStock(request);
        ServerResponse response = responseMono.block();

        assertNotNull(response);
        assertEquals(200, response.statusCode().value());
    }

    @Test
    void updateProductName_shouldReturnUpdatedProduct() {
        String branchId = "b1";
        String productId = "p1";
        UpdateNameDTO updateNameDTO = new UpdateNameDTO("NewName");
        Product product = Product.builder().id(productId).name("NewName").stock(10).branchId(branchId).build();

        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("branchId")).thenReturn(branchId);
        when(request.pathVariable("productId")).thenReturn(productId);
        when(request.bodyToMono(UpdateNameDTO.class)).thenReturn(Mono.just(updateNameDTO));
        when(updateProductNameUseCase.execute(branchId, productId, "NewName")).thenReturn(Mono.just(product));

        Mono<ServerResponse> responseMono = handler.updateProductName(request);
        ServerResponse response = responseMono.block();

        assertNotNull(response);
        assertEquals(200, response.statusCode().value());
    }

    @Test
    void getMaxStockProductsByFranchise_shouldReturnOk() {
        String franchiseId = "f1";
        ProductStock productStock = ProductStock.builder()
                .productId("p1")
                .productName("Product1")
                .branchId("b1")
                .branchName("Branch1")
                .stock(30)
                .build();

        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("franchiseId")).thenReturn(franchiseId);
        when(getMaxStockProductsByFranchiseUseCase.execute(franchiseId))
                .thenReturn(Flux.just(productStock));

        Mono<ServerResponse> responseMono = handler.getMaxStockProductsByFranchise(request);
        ServerResponse response = responseMono.block();

        assertNotNull(response);
        assertEquals(200, response.statusCode().value());
    }
}