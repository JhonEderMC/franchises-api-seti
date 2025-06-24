package co.com.bancolombia.api;

import co.com.bancolombia.api.handler.BranchHandler;
import co.com.bancolombia.api.handler.FranchiseHandler;
import co.com.bancolombia.api.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(FranchiseHandler franchiseHandler, BranchHandler branchHandler, ProductHandler productHandler) {
        // Franchise routes
        return route(POST("/api/v1/franchises"), franchiseHandler::createFranchise)
                .andRoute(PUT("/api/v1/franchises/{franchiseId}/name"), franchiseHandler::updateFranchiseName)
                // Branch routes
                .andRoute(POST("/api/v1/franchises/{franchiseId}/branches"), branchHandler::addBranchToFranchise)
                .andRoute(PUT("/api/v1/branches/{branchId}/name"), branchHandler::updateBranchName)
                // Product routes
                .andRoute(POST("/api/v1/branches/{branchId}/products"), productHandler::addProductToBranch)
                .andRoute(DELETE("/api/v1/branches/{branchId}/products/{productId}"), productHandler::removeProductFromBranch)
                .andRoute(PUT("/api/v1/branches/{branchId}/products/{productId}/stock"), productHandler::updateProductStock)
                .andRoute(PUT("/api/v1/branches/{branchId}/products/{productId}/name"), productHandler::updateProductName)
                .andRoute(GET("/api/v1/franchises/{franchiseId}/products/max-stock"), productHandler::getMaxStockProductsByFranchise);
    }
}
