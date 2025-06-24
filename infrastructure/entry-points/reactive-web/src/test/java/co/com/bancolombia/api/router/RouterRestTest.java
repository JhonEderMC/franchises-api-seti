package co.com.bancolombia.api.router;

import co.com.bancolombia.api.handler.BranchHandler;
import co.com.bancolombia.api.handler.FranchiseHandler;
import co.com.bancolombia.api.handler.ProductHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.mockito.Mockito.*;

class RouterRestTest {

    private FranchiseHandler franchiseHandler;
    private BranchHandler branchHandler;
    private ProductHandler productHandler;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        franchiseHandler = mock(FranchiseHandler.class);
        branchHandler = mock(BranchHandler.class);
        productHandler = mock(ProductHandler.class);

        // Mock all handler methods to return a simple OK response
        when(franchiseHandler.createFranchise(any(ServerRequest.class)))
                .thenReturn(ServerResponse.ok().build());
        when(franchiseHandler.updateFranchiseName(any(ServerRequest.class)))
                .thenReturn(ServerResponse.ok().build());
        when(branchHandler.addBranchToFranchise(any(ServerRequest.class)))
                .thenReturn(ServerResponse.ok().build());
        when(branchHandler.updateBranchName(any(ServerRequest.class)))
                .thenReturn(ServerResponse.ok().build());
        when(productHandler.addProductToBranch(any(ServerRequest.class)))
                .thenReturn(ServerResponse.ok().build());
        when(productHandler.removeProductFromBranch(any(ServerRequest.class)))
                .thenReturn(ServerResponse.ok().build());
        when(productHandler.updateProductStock(any(ServerRequest.class)))
                .thenReturn(ServerResponse.ok().build());
        when(productHandler.updateProductName(any(ServerRequest.class)))
                .thenReturn(ServerResponse.ok().build());
        when(productHandler.getMaxStockProductsByFranchise(any(ServerRequest.class)))
                .thenReturn(ServerResponse.ok().build());

        RouterRest routerRest = new RouterRest();
        RouterFunction<ServerResponse> routerFunction = routerRest.routerFunction(
                franchiseHandler, branchHandler, productHandler);

        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void shouldRouteCreateFranchise() {
        webTestClient.post().uri("/api/v1/franchises")
                .exchange()
                .expectStatus().isOk();
        verify(franchiseHandler).createFranchise(any(ServerRequest.class));
    }

    @Test
    void shouldRouteUpdateFranchiseName() {
        webTestClient.put().uri("/api/v1/franchises/1/name")
                .exchange()
                .expectStatus().isOk();
        verify(franchiseHandler).updateFranchiseName(any(ServerRequest.class));
    }

    @Test
    void shouldRouteAddBranchToFranchise() {
        webTestClient.post().uri("/api/v1/franchises/1/branches")
                .exchange()
                .expectStatus().isOk();
        verify(branchHandler).addBranchToFranchise(any(ServerRequest.class));
    }

    @Test
    void shouldRouteUpdateBranchName() {
        webTestClient.put().uri("/api/v1/branches/1/name")
                .exchange()
                .expectStatus().isOk();
        verify(branchHandler).updateBranchName(any(ServerRequest.class));
    }

    @Test
    void shouldRouteAddProductToBranch() {
        webTestClient.post().uri("/api/v1/branches/1/products")
                .exchange()
                .expectStatus().isOk();
        verify(productHandler).addProductToBranch(any(ServerRequest.class));
    }

    @Test
    void shouldRouteRemoveProductFromBranch() {
        webTestClient.delete().uri("/api/v1/branches/1/products/2")
                .exchange()
                .expectStatus().isOk();
        verify(productHandler).removeProductFromBranch(any(ServerRequest.class));
    }

    @Test
    void shouldRouteUpdateProductStock() {
        webTestClient.put().uri("/api/v1/branches/1/products/2/stock")
                .exchange()
                .expectStatus().isOk();
        verify(productHandler).updateProductStock(any(ServerRequest.class));
    }

    @Test
    void shouldRouteUpdateProductName() {
        webTestClient.put().uri("/api/v1/branches/1/products/2/name")
                .exchange()
                .expectStatus().isOk();
        verify(productHandler).updateProductName(any(ServerRequest.class));
    }

    @Test
    void shouldRouteGetMaxStockProductsByFranchise() {
        webTestClient.get().uri("/api/v1/franchises/1/products/max-stock")
                .exchange()
                .expectStatus().isOk();
        verify(productHandler).getMaxStockProductsByFranchise(any(ServerRequest.class));
    }
}